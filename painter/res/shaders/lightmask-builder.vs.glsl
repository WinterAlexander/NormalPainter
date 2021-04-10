#include glsl3_vs.h.glsl

attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform vec2 u_backmaskScale;
uniform vec2 u_backmaskOffset;

uniform vec2 u_cameraOffset;
uniform vec2 u_cameraSize;

varying vec2 v_backmaskCoords;
varying vec2 v_position;

void main()
{
	v_backmaskCoords = a_texCoord0 * u_backmaskScale + u_backmaskOffset;
	v_backmaskCoords.y = 1.0 - v_backmaskCoords.y;

	v_position = a_texCoord0 * u_cameraSize + u_cameraOffset;

	gl_Position = u_projTrans * a_position;
}