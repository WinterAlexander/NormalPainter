#include glsl3_fs.h.glsl

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_biomeMask;

uniform vec2 u_offset;
uniform vec2 u_scale;

void main()
{
    vec4 color = texture2D(u_texture, v_texCoords);

	//coordinates in the map corresponds to texCoordinates * scale + offset but with Y reversed

	vec2 mapCoords = v_texCoords;
	mapCoords.y = 1.0 - mapCoords.y;
	mapCoords *= u_scale;
	mapCoords += u_offset;
	mapCoords.y = 1.0 - mapCoords.y;

	//alpha corresponds to the alpha of the mask
    float a = texture2D(u_biomeMask, mapCoords).a;

	//apply alpha
	gl_FragColor = vec4(color.rgb, a);

	//to debug the mask
	//gl_FragColor = texture2D(u_biomeMask, mapCoords);
}