#include glsl3_fs.h.glsl

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

uniform sampler2D u_mask;
uniform vec2 u_maskStart;
uniform vec2 u_maskSize;
uniform vec4 u_maskUV;

#include lighting.h.glsl

void main()
{
	vec2 maskCoords = (gl_FragCoord.xy - u_maskStart) / u_maskSize;

	maskCoords.y = 1.0 - maskCoords.y;

	if(maskCoords.x < 0.0 || maskCoords.x > 1.0 || maskCoords.y < 0.0 || maskCoords.y > 1.0)
		discard;

	vec2 uvSize = u_maskUV.zw - u_maskUV.xy;
	vec4 mask_color = texture2D(u_mask, maskCoords * uvSize + u_maskUV.xy);

	gl_FragColor = sunPreshaded(u_texture, v_texCoords) * v_color * mask_color;
}