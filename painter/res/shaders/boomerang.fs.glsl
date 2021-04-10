#include glsl3_fs.h.glsl

varying vec4 v_color;
varying vec3 v_texCoords_h;

uniform sampler2D u_texture;
uniform mat3 u_rotationTrans;

#include lighting.h.glsl

void main()
{
	vec2 texCoords = v_texCoords_h.xy / v_texCoords_h.z; //2D cartesian coordinates from homegeneous coordinates

    // rotate
    vec2 rot_center = vec2(0.5, 0.5);
    texCoords = (vec3(texCoords - rot_center, 0.0) * u_rotationTrans).xy + rot_center;

	texCoords.x *= 1.2;
	texCoords.x -= 0.1;

	// manual clamp to border
	if(texCoords.x < 0.0
	|| texCoords.x > 1.0
	|| texCoords.y < 0.0
	|| texCoords.y > 1.0)
		discard;

	gl_FragColor = sunPreshaded(u_texture, texCoords) * v_color;
}