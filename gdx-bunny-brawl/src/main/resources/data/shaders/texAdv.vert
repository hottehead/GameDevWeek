#version 130

attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec4 a_color;

uniform sampler2D u_prevStep;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

out vec2 vTexCoord;
out vec4 vColor;
 
void main() {
	vColor = a_color;
	vTexCoord = a_texCoord0;
	gl_Position = u_projTrans * a_position;
}