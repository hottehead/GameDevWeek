uniform sampler2D u_prevStep;
uniform sampler2D u_texture;
uniform float u_fadeInFactor;
uniform mat4 u_projTrans;

in vec4 vColor;
in vec2 vTexCoord;

float greyscale(vec4 colorValue) {
	float val=0.0f;
	for(int i=0;i<4;++i) {
		val = val+ colorValue[i] * 0.25f;
	}
	return val;
}

void main() {
	vec2 sampleSize = textureSize(u_texture, 0);
	vec2 abtastRate = vec2(1.0f/sampleSize.x, 1.0f/sampleSize.y);


	vec4 currentFrame = texture2D(u_texture, vTexCoord);
	vec4 prevFrame = texture2D(u_prevStep, vTexCoord - vec2(abtastRate.x,0));



	gl_FragColor = (currentFrame) + (u_fadeInFactor*prevFrame);
}