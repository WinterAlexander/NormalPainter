#ifdef GLSL3
	#extension GL_ARB_explicit_attrib_location : enable

	#define varying in
	#define texture2D texture
#endif

varying vec2 v_backmaskCoords;
varying vec2 v_position;

uniform sampler2D u_texture; // bottom left lightmap
uniform sampler2D u_lightMap_BR;
uniform sampler2D u_lightMap_TL;
uniform sampler2D u_lightMap_TR;

uniform float u_paddingOffset;
uniform int u_lightCount;
uniform vec4 u_lights[MAX_LIGHTS];
uniform vec3 u_lightColors[MAX_LIGHTS];

#ifdef GLSL3
	layout(location = 0) out vec4 out_FragData0;
	layout(location = 1) out vec3 out_FragData1;
#endif

#include math.h.glsl

vec4 lightMapSample(sampler2D tex, vec2 coords)
{
	return texture2D(tex, coords * (1.0 - 2.0 * u_paddingOffset) + u_paddingOffset);
}

void main()
{
	float backIntensity;

	if(v_backmaskCoords.x <= 1.0)
	{
		if(v_backmaskCoords.y >= 0.0)
			backIntensity = lightMapSample(u_texture, v_backmaskCoords).r;
		else
			backIntensity = lightMapSample(u_lightMap_TL, v_backmaskCoords + vec2(0.0, 1.0)).r;
	}
	else if(v_backmaskCoords.y >= 0.0)
		backIntensity = lightMapSample(u_lightMap_BR, v_backmaskCoords + vec2(-1.0, 0.0)).r;
	else
		backIntensity = lightMapSample(u_lightMap_TR, v_backmaskCoords + vec2(-1.0, 1.0)).r;

	vec3 dir = backIntensity * vec3(0.70711, 0.70711, 1.0);
	float intensity = backIntensity;
	vec3 color = vec3(backIntensity);

	for(int i = 0; i < u_lightCount && i < MAX_LIGHTS; i++)
	{
		vec4 light = u_lights[i];

		vec2 diff = vec2(light.x - v_position.x, light.y - v_position.y);

		float lightIntensity = clamp((1.0 - pow(4.0 * (pow2(diff.x / light.z / 2.0) + pow2(diff.y / light.z / 2.0)), 0.1)) / light.w, 0.0, 1.0);
		vec3 lightDir = normalize(vec3(diff, -0.001));

		dir += lightDir * lightIntensity;
		intensity += lightIntensity;
		color += u_lightColors[i] * lightIntensity;
	}

	dir.rg = dir.rg / intensity;
	dir = dir * (1.0 - backIntensity) + backIntensity * vec3(0.70711, 0.70711, 1.0);
	color = color / intensity * (1.0 - backIntensity) + vec3(backIntensity);

	#ifdef GLSL3
		out_FragData0 = vec4(0.5 * dir.rg + 0.5, dir.b, intensity);
		out_FragData1 = color;
	#else
		gl_FragData[0] = vec4(0.5 * dir.rg + 0.5, dir.b, intensity);
		gl_FragData[1] = color;
	#endif
}