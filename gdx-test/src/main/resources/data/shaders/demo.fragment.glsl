#ifdef GL_ES
#define LOWP lowp
	precision mediump float;
#else
	#define LOWP 
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform vec4 u_tint;

void main()
{
	vec4 tex = texture2D(u_texture, v_texCoords);
	vec4 inv = vec4(1.0,1.0,1.0,1.0) - tex;
	inv.a =tex.a;  
	gl_FragColor = v_color  * inv* u_tint;
}