#version 150

uniform float Growth;
uniform float Time;
uniform float Seed;
uniform vec2 ScreenSize;
in vec2 screenUv;
out vec4 fragColor;

float hash11(float value) {
    return fract(sin(value * 127.1 + Seed * 91.7) * 43758.5453);
}

vec2 hash22(vec2 cell, float seed) {
    vec2 value = vec2(dot(cell, vec2(127.1, 311.7)), dot(cell, vec2(269.5, 183.3)));
    return fract(sin(value + seed * 17.17 + Seed * 53.41) * 43758.5453);
}

vec3 cellularDistances(vec2 p, float seed) {
    vec2 base = floor(p);
    vec2 local = fract(p);
    float first = 8.0;
    float second = 8.0;
    float third = 8.0;
    for (int y = -1; y <= 1; y++) {
        for (int x = -1; x <= 1; x++) {
            vec2 offset = vec2(float(x), float(y));
            vec2 point = offset + hash22(base + offset, seed) - local;
            float distanceToPoint = length(point);
            if (distanceToPoint < first) {
                third = second;
                second = first;
                first = distanceToPoint;
            } else if (distanceToPoint < second) {
                third = second;
                second = distanceToPoint;
            } else if (distanceToPoint < third) {
                third = distanceToPoint;
            }
        }
    }
    return vec3(first, second, third);
}

vec3 roundedLichenPalette(float shade) {
    vec3 brown = vec3(0.369, 0.247, 0.043);
    vec3 darkOlive = vec3(0.482, 0.380, 0.063);
    vec3 warmOlive = vec3(0.600, 0.475, 0.098);
    vec3 lichen = vec3(0.745, 0.733, 0.071);
    vec3 gold = vec3(0.863, 0.776, 0.090);
    vec3 cream = vec3(1.000, 0.933, 0.408);
    float scaled = clamp(shade, 0.0, 1.0) * 5.0;
    if (scaled < 1.0) return mix(brown, darkOlive, smoothstep(0.0, 1.0, scaled));
    if (scaled < 2.0) return mix(darkOlive, warmOlive, smoothstep(1.0, 2.0, scaled));
    if (scaled < 3.0) return mix(warmOlive, lichen, smoothstep(2.0, 3.0, scaled));
    if (scaled < 4.0) return mix(lichen, gold, smoothstep(3.0, 4.0, scaled));
    return mix(gold, cream, smoothstep(4.0, 5.0, scaled));
}

vec4 slimeFromEdge(vec2 p, float span, float edgeSeed, float aa) {
    float sheet = 0.0;
    float innerSheet = 0.0;
    float sourceTrunks = 0.0;
    float sourceHighlight = 0.0;
    float sourceCurvature = 0.0;

    // Overlapping colonies make one broad advancing sheet instead of isolated circles.
    for (int i = 0; i < 3; i++) {
        float seed = edgeSeed + float(i) * 7.1;
        float root = (float(i) + 0.5 + (hash11(seed + 1.0) - 0.5) * 0.34) * span / 3.0;
        vec2 fromRoot = vec2((p.x - root) * 0.72, p.y);
        float angle = atan(fromRoot.y, fromRoot.x);
        float broadLobes = 0.018 * sin(angle * 3.0 + seed)
                         + 0.010 * sin(angle * 5.0 - seed * 0.4);
        float fingers = 0.006 * sin(angle * 13.0 + seed * 1.7)
                      + 0.003 * sin(angle * 23.0 - seed);
        float breathing = 0.0025 * sin(Time * 0.42 + seed + angle * 2.0);
        float radius = Growth * (0.145 + 0.045 * hash11(seed + 8.0) + broadLobes + fingers)
                     + breathing * smoothstep(0.08, 0.5, Growth);
        float distanceFromRoot = length(fromRoot);
        float colony = 1.0 - smoothstep(radius - aa * 2.0, radius + aa * 3.0, distanceFromRoot);
        float colonyInner = 1.0 - smoothstep(radius - 0.018, radius - 0.006, distanceFromRoot);
        sheet = max(sheet, colony);
        innerSheet = max(innerSheet, colonyInner);

        // Heavy transport channels connect the cellular web to each source.
        float center = root + 0.025 * sin(p.y * 18.0 + seed)
                            + 0.011 * sin(p.y * 41.0 - seed * 0.7);
        float rootReach = radius * 1.08;
        float tip = 1.0 - smoothstep(rootReach - 0.014, rootReach, p.y);
        float taper = clamp(1.0 - p.y / max(rootReach, 0.001), 0.0, 1.0);
        float flow = 0.72 + 0.52 * (0.5 + 0.5 * sin(Time * 1.05 - p.y * 35.0 + seed));
        float trunkWidth = (0.004 + 0.007 * sqrt(taper)) * flow;
        float trunkDistance = abs(p.x - center);
        sourceTrunks = max(sourceTrunks, (1.0 - smoothstep(trunkWidth, trunkWidth + aa, trunkDistance)) * tip);
        sourceCurvature = max(sourceCurvature,
                clamp(1.0 - trunkDistance / max(trunkWidth, aa), 0.0, 1.0) * tip);
        sourceHighlight = max(sourceHighlight,
                (1.0 - smoothstep(trunkWidth * 0.30, trunkWidth * 0.30 + aa, trunkDistance)) * tip);
    }

    if (sheet <= 0.001 && sourceTrunks <= 0.001) {
        return vec4(0.0);
    }

    // Voronoi borders form the looping, reconnecting transport network seen in slime mold.
    vec2 warpedPoint = p;
    warpedPoint.x += 0.030 * sin(p.y * 29.0 + edgeSeed)
                   + 0.014 * sin((p.x + p.y) * 47.0 - edgeSeed);
    warpedPoint.y += 0.025 * sin(p.x * 25.0 - edgeSeed * 0.7)
                   + 0.011 * sin((p.x - p.y) * 39.0 + edgeSeed);
    // Slow opposing waves make the living web crawl while leaving its edge sources anchored.
    float movement = smoothstep(0.008, 0.075, p.y);
    warpedPoint.x += movement * (0.010 * sin(Time * 0.48 + p.y * 24.0 + edgeSeed)
                   + 0.005 * sin(Time * 0.31 - p.y * 41.0));
    warpedPoint.y += movement * (0.008 * sin(Time * 0.41 + p.x * 21.0 - edgeSeed)
                   + 0.004 * sin(Time * 0.27 + p.x * 37.0));
    vec2 networkPoint = vec2(warpedPoint.x * 16.0, warpedPoint.y * 19.0);
    vec3 cells = cellularDistances(networkPoint, edgeSeed);
    float borderDistance = cells.y - cells.x;
    float flowWave = 0.5 + 0.5 * sin(Time * 1.12 - p.y * 38.0 + edgeSeed);
    float organicWidth = 0.82 + 0.24 * sin(p.x * 21.0 + p.y * 16.0 + edgeSeed);
    float veinWidth = mix(0.040, 0.115, flowWave) * organicWidth;
    float web = (1.0 - smoothstep(veinWidth, veinWidth + 0.035, borderDistance)) * sheet;

    // Triple-cell junctions make irregular accumulations rather than evenly spaced beads.
    float junction = (1.0 - smoothstep(0.08, 0.16, cells.z - cells.x)) * web;
    float body = max(web, sourceTrunks);
    float travelingHighlight = web * smoothstep(0.70, 0.98, flowWave) * 0.62;
    float highlight = max(max(sourceHighlight, junction * 0.72), travelingHighlight);
    float curvature = max(clamp(1.0 - borderDistance / max(veinWidth, 0.001), 0.0, 1.0), sourceCurvature);

    // The newest rim stays dense and exploratory while the interior resolves into channels.
    float advancingRim = clamp(sheet - innerSheet, 0.0, 1.0);
    float fringeNoise = hash11(floor(p.x * 270.0) + floor(p.y * 270.0) * 0.37 + edgeSeed);
    float exploratoryFront = advancingRim * smoothstep(0.28, 0.82, fringeNoise);
    body = max(body, exploratoryFront * (0.45 + web * 0.55));
    float film = sheet * (0.58 + innerSheet * 0.20);
    return vec4(body, min(highlight, body), film, curvature);
}

