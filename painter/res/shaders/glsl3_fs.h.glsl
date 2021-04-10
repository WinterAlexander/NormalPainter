#ifdef GLSL3
	out vec4 out_FragColor;

	#define varying in
	#define texture2D texture
	#define gl_FragColor out_FragColor
#endif