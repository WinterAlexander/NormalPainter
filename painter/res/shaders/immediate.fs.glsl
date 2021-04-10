#include glsl3_fs.h.glsl

varying vec4 v_col;

void main()
{
	gl_FragColor = v_col;
}