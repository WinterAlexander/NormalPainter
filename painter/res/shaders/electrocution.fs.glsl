#include glsl3_fs.h.glsl

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform int u_time;

#include lighting.h.glsl

void main()
{
    vec4 color = sunPreshaded(u_texture, v_texCoords) * v_color;

	color.rgb *= mod(u_time / 10.0, 2.0);

	gl_FragColor = color;
}