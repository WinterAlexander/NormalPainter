#include glsl3_vs.h.glsl

attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

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

uniform vec2 u_biomeMapOffset;
uniform vec2 u_biomeMapScale;

uniform vec2 u_biomeOffset0_f;
uniform vec2 u_biomeOffset1_f;
uniform vec2 u_biomeOffset2_f;
uniform vec2 u_biomeOffset3_f;

#ifdef LIGHTING_ENABLED
    uniform vec2 u_biomeOffset0_n;
    uniform vec2 u_biomeOffset1_n;
    uniform vec2 u_biomeOffset2_n;
    uniform vec2 u_biomeOffset3_n;

    uniform vec2 u_biomeOffset0_p;
    uniform vec2 u_biomeOffset1_p;
    uniform vec2 u_biomeOffset2_p;
    uniform vec2 u_biomeOffset3_p;
#endif

#include lightmask.h.glsl

void main()
{
    v_color = a_color;
    v_texCoords0 = a_texCoord0 + u_biomeOffset0_f;
    v_texCoords1 = a_texCoord0 + u_biomeOffset1_f;
    v_texCoords2 = a_texCoord0 + u_biomeOffset2_f;
    v_texCoords3 = a_texCoord0 + u_biomeOffset3_f;

    gl_Position = u_projTrans * a_position;

    v_biomeMapCoords = (gl_Position.xy / gl_Position.w + 1.0) / 2.0 * u_biomeMapScale + u_biomeMapOffset;
    v_biomeMapCoords.y = 1.0 - v_biomeMapCoords.y;

    setupLightMask();

    #ifdef LIGHTING_ENABLED
        v_normalCoords0 = v_normalCoords + u_biomeOffset0_n;
        v_normalCoords1 = v_normalCoords + u_biomeOffset1_n;
        v_normalCoords2 = v_normalCoords + u_biomeOffset2_n;
        v_normalCoords3 = v_normalCoords + u_biomeOffset3_n;

        v_preshadedCoords0 = v_preshadedCoords + u_biomeOffset0_p;
        v_preshadedCoords1 = v_preshadedCoords + u_biomeOffset1_p;
        v_preshadedCoords2 = v_preshadedCoords + u_biomeOffset2_p;
        v_preshadedCoords3 = v_preshadedCoords + u_biomeOffset3_p;
    #endif
}