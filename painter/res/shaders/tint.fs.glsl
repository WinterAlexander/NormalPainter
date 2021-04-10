#include glsl3_fs.h.glsl

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

void main()
{
    vec4 color = texture2D(u_texture, v_texCoords);

    if(color.a == 0.0)
        discard;

	if(color.r < 0.5)
		gl_FragColor.r = 2.0 * color.r * v_color.r;
	else
		gl_FragColor.r = 2.0 * (1.0 - color.r) * v_color.r + 2.0 * (color.r - 0.5);

	if(color.g < 0.5)
		gl_FragColor.g = 2.0 * color.g * v_color.g;
	else
		gl_FragColor.g = 2.0 * (1.0 - color.g) * v_color.g + 2.0 * (color.g - 0.5);

	if(color.b < 0.5)
		gl_FragColor.b = 2.0 * color.b * v_color.b;
	else
		gl_FragColor.b = 2.0 * (1.0 - color.b) * v_color.b + 2.0 * (color.b - 0.5);

	gl_FragColor.a = color.a * v_color.a;
}