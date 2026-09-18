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

float stemCenter(float depth, float root, float seed) {
    return root + 0.024 * sin(depth * 21.0 + seed)
                + 0.012 * sin(depth * 47.0 + seed * 1.3);
}

float lineMask(float distance, float width, float aa) {
    return 1.0 - smoothstep(width, width + aa, distance);
}

vec2 vine(vec2 p, float root, float seed, float aa) {
    float reach = (0.23 + 0.09 * randomValue(seed)) * Growth;
    if (p.y < 0.0 || p.y > reach + 0.02 || abs(p.x - root) > 0.15) {
        return vec2(0.0);
    }
    float taper = clamp(1.0 - p.y / max(reach, 0.001), 0.0, 1.0);
    float width = 0.0012 + 0.0035 * sqrt(taper) * sqrt(Growth);
    float tip = 1.0 - smoothstep(reach - 0.008, reach, p.y);
    float distance = abs(p.x - stemCenter(p.y, root, seed));
    float body = lineMask(distance, width, aa) * tip;
    float highlight = lineMask(distance, width * 0.38, aa) * tip;

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
        float branchRoot = stemCenter(anchor, root, seed);
        float branchX = branchRoot + direction * (depth * 0.8 + 0.012 * sin(depth * 38.0));
        float branchWidth = (0.001 + 0.002 * (1.0 - clamp(depth / branchLength, 0.0, 1.0))) * sqrt(Growth);
        float branchTip = 1.0 - smoothstep(branchLength - 0.007, branchLength, depth);
        body = max(body, lineMask(abs(p.x - branchX), branchWidth, aa) * branchTip);

        // Small pointed leaves grow along each twig instead of appearing at its final endpoint.
        for (int l = 1; l <= 2; l++) {
            float leafDepth = float(l) * 0.024;
            float leafGrowth = smoothstep(leafDepth, leafDepth + 0.018, branchLength);
            if (leafGrowth <= 0.0) {
                continue;
            }
            float leafX = branchRoot + direction * (leafDepth * 0.8 + 0.012 * sin(leafDepth * 38.0));
            vec2 offset = p - vec2(leafX, anchor + leafDepth);
            vec2 axis = normalize(vec2(direction, 0.65));
            float along = dot(offset, axis) - 0.009 * leafGrowth;
            float across = dot(offset, vec2(-axis.y, axis.x));
            float leaf = length(vec2(along / (0.012 * leafGrowth), across / (0.005 * leafGrowth)));
            body = max(body, 1.0 - smoothstep(0.85, 1.1, leaf));
            highlight = max(highlight, lineMask(abs(across), 0.0007, aa)
                    * (1.0 - smoothstep(0.008 * leafGrowth, 0.012 * leafGrowth, abs(along))));
        }
    }
    return vec2(body, min(highlight, body));
}

void main() {
    if (Growth < 0.001) {
        fragColor = vec4(0.0);
        return;
    }
    float aspect = ScreenSize.x / max(ScreenSize.y, 1.0);
    float aa = 1.2 / max(ScreenSize.y, 1.0);
    vec2 masks = vec2(0.0);
    for (int edge = 0; edge < 4; edge++) {
        vec2 p;
        float span;
        if (edge == 0) { p = vec2(screenUv.y, screenUv.x * aspect); span = 1.0; }
        else if (edge == 1) { p = vec2(1.0 - screenUv.y, (1.0 - screenUv.x) * aspect); span = 1.0; }
        else if (edge == 2) { p = vec2(screenUv.x * aspect, screenUv.y); span = aspect; }
        else { p = vec2((1.0 - screenUv.x) * aspect, 1.0 - screenUv.y); span = aspect; }
        if (p.y > 0.35 * Growth + 0.02) { continue; }
        for (int i = 0; i < 4; i++) {
            float seed = float(edge) * 19.3 + float(i) * 7.1 + 2.0;
            float root = (float(i) + 0.5 + (randomValue(seed + 1.0) - 0.5) * 0.45) * span / 4.0;
            masks = max(masks, vine(p, root, seed, aa));
        }
    }
    // Palette sampled from cordyceps_lichen.png: #7b6110, #939100, #bebb12, #ffee68.
    float grain = fract(sin(dot(floor(screenUv * ScreenSize / 2.0), vec2(12.9898, 78.233))) * 43758.5453);
    vec3 color = mix(vec3(0.482, 0.380, 0.063), vec3(0.576, 0.569, 0.0), grain);
    color = mix(color, vec3(0.745, 0.733, 0.071), 0.35 + masks.y * 0.5);
    color = mix(color, vec3(1.0, 0.933, 0.408), masks.y * (0.3 + 0.035 * sin(Time * 1.3)));
    fragColor = vec4(color, masks.x * 0.88 * smoothstep(0.0, 0.025, Growth));
}
