#include glsl3_fs.h.glsl

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec3 u_fireColor;
uniform vec3 u_roastedColor;

uniform float u_fireLevel;

void main()
{
	// reproduced gradient can be see here
	// http://colorzilla.com/gradient-editor/#000000+0,ff7700+17,ff9232+72,ff7700+72,ff6e00+100&0.8+0,0+100

	float alpha = u_fireLevel * 0.75;
	vec3 roast_color = u_roastedColor * max(0.0, (u_fireLevel - 0.6) / 0.4)
					+ u_fireColor * min(1.0, (1.0 - u_fireLevel) / 0.4);

    vec4 color = texture2D(u_texture, v_texCoords) * v_color;

	gl_FragColor = color * (1.0 - alpha) + vec4(roast_color, 1.0) * color.a * alpha;
}