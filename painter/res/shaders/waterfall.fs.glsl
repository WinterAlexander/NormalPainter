#include glsl3_fs.h.glsl

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture; //mask

uniform sampler2D u_waterTexture;
uniform vec2 u_texPos;
uniform vec4 u_uv;

uniform float u_offset;

#include lighting.h.glsl

void main()
{
	vec2 texPos = (v_texCoords - u_uv.xy) / u_uv.zw / 8.0
					+ vec2(u_texPos.x, 1.0 - u_texPos.y - u_offset);

    vec4 color = texture2D(u_waterTexture, texPos);
    color.a *= texture2D(u_texture, v_texCoords).a;

	gl_FragColor = vec4(getLightLevel() * color.rgb, color.a) * v_color;
}