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

float anchoredStemCenter(float depth, float root, float seed) {
    return root + 0.024 * sin(depth * 21.0 + seed)
                + 0.012 * sin(depth * 47.0 + seed * 1.3);
}

float noduleSideOffset(float seed, int index) {
    float nSeed = seed + float(index) * 5.3;
    return (randomValue(nSeed) - 0.5) * (0.010 + 0.005 * float(index % 2));
}
float lineMask(float distance, float width, float aa) {
    return 1.0 - smoothstep(width, width + aa, distance);
}

vec4 vine(vec2 p, float root, float seed, float aa) {
    float reach = (0.23 + 0.09 * randomValue(seed)) * Growth;
    if (p.y < 0.0 || p.y > reach + 0.05 || abs(p.x - root) > 0.15) {
        return vec4(0.0);
    }
    float taper = clamp(1.0 - p.y / max(reach, 0.001), 0.0, 1.0);
    float pointTaper = smoothstep(0.0, 0.035, reach - p.y);
    float rootSwelling = 1.0 + 1.35 * Growth * (1.0 - smoothstep(0.015, 0.145, p.y));
    float width = (0.0036 + 0.0064 * sqrt(taper) * sqrt(Growth)) * sqrt(pointTaper) * rootSwelling;
    float filmWidth = 0.034 * Growth;
    float distance = abs(p.x - stemCenter(p.y, root, seed, reach));
    float body = lineMask(distance, width, aa) * step(p.y, reach);
    float highlight = lineMask(distance, width * 0.38, aa) * step(p.y, reach);
    float curvature = (1.0 - clamp(distance / max(width, aa), 0.0, 1.0)) * step(p.y, reach);
    float mainFilmDistance = length(vec2(distance, max(p.y - reach, 0.0)));
    float film = lineMask(mainFilmDistance, width + filmWidth, aa * 2.0);

    // Each twig extends from its existing parent; lowering Growth retracts the same paths.
    for (int b = 0; b < 5; b++) {
        float branchSeed = seed + float(b) * 5.3;
        float anchor = 0.035 + float(b) * 0.055;
        float branchLength = min(max(reach - anchor, 0.0), 0.062 + 0.03 * randomValue(branchSeed));
        float depth = p.y - anchor;
        if (branchLength < 0.001 || depth < 0.0 || depth > branchLength + 0.05) {
            continue;
        }
        float direction = (b % 2 == 0) ? -1.0 : 1.0;
        float branchRoot = anchoredStemCenter(anchor, root, seed) + noduleSideOffset(seed, b);
        float branchWriggle = 0.0032 * sin(Time * 0.9 + depth * 31.0 + branchSeed) * smoothstep(0.0, 0.030, depth);
        float branchX = branchRoot + direction * (depth * 0.8 + 0.012 * sin(depth * 38.0)) + branchWriggle;
        float branchWidth = (0.0022 + 0.0034 * (1.0 - clamp(depth / branchLength, 0.0, 1.0))) * sqrt(Growth);
        float branchTip = 1.0 - smoothstep(branchLength - 0.007, branchLength, depth);
        float branchDistance = abs(p.x - branchX);
        body = max(body, lineMask(branchDistance, branchWidth, aa) * branchTip);
        curvature = max(curvature, (1.0 - clamp(branchDistance / max(branchWidth, aa), 0.0, 1.0)) * branchTip);
        float branchFilmDistance = length(vec2(branchDistance, max(depth - branchLength, 0.0)));
        film = max(film, lineMask(branchFilmDistance, branchWidth + filmWidth, aa * 2.0));

        // Forked, rounded veins replace foliage leaves.
        for (int fork = 0; fork < 2; fork++) {
            float anchorDepth = 0.024 + float(fork) * 0.026;
            float forkDepth = depth - anchorDepth;
            float forkReach = min(max(branchLength - anchorDepth, 0.0), 0.042);
            if (forkReach > 0.001 && forkDepth >= 0.0 && forkDepth <= forkReach + 0.04) {
                float forkRoot = branchRoot + direction * (anchorDepth * 0.8 + 0.012 * sin(anchorDepth * 38.0));
                float forkX = forkRoot + direction * forkDepth * (fork == 0 ? -0.45 : 1.6);
                float forkWidth = 0.0030 * sqrt(Growth);
                float forkDistance = abs(p.x - forkX);
                float forkBodyGate = step(forkDepth, forkReach);
                body = max(body, lineMask(forkDistance, forkWidth, aa) * forkBodyGate);
                curvature = max(curvature, (1.0 - clamp(forkDistance / max(forkWidth, aa), 0.0, 1.0)) * forkBodyGate);
                float forkFilmDistance = length(vec2(forkDistance, max(forkDepth - forkReach, 0.0)));
                film = max(film, lineMask(forkFilmDistance, forkWidth + filmWidth * 0.72, aa * 2.0));
            }
        }
        highlight = max(highlight, lineMask(abs(p.x - branchX + 0.001), branchWidth * 0.3, aa) * branchTip);
    }
    // Each nodule appears when growth reaches a fixed point, stays anchored, and sources a branch.
    float nodule = 0.0;
    for (int n = 0; n < 5; n++) {
        float nSeed = seed + float(n) * 5.3;
        float noduleDepth = 0.035 + float(n) * 0.055;
        float activation = smoothstep(noduleDepth, noduleDepth + 0.014, reach);
        vec2 center = vec2(anchoredStemCenter(noduleDepth, root, seed) + noduleSideOffset(seed, n), noduleDepth);
        float radius = (0.006 + randomValue(nSeed + 2.0) * 0.006) * activation;
        radius *= 1.0 + 0.38 * Growth * (1.0 - float(n) / 5.0);
        float noduleDistance = length(p - center);
        float noduleMask = 1.0 - smoothstep(radius, radius + aa, noduleDistance);
        nodule = max(nodule, noduleMask);
        curvature = max(curvature, (1.0 - clamp(noduleDistance / max(radius, aa), 0.0, 1.0)) * activation);
        film = max(film, (1.0 - smoothstep(radius + filmWidth * 0.75,
                radius + filmWidth * 0.75 + aa * 2.0, noduleDistance)) * activation);
    }
    body = max(body, nodule);
    highlight = max(highlight, nodule * curvature);
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
            float cornerTilt = root < span * 0.27 ? 0.68 : root > span * 0.73 ? -0.68 : 0.0;
            vec2 angledP = vec2(p.x - cornerTilt * p.y, p.y);
            masks = max(masks, vine(angledP, root, seed, aa));
        }
    }
    // Six real lichen colors chosen by analytic curvature; alpha is kept separate.
    float film = masks.z;
    float grain = fract(sin(dot(floor(uv * grid), vec2(12.9898, 78.233))) * 43758.5453);
    float wetPulse = 0.82 + 0.18 * sin(Time * 1.3);
    float smoothSheen = 0.5 + 0.5 * sin(uv.x * 12.0 + Time * 0.12) * sin(uv.y * 15.0 - Time * 0.09);

    vec3 brown = vec3(0.369, 0.247, 0.043);   // #5e3f0b
    vec3 darkOlive = vec3(0.482, 0.380, 0.063); // #7b6110
    vec3 warmOlive = vec3(0.600, 0.475, 0.098); // #997919
    vec3 lichen = vec3(0.745, 0.733, 0.071);  // #bebb12
    vec3 gold = vec3(0.863, 0.776, 0.090);    // #dcc617
    vec3 cream = vec3(1.000, 0.933, 0.408);   // #ffee68