void main() {
    if (Growth < 0.001) {
        fragColor = vec4(0.0);
        return;
    }

    // A fixed virtual resolution gives the growth a deliberate Minecraft-scale pixel edge.
    vec2 grid = vec2(270.0 * ScreenSize.x / max(ScreenSize.y, 1.0), 270.0);
    vec2 uv = (floor(screenUv * grid) + 0.5) / grid;
    float aspect = ScreenSize.x / max(ScreenSize.y, 1.0);
    float aa = 0.0015;
    vec4 masks = vec4(0.0);
    for (int edge = 0; edge < 4; edge++) {
        vec2 p;
        float span;
        if (edge == 0) { p = vec2(uv.y, uv.x * aspect); span = 1.0; }
        else if (edge == 1) { p = vec2(1.0 - uv.y, (1.0 - uv.x) * aspect); span = 1.0; }
        else if (edge == 2) { p = vec2(uv.x * aspect, uv.y); span = aspect; }
        else { p = vec2((1.0 - uv.x) * aspect, 1.0 - uv.y); span = aspect; }
        if (p.y <= 0.24 * Growth + 0.035) {
            masks = max(masks, slimeFromEdge(p, span, float(edge) * 19.3 + 2.0, aa));
        }
    }

    float film = masks.z;
    float wetPulse = 0.82 + 0.18 * sin(Time * 1.15);
    float smoothSheen = 0.5 + 0.5 * sin(uv.x * 12.0 + Time * 0.12)
                                   * sin(uv.y * 15.0 - Time * 0.09);
    vec3 brown = vec3(0.369, 0.247, 0.043);
    vec3 darkOlive = vec3(0.482, 0.380, 0.063);
    vec3 warmOlive = vec3(0.600, 0.475, 0.098);
    vec3 lichen = vec3(0.745, 0.733, 0.071);
    vec3 gold = vec3(0.863, 0.776, 0.090);
    vec3 cream = vec3(1.000, 0.933, 0.408);

    float filmRim = 4.0 * film * (1.0 - film);
    vec3 filmColor = mix(darkOlive, warmOlive, 0.45 + smoothSheen * 0.20);
    filmColor = mix(filmColor, brown * 0.72, filmRim * 0.76);
    filmColor = mix(filmColor, gold, smoothstep(0.78, 1.0, smoothSheen) * 0.18 * wetPulse);

    // Continuous edge-to-center palette interpolation gives every channel a rounded cross-section.
    vec3 veinColor = roundedLichenPalette(masks.w);
    veinColor = mix(veinColor, cream, masks.y * (0.18 + 0.20 * smoothSheen) * wetPulse);
    vec3 color = mix(filmColor, veinColor, smoothstep(0.04, 0.48, masks.x));
    float alpha = max(masks.x * 0.92, film * (0.28 + smoothSheen * 0.06));
    fragColor = vec4(color, alpha * smoothstep(0.0, 0.025, Growth));
}
