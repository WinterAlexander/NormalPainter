#include glsl3_fs.h.glsl

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

#include math.h.glsl

void main()
{
	float dst2 = min(1.0, pow2(v_texCoords.x - 0.5) + pow2(v_texCoords.y - 0.5));

	gl_FragColor = (1.0 - dst2) * texture2D(u_texture, v_texCoords) * v_color;
}