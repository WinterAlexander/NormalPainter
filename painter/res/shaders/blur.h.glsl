

vec4 blur(sampler2D image, vec2 uv)
{
	vec4 color = vec4(0.0);
	color += texture2D(image, uv) * 0.29411764705882354;

	vec2 dir = vec2(0.7, 0.7);
	vec2 off1 = 0.02 * dir;
	color += texture2D(image, uv + off1) * 0.35294117647058826;
	color += texture2D(image, uv - off1) * 0.35294117647058826;
	return color;
}