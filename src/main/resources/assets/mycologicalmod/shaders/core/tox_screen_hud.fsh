#version 150

uniform sampler2D Sampler0;
uniform float Stage;
uniform float Time;
in vec2 texCoord;
out vec4 fragColor;

float hash11(float value) {
    return fract(sin(value * 127.1) * 43758.5453);
}

vec3 palette(float shade) {
    if (shade < 0.125) return vec3(0.486, 0.212, 0.027);
    if (shade < 0.250) return vec3(0.588, 0.278, 0.071);
    if (shade < 0.375) return vec3(0.686, 0.298, 0.039);
    if (shade < 0.500) return vec3(0.745, 0.341, 0.071);
    if (shade < 0.625) return vec3(0.843, 0.337, 0.000);
    if (shade < 0.750) return vec3(0.937, 0.424, 0.016);
    if (shade < 0.875) return vec3(0.910, 0.494, 0.161);
    return vec3(0.980, 0.561, 0.231);
}

void main() {
    vec2 sheetUv = vec2((texCoord.x + clamp(floor(Stage + 0.5), 0.0, 4.0)) / 5.0, texCoord.y);
    vec4 source = texture(Sampler0, sheetUv);
    bool liquidMask = source.g > 0.75 && source.r < 0.65 && source.b < 0.35 && source.a > 0.5;
    if (!liquidMask) {
        fragColor = source;
        return;
    }

    // One texture pixel maps to one GUI pixel, matching the vanilla hotbar scale.
    vec2 pixel = floor(texCoord * vec2(24.0, 48.0)) + 0.5;
    float verticalShade = clamp((48.0 - pixel.y) / 48.0, 0.0, 1.0);
    float flicker = hash11(pixel.x * 7.0 + pixel.y * 19.0 + floor(Time * 3.0) * 0.37);
    float shade = 0.38 + verticalShade * 0.30 + (flicker - 0.5) * 0.10;

    // Quantized traveling bands mimic light bending through a viscous transparent fluid.
    float refractionWave = sin(pixel.y * 0.48 + Time * 2.1
            + sin(pixel.x * 0.77 - Time * 1.3) * 1.4);
    float refractedBand = smoothstep(0.62, 0.94, refractionWave);

    // Shade the liquid as a cylindrical volume rather than a flat rectangle.
    float tubeOffset = clamp((pixel.x - 11.5) / 5.0, -1.0, 1.0);
    float tubeCurve = sqrt(max(0.0, 1.0 - tubeOffset * tubeOffset));
    float tubeEdge = 1.0 - tubeCurve;
    float hotspotX = 9.4 + 0.35 * sin(pixel.y * 0.17 - Time * 0.55);
    float tubeHotspot = 1.0 - smoothstep(0.35, 1.15, abs(pixel.x - hotspotX));
    tubeHotspot *= 0.58 + refractedBand * 0.42;
    float secondaryHotspot = (1.0 - smoothstep(0.25, 0.85, abs(pixel.x - 14.7)))
            * (0.35 + 0.25 * sin(pixel.y * 0.29 + Time * 0.7));
    shade += refractedBand * 0.16 + tubeCurve * 0.15 - tubeEdge * 0.22
            + tubeHotspot * 0.42 + secondaryHotspot * 0.19;

    float darkBubble = 0.0;
    float brightRim = 0.0;
    float popRing = 0.0;
    for (int i = 0; i < 6; i++) {
        float seed = float(i) * 13.7 + Stage * 31.1;
        float speed = 2.25 + hash11(seed + 2.0) * 2.7;
        float cycle = 7.0 + hash11(seed + 4.0) * 5.0;
        float age = mod(Time + hash11(seed) * cycle, cycle);
        vec2 center = vec2(8.0 + hash11(seed + 1.0) * 7.0,
                45.0 - age * speed);
        center.x += sin(Time * 1.7 + seed + center.y * 0.24) * 0.7;
        float radius = 1.0 + hash11(seed + 3.0) * 1.65;
        float distanceToBubble = length(pixel - center);
        darkBubble = max(darkBubble, 1.0 - smoothstep(radius - 0.25, radius + 0.35, distanceToBubble));
        brightRim = max(brightRim, 1.0 - smoothstep(0.20, 0.65, abs(distanceToBubble - radius)));

        float popLife = smoothstep(cycle - 1.0, cycle - 0.25, age);
        float popRadius = radius + popLife * 3.6;
        float ring = 1.0 - smoothstep(0.20, 0.70, abs(distanceToBubble - popRadius));
        popRing = max(popRing, ring * popLife);
    }

    shade -= darkBubble * 0.34;
    shade += brightRim * 0.25 + popRing * 0.34;

    // A dense blood-colored source billows upward into thinner, branching clouds.
    float fromBottom = 48.0 - pixel.y;
    float plumeWarp = sin(pixel.x * 0.82 + Time * 0.72) * 2.6
            + sin(pixel.y * 0.31 - Time * 0.46) * 2.0
            + sin((pixel.x + pixel.y) * 0.43 + Time * 0.29) * 1.2;
    float bloodReach = 7.0 + Stage * 5.2 + 2.2 * sin(Time * 0.34);
    float bloodBody = 1.0 - smoothstep(bloodReach * 0.42, bloodReach, fromBottom + plumeWarp);
    float tendrilNoise = hash11(floor(pixel.x * 0.5) * 11.0
            + floor((pixel.y - Time * 1.3) * 0.25) * 23.0);
    float tendrils = (1.0 - smoothstep(bloodReach, bloodReach + 8.0,
            fromBottom + plumeWarp * 1.5)) * smoothstep(0.48, 0.82, tendrilNoise);
    float blood = clamp(max(bloodBody, tendrils * 0.72), 0.0, 1.0);

    vec3 liquidColor = palette(clamp(shade, 0.0, 1.0));
    vec3 darkBlood = vec3(0.255, 0.016, 0.010);
    vec3 bloodRed = vec3(0.680, 0.035, 0.018);
    vec3 brightBlood = vec3(0.940, 0.110, 0.045);
    vec3 bloodColor = mix(darkBlood, bloodRed, clamp(fromBottom / max(bloodReach, 1.0), 0.0, 1.0));
    bloodColor = mix(bloodColor, brightBlood, brightRim * 0.45 + popRing * 0.30);
    liquidColor = mix(liquidColor, bloodColor, blood * 0.82);
    liquidColor *= mix(0.74, 1.06, tubeCurve);
    liquidColor = mix(liquidColor, vec3(1.0, 0.80, 0.52), tubeHotspot * 0.58);
    liquidColor = mix(liquidColor, vec3(1.0, 0.92, 0.72), secondaryHotspot * 0.30);

    float liquidAlpha = 0.66 + refractedBand * 0.13 + brightRim * 0.12 + popRing * 0.10;
    liquidAlpha -= darkBubble * 0.18 + tubeEdge * 0.10;
    liquidAlpha += blood * 0.10;
    liquidAlpha += tubeHotspot * 0.17 + secondaryHotspot * 0.07;
    fragColor = vec4(liquidColor, clamp(liquidAlpha, 0.42, 0.96) * source.a);
}
