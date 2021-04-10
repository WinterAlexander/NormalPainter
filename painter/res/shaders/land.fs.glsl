#include glsl3_fs.h.glsl

varying vec4 v_color;

varying vec2 v_texCoords0;
varying vec2 v_texCoords1;
varying vec2 v_texCoords2;
varying vec2 v_texCoords3;

#ifdef LIGHTING_ENABLED
	varying vec2 v_normalCoords0;
	varying vec2 v_normalCoords1;
	varying vec2 v_normalCoords2;
	varying vec2 v_normalCoords3;

	varying vec2 v_preshadedCoords0;
	varying vec2 v_preshadedCoords1;
	varying vec2 v_preshadedCoords2;
	varying vec2 v_preshadedCoords3;
#endif

varying vec2 v_biomeMapCoords;

uniform sampler2D u_texture;
uniform sampler2D u_biomeMask0;
uniform sampler2D u_biomeMask1;
uniform sampler2D u_biomeMask2;

uniform int u_enabled0;
uniform int u_enabled1;
uniform int u_enabled2;
uniform int u_enabled3;

#include lighting.h.glsl

void main()
{
    float a0 = texture2D(u_biomeMask0, v_biomeMapCoords).a * u_enabled0;
	vec4 color0 = texture2D(u_texture, v_texCoords0);

	float a1 = texture2D(u_biomeMask1, v_biomeMapCoords).a * u_enabled1;
	vec4 color1 = texture2D(u_texture, v_texCoords1);

	float a2 = texture2D(u_biomeMask2, v_biomeMapCoords).a * u_enabled2;
	vec4 color2 = texture2D(u_texture, v_texCoords2);

	float a3 = (1.0 - a0 - a1 - a2) * u_enabled3;
	vec4 color3 = texture2D(u_texture, v_texCoords3);

	#ifdef LIGHTING_ENABLED
		vec4 lightWithSun = getLightLevelWithSun(v_normalCoords0);

		vec3 light0 = lightWithSun.rgb;
		vec3 light1 = getLightLevel(v_normalCoords1);
		vec3 light2 = getLightLevel(v_normalCoords2);
		vec3 light3 = getLightLevel(v_normalCoords3);

		float sun = lightWithSun.a;

		if(v_preshadedCoords.x > 1.5)
		{
			color0 *= vec4(light0, 1.0);
			color1 *= vec4(light1, 1.0);
			color2 *= vec4(light2, 1.0);
			color3 *= vec4(light3, 1.0);
		}
		else
		{
			vec4 preshaded0 = texture2D(u_preshaded, v_preshadedCoords0);
			vec4 preshaded1 = texture2D(u_preshaded, v_preshadedCoords1);
			vec4 preshaded2 = texture2D(u_preshaded, v_preshadedCoords2);
			vec4 preshaded3 = texture2D(u_preshaded, v_preshadedCoords3);

			color0 = sun * preshaded0 + (1.0 - sun) * color0 * vec4(light0, 1.0);
			color1 = sun * preshaded1 + (1.0 - sun) * color1 * vec4(light1, 1.0);
			color2 = sun * preshaded2 + (1.0 - sun) * color2 * vec4(light2, 1.0);
			color3 = sun * preshaded3 + (1.0 - sun) * color3 * vec4(light3, 1.0);
		}
	#endif

	vec4 color = color0 * a0 +
					color1 * a1 +
					color2 * a2 +
					color3 * a3;

	gl_FragColor = color * v_color;
}