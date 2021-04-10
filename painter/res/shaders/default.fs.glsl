#include glsl3_fs.h.glsl

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

#include lighting.h.glsl

void main()
{
	gl_FragColor = sunPreshaded(u_texture, v_texCoords) * v_color;
}