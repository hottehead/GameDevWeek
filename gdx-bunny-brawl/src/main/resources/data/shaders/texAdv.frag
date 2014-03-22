#version 130

uniform sampler2D u_texture;
uniform mat4 u_projTrans;

in vec4 vColor;
in vec2 vTexCoord;

void main() {
	vec4 currentFrame = texture2D(u_texture, vTexCoord);

	gl_FragData[0] = currentFrame;
}