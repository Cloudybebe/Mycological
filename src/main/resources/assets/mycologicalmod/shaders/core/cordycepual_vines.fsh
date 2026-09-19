#version 150

uniform float Growth;
uniform float Time;
uniform float Seed;
uniform vec2 ScreenSize;
in vec2 screenUv;
out vec4 fragColor;

float randomValue(float seed) {
    return fract(sin(seed * 127.1 + Seed * 91.7) * 43758.5453);
}

float stemCenter(float depth, float root, float seed, float reach) {
    float nearTip = smoothstep(0.42, 1.0, depth / max(reach, 0.001));
    float wriggleAmount = 0.0025 + 0.0075 * nearTip;
    float wriggle = wriggleAmount * sin(Time * 0.78 + depth * 18.0 + seed * 1.8)
                  + wriggleAmount * 0.42 * sin(Time * 1.31 - depth * 34.0 + seed);
    return root + 0.024 * sin(depth * 21.0 + seed)
                + 0.012 * sin(depth * 47.0 + seed * 1.3) + wriggle;
}

float lineMask(float distance, float width, float aa) {
    return 1.0 - smoothstep(width, width + aa, distance);
}

vec4 vine(vec2 p, float root, float seed, float aa) {
    float reach = (0.23 + 0.09 * randomValue(seed)) * Growth;
    if (p.y < 0.0 || p.y > reach + 0.02 || abs(p.x - root) > 0.15) {
        return vec4(0.0);
    }
    float taper = clamp(1.0 - p.y / max(reach, 0.001), 0.0, 1.0);
    float pointTaper = smoothstep(0.0, 0.035, reach - p.y);
    float width = (0.0036 + 0.0064 * sqrt(taper) * sqrt(Growth)) * sqrt(pointTaper);
    float distance = abs(p.x - stemCenter(p.y, root, seed, reach));
    float body = lineMask(distance, width, aa) * step(p.y, reach);
    float highlight = lineMask(distance, width * 0.38, aa) * step(p.y, reach);
    float curvature = (1.0 - clamp(distance / max(width, aa), 0.0, 1.0)) * step(p.y, reach);

    // Each twig extends from its existing parent; lowering Growth retracts the same paths.
    for (int b = 0; b < 4; b++) {
        float branchSeed = seed + float(b) * 3.71;
        float anchor = 0.025 + float(b) * 0.051;
        float branchLength = min(max(reach - anchor, 0.0), 0.062 + 0.03 * randomValue(branchSeed));
        float depth = p.y - anchor;
        if (branchLength < 0.001 || depth < 0.0 || depth > branchLength + 0.018) {
            continue;
        }
        float direction = (b % 2 == 0) ? -1.0 : 1.0;
        float branchRoot = stemCenter(anchor, root, seed, reach);
        float branchWriggle = 0.0032 * sin(Time * 0.9 + depth * 31.0 + branchSeed);
        float branchX = branchRoot + direction * (depth * 0.8 + 0.012 * sin(depth * 38.0)) + branchWriggle;
        float branchWidth = (0.0022 + 0.0034 * (1.0 - clamp(depth / branchLength, 0.0, 1.0))) * sqrt(Growth);
        float branchTip = 1.0 - smoothstep(branchLength - 0.007, branchLength, depth);
        float branchDistance = abs(p.x - branchX);
        body = max(body, lineMask(branchDistance, branchWidth, aa) * branchTip);
        curvature = max(curvature, (1.0 - clamp(branchDistance / max(branchWidth, aa), 0.0, 1.0)) * branchTip);

        // Forked, rounded veins replace foliage leaves.
        for (int fork = 0; fork < 2; fork++) {
            float anchorDepth = 0.024 + float(fork) * 0.026;
            float forkDepth = depth - anchorDepth;
            float forkReach = min(max(branchLength - anchorDepth, 0.0), 0.042);
            if (forkReach > 0.001 && forkDepth >= 0.0 && forkDepth <= forkReach) {
                float forkRoot = branchRoot + direction * (anchorDepth * 0.8 + 0.012 * sin(anchorDepth * 38.0));
                float forkX = forkRoot + direction * forkDepth * (fork == 0 ? -0.45 : 1.6);
                float forkWidth = 0.0030 * sqrt(Growth);
                float forkDistance = abs(p.x - forkX);
                body = max(body, lineMask(forkDistance, forkWidth, aa));
                curvature = max(curvature, 1.0 - clamp(forkDistance / max(forkWidth, aa), 0.0, 1.0));
            }
        }
        highlight = max(highlight, lineMask(abs(p.x - branchX + 0.001), branchWidth * 0.3, aa) * branchTip);
    }
    // Rounded nodules swell where the vein emerges from its film patch.
    float nodule = 0.0;
    for (int n = 0; n < 3; n++) {
        float nSeed = seed + float(n) * 5.3;
        vec2 center = vec2(root + (randomValue(nSeed) - 0.5) * 0.027,
                           0.006 + randomValue(nSeed + 1.0) * 0.018);
        float radius = (0.008 + randomValue(nSeed + 2.0) * 0.009) * smoothstep(0.02, 0.18, Growth);
        float noduleDistance = length(p - center);
        nodule = max(nodule, 1.0 - smoothstep(radius, radius + aa, noduleDistance));
        curvature = max(curvature, 1.0 - clamp(noduleDistance / max(radius, aa), 0.0, 1.0));
    }
    body = max(body, nodule);
    highlight = max(highlight, nodule * lineMask(length(p - vec2(root - 0.004, 0.015)), 0.005, aa));

    // The slime film grows radially from this exact vine source, with a lumpy living front.
    vec2 fromRoot = vec2((p.x - root) * 0.78, p.y);
    float angle = atan(fromRoot.y, fromRoot.x);
    float lumpyEdge = 0.009 * sin(angle * 7.0 + seed) + 0.005 * sin(angle * 13.0 - seed * 0.7);
    float filmRadius = Growth * (0.105 + 0.035 * randomValue(seed + 8.0)) + lumpyEdge * Growth;
    float film = 1.0 - smoothstep(filmRadius - 0.008, filmRadius + 0.003, length(fromRoot));
    film *= smoothstep(0.005, 0.08, Growth);
    return vec4(body, min(highlight, body), film, curvature);
}

