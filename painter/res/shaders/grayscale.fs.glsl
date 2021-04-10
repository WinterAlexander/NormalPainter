#include glsl3_fs.h.glsl

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

void main()
{
	vec4 texColor = texture2D(u_texture, v_texCoords);

	if(texColor.a == 0.0)
		discard;

	vec4 color = texColor * v_color;
	float gray = 0.299 * color.r + 0.587 * color.g + 0.114 * color.b;
	vec3 grayscale = vec3(gray);

	gl_FragColor = vec4(grayscale, color.a);
}