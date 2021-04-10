#include glsl3_fs.h.glsl

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

#include math.h.glsl

void main()
{
	float alpha = (1.0 - pow(4.0 * (pow2(v_texCoords.x - 0.5) + pow2(0.5 - v_texCoords.y)), 0.1)) / v_color.a;

	if(alpha <= 0.0)
		discard;

	gl_FragColor = vec4(vec3(1.0), alpha * v_color.r);
}