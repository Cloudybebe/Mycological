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

vec3 vine(vec2 p, float root, float seed, float aa) {
    float reach = (0.23 + 0.09 * randomValue(seed)) * Growth;
    if (p.y < 0.0 || p.y > reach + 0.02 || abs(p.x - root) > 0.15) {
        return vec3(0.0);
    }
    float taper = clamp(1.0 - p.y / max(reach, 0.001), 0.0, 1.0);
    float pointTaper = smoothstep(0.0, 0.035, reach - p.y);
    float width = (0.0036 + 0.0064 * sqrt(taper) * sqrt(Growth)) * sqrt(pointTaper);
    float distance = abs(p.x - stemCenter(p.y, root, seed, reach));
    float body = lineMask(distance, width, aa) * step(p.y, reach);
    float highlight = lineMask(distance, width * 0.38, aa) * step(p.y, reach);

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
        body = max(body, lineMask(abs(p.x - branchX), branchWidth, aa) * branchTip);

        // Forked, rounded veins replace foliage leaves.
        for (int fork = 0; fork < 2; fork++) {
            float anchorDepth = 0.024 + float(fork) * 0.026;
            float forkDepth = depth - anchorDepth;
            float forkReach = min(max(branchLength - anchorDepth, 0.0), 0.042);
            if (forkReach > 0.001 && forkDepth >= 0.0 && forkDepth <= forkReach) {
                float forkRoot = branchRoot + direction * (anchorDepth * 0.8 + 0.012 * sin(anchorDepth * 38.0));
                float forkX = forkRoot + direction * forkDepth * (fork == 0 ? -0.45 : 1.6);
                body = max(body, lineMask(abs(p.x - forkX), 0.0030 * sqrt(Growth), aa));
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
        nodule = max(nodule, 1.0 - smoothstep(radius, radius + aa, length(p - center)));
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
    return vec3(body, min(highlight, body), film);
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
    vec3 masks = vec3(0.0);
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
    // High-contrast slime palette: dark rounded rims, green-gold body, lime wet core.
    float film = masks.z;
    float grain = fract(sin(dot(floor(uv * grid), vec2(12.9898, 78.233))) * 43758.5453);
    float wetPulse = 0.82 + 0.18 * sin(Time * 1.3);
    float wetSpecks = step(0.86, fract(grain * 17.0 + floor(uv.x * grid.x) * 0.071));

    vec3 filmDark = vec3(0.145, 0.205, 0.045);
    vec3 filmBody = vec3(0.395, 0.510, 0.075);
    vec3 filmGold = vec3(0.850, 0.705, 0.075);
    float filmRim = 4.0 * film * (1.0 - film);
    vec3 filmColor = mix(filmDark, filmBody, 0.42 + grain * 0.32);
    filmColor = mix(filmColor, filmDark * 0.65, filmRim * 0.8);
    filmColor = mix(filmColor, filmGold, wetSpecks * 0.42 * wetPulse);

    vec3 veinEdge = vec3(0.075, 0.185, 0.035);
    vec3 veinBody = vec3(0.485, 0.690, 0.075);
    vec3 veinCore = vec3(0.760, 0.925, 0.185);
    vec3 wetHighlight = vec3(0.955, 1.000, 0.520);
    float roundedCore = smoothstep(0.02, 0.78, masks.y);
    vec3 veinColor = mix(veinEdge, veinBody, roundedCore);
    veinColor = mix(veinColor, veinCore, roundedCore * roundedCore);
    veinColor = mix(veinColor, wetHighlight, roundedCore * roundedCore * (0.30 + 0.16 * wetPulse));

    vec3 color = mix(filmColor, veinColor, smoothstep(0.02, 0.35, masks.x));
    fragColor = vec4(color, max(masks.x * 0.9, film * (0.26 + wetSpecks * 0.12)) * smoothstep(0.0, 0.025, Growth));
}