void main() {
    if (Growth < 0.001) {
        fragColor = vec4(0.0);
        return;
    }
    // Sample the entire effect on a 270-pixel-high grid, independent of display resolution.
    vec2 grid = vec2(270.0 * ScreenSize.x / max(ScreenSize.y, 1.0), 270.0);
    vec2 uv = (floor(screenUv * grid) + 0.5) / grid;
    float aspect = ScreenSize.x / max(ScreenSize.y, 1.0);
    float aa = 0.001;
    vec4 masks = vec4(0.0);
    for (int edge = 0; edge < 4; edge++) {
        vec2 p;
        float span;
        if (edge == 0) { p = vec2(uv.y, uv.x * aspect); span = 1.0; }
        else if (edge == 1) { p = vec2(1.0 - uv.y, (1.0 - uv.x) * aspect); span = 1.0; }
        else if (edge == 2) { p = vec2(uv.x * aspect, uv.y); span = aspect; }
        else { p = vec2((1.0 - uv.x) * aspect, 1.0 - uv.y); span = aspect; }
        if (p.y > 0.35 * Growth + 0.02) { continue; }
        for (int i = 0; i < 4; i++) {
            float seed = float(edge) * 19.3 + float(i) * 7.1 + 2.0;
            float root = (float(i) + 0.5 + (randomValue(seed + 1.0) - 0.5) * 0.45) * span / 4.0;
            masks = max(masks, vine(p, root, seed, aa));
        }
    }
    // Six real lichen colors chosen by analytic curvature; alpha is kept separate.
    float film = masks.z;
    float grain = fract(sin(dot(floor(uv * grid), vec2(12.9898, 78.233))) * 43758.5453);
    float wetPulse = 0.82 + 0.18 * sin(Time * 1.3);
    float wetSpecks = step(0.86, fract(grain * 17.0 + floor(uv.x * grid.x) * 0.071));

    vec3 brown = vec3(0.369, 0.247, 0.043);   // #5e3f0b
    vec3 darkOlive = vec3(0.482, 0.380, 0.063); // #7b6110
    vec3 warmOlive = vec3(0.600, 0.475, 0.098); // #997919
    vec3 lichen = vec3(0.745, 0.733, 0.071);  // #bebb12
    vec3 gold = vec3(0.863, 0.776, 0.090);    // #dcc617
    vec3 cream = vec3(1.000, 0.933, 0.408);   // #ffee68

    float filmRim = 4.0 * film * (1.0 - film);
    float filmBand = floor(grain * 4.0);
    vec3 filmColor = filmBand < 1.0 ? brown
            : filmBand < 2.0 ? darkOlive
            : filmBand < 3.0 ? warmOlive : lichen;
    filmColor = mix(filmColor, brown * 0.72, filmRim * 0.82);
    filmColor = mix(filmColor, gold, wetSpecks * 0.34 * wetPulse);
    // A tiny fixed-grid dither breaks up band contours while preserving the six-color palette.
    float shade = clamp(masks.w + (grain - 0.5) * 0.10, 0.0, 0.999);
    float band = floor(shade * 6.0);
    vec3 veinColor = band < 1.0 ? brown
            : band < 2.0 ? darkOlive
            : band < 3.0 ? warmOlive
            : band < 4.0 ? lichen
            : band < 5.0 ? gold : cream;
    // Sparse wet glints use the existing core mask; no additional geometry pass is drawn.
    veinColor = mix(veinColor, cream, masks.y * wetSpecks * 0.28 * wetPulse);
    vec3 color = mix(filmColor, veinColor, smoothstep(0.02, 0.35, masks.x));
    fragColor = vec4(color, max(masks.x * 0.9, film * (0.34 + wetSpecks * 0.10)) * smoothstep(0.0, 0.025, Growth));
}
