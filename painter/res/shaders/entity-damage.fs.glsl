#include glsl3_fs.h.glsl

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec3 u_dmgColor;

uniform float u_dmgCooldown;

#include lighting.h.glsl

void main()
{
    vec4 color = sunPreshaded(u_texture, v_texCoords) * v_color;

	float alpha = -3.0 * (u_dmgCooldown - 0.5) * (u_dmgCooldown - 0.5) + 0.75; //y = -3(x-0.5)^2 + 0.75 parabola (75% opacity at peak)

	gl_FragColor = color * (1.0 - alpha) + vec4(u_dmgColor, 1.0) * color.a * alpha;
}