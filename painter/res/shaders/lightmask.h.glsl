
#ifdef LIGHTING_ENABLED
	attribute vec4 a_lightCoords;
	attribute vec2 a_normalRotation;

	uniform vec2 u_lightMaskOffset;
	uniform vec2 u_lightMaskScale;

	varying vec2 v_normalCoords; // x component as > 1f means no normal, y component as > 1f means no light mask
	varying vec2 v_preshadedCoords;
	varying vec2 v_maskCoords;
	varying vec2 v_normalRot;
	varying vec2 v_inverted;
	varying vec2 v_lightMode;
#endif

void setupLightMask()
{
	#ifdef LIGHTING_ENABLED
		vec2 localCoords = (gl_Position.xy / gl_Position.w + 1.0) / 2.0;
		v_maskCoords = localCoords * u_lightMaskScale + u_lightMaskOffset;
		v_maskCoords.y = 1.0 - v_maskCoords.y;
		v_normalCoords = abs(a_lightCoords.xy);
		v_preshadedCoords = abs(a_lightCoords.zw);
		v_normalRot = a_normalRotation;
		v_inverted = sign(1.0 / a_lightCoords.xy);
		v_lightMode = (-sign(1.0 / a_lightCoords.zw) + 1.0) / 2.0;
	#endif
}