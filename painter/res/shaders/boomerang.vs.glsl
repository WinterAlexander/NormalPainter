#include glsl3_vs.h.glsl

attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform sampler2D u_texture;
uniform mat4 u_projTrans;
uniform mat3 u_perspectiveTrans;

varying vec4 v_color;
varying vec3 v_texCoords_h; // homgeneous texture coordinates

#include lightmask.h.glsl

void main()
{
    const float scale = 500.0; //size used to generate the GIMP matrix

    vec3 scaled_coords = vec3(a_texCoord0 * scale, 1.0) * u_perspectiveTrans;

    v_texCoords_h = vec3(scaled_coords.xy / scale, scaled_coords.z);

    gl_Position = u_projTrans * a_position;
    v_color = a_color;

    setupLightMask();
}