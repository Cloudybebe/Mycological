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

    // The GUI sheet is authored at 32 x 64 pixels per frame, giving the liquid
    // four times the pixel detail of the original while retaining hard pixel edges.
    vec2 pixel = floor(texCoord * vec2(32.0, 64.0)) + 0.5;
    float verticalShade = clamp((64.0 - pixel.y) / 64.0, 0.0, 1.0);
    float flicker = hash11(pixel.x * 7.0 + pixel.y * 19.0 + floor(Time * 3.0) * 0.37);
    float shade = 0.38 + verticalShade * 0.30 + (flicker - 0.5) * 0.10;

    // Quantized traveling bands mimic light bending through a viscous transparent fluid.
    float refractionWave = sin(pixel.y * 0.72 + Time * 2.1
            + sin(pixel.x * 1.15 - Time * 1.3) * 1.4);
    float refractedBand = smoothstep(0.62, 0.94, refractionWave);
    float glassEdge = 1.0 - smoothstep(0.0, 3.5, min(abs(pixel.x - 11.0), abs(pixel.x - 21.0)));
    shade += refractedBand * 0.18 + glassEdge * 0.08;

    float darkBubble = 0.0;
    float brightRim = 0.0;
    float popRing = 0.0;
    for (int i = 0; i < 5; i++) {
        float seed = float(i) * 13.7 + Stage * 31.1;
        float speed = 3.0 + hash11(seed + 2.0) * 3.6;
        float cycle = 7.0 + hash11(seed + 4.0) * 5.0;
        float age = mod(Time + hash11(seed) * cycle, cycle);
        vec2 center = vec2(12.0 + hash11(seed + 1.0) * 8.0,
                60.0 - age * speed);
        center.x += sin(Time * 1.7 + seed + center.y * 0.35) * 0.45;
        float radius = 1.4 + hash11(seed + 3.0) * 2.3;
        float distanceToBubble = length(pixel - center);
        darkBubble = max(darkBubble, 1.0 - smoothstep(radius - 0.25, radius + 0.35, distanceToBubble));
        brightRim = max(brightRim, 1.0 - smoothstep(0.20, 0.65, abs(distanceToBubble - radius)));

        float popLife = smoothstep(cycle - 1.0, cycle - 0.25, age);
        float popRadius = radius + popLife * 4.8;
        float ring = 1.0 - smoothstep(0.20, 0.70, abs(distanceToBubble - popRadius));
        popRing = max(popRing, ring * popLife);
    }

    shade -= darkBubble * 0.34;
    shade += brightRim * 0.25 + popRing * 0.34;
    float liquidAlpha = 0.66 + refractedBand * 0.13 + brightRim * 0.12 + popRing * 0.10;
    liquidAlpha -= darkBubble * 0.18;
    fragColor = vec4(palette(clamp(shade, 0.0, 1.0)), clamp(liquidAlpha, 0.42, 0.94) * source.a);
}
