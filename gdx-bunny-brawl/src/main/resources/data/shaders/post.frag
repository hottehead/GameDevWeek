#version 130

uniform sampler2D u_texture;
uniform mat4 u_projTrans;

in vec4 vColor;
in vec2 vTexCoord;

float greyscale(vec4 colorValue) {
	float val=0.0f;
	for(int i=0;i<3;++i) {
		val = val+ colorValue[i] * 0.33f;
	}
	return val;
}

void main() {
	vec2 sampleSize = textureSize(u_texture, 0);
	vec2 abtastRate = vec2(1.0f/sampleSize.x, 1.0f/sampleSize.y);

	vec4 pixelColor = texture2D(u_texture, vTexCoord);
	vec4 nearPixel = texture2D(u_texture, vTexCoord + vec2(abtastRate.x,abtastRate.y));

	gl_FragColor = vec4(pixelColor.xyz, pixelColor.a+nearPixel.a);
	//gl_FragColor = nearPixel;
}