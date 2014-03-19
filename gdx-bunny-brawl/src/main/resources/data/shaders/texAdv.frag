
uniform sampler2D u_prevStep;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

in vec4 vColor;
in vec2 vTexCoord;



void main() {
	vec4 currentFrame = texture2D(u_texture, vTexCoord);
	vec4 prevFrame = texture2D(u_prevStep, vTexCoord);

	vec2 sampleSize = samplerSize(u_texture);
	vec2 abtastRate = vec2(1.0f/abtastRate.x, 1.0f/abtastRate.y);

	gl_FragColor = (currentFrame) * vColor  + prevFrame * 0.5f;
}