float filmRim = 4.0 * film * (1.0 - film);
    vec3 filmColor = mix(darkOlive, warmOlive, 0.48 + smoothSheen * 0.20);
    filmColor = mix(filmColor, brown * 0.72, filmRim * 0.82);
    filmColor = mix(filmColor, gold, smoothstep(0.76, 1.0, smoothSheen) * 0.22 * wetPulse);

    // A tiny fixed-grid dither breaks up band contours while preserving the six-color palette.
    float shade = clamp(masks.w + (grain - 0.5) * 0.10, 0.0, 0.999);
    float band = floor(shade * 6.0);
    vec3 veinColor = band < 1.0 ? brown
            : band < 2.0 ? darkOlive
            : band < 3.0 ? warmOlive
            : band < 4.0 ? lichen
            : band < 5.0 ? gold : cream;
    // Sparse wet glints use the existing core mask; no additional geometry pass is drawn.
    veinColor = mix(veinColor, cream, masks.y * (0.16 + 0.16 * smoothSheen) * wetPulse);
    vec3 color = mix(filmColor, veinColor, smoothstep(0.02, 0.35, masks.x));
    fragColor = vec4(color, max(masks.x * 0.9, film * (0.35 + smoothSheen * 0.07)) * smoothstep(0.0, 0.025, Growth));
}
