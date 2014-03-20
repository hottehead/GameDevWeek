#version 330

uniform sampler2D u_texture;
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

vec4 gaussianBlur3x3(vec2 texCoord) {
	vec2 samplerSize = textureSize(u_texture, 0);
	vec2 d = vec2(1.0/samplerSize.x, 1.0/samplerSize.y);

	vec2 offsetFetch[9] = vec2[9](
		vec2(-d.x,-d.y), vec2(0,-d.y), vec2(+d.x,-d.y),
		vec2(-d.x,0), vec2(0,0), vec2(+d.x,0),
		vec2(-d.x,+d.y), vec2(0,+d.y), vec2(+d.x,+d.y)
	);

	float kernel[9] = float[9](
		1,2,1,
		2,4,2,
		1,2,1
	);

	float kernelFactor = 1.0f/16.0f;
	vec4 endResult = vec4(0);
	for(int i=0;i<9;++i) {
		vec4 pixelValue = texture2D(u_texture, texCoord + offsetFetch[i]);
		endResult.xyz += pixelValue.xyz * (kernel[i]);
	}
	endResult.a = texture2D(u_texture, texCoord).a;
	endResult.xyz = endResult.xyz * kernelFactor;
	return endResult;
}

vec4 gaussianBlur5x5(vec2 texCoord) {
	vec2 samplerSize = textureSize(u_texture, 0);
	vec2 d = vec2(1.0/samplerSize.x, 1.0/samplerSize.y);

	vec2 offsetFetch[25] = vec2[25](
		vec2(-d.x*2,-d.y*2), vec2(-d.x,-d.y*2), vec2(0,-d.y*2), vec2(+d.x,-d.y*2), vec2(+d.x*2,-d.y*2),
		vec2(-d.x*2,-d.y), vec2(-d.x,-d.y), vec2(0,-d.y), vec2(+d.x,-d.y), vec2(+d.x*2,-d.y),
		vec2(-d.x*2,0), vec2(-d.x,0), vec2(0,0), vec2(+d.x,0), vec2(+d.x*2,0),
		vec2(-d.x*2,+d.y), vec2(-d.x,+d.y), vec2(0,+d.y), vec2(+d.x,+d.y), vec2(+d.x*2,+d.y),
		vec2(-d.x*2,+d.y*2), vec2(-d.x,+d.y*2), vec2(0,+d.y*2), vec2(+d.x,+d.y*2), vec2(+d.x*2,+d.y*2)
	);

	float kernel[25] = float[25](
		2,4,5,4,2,
		4,9,12,9,4,
		5,12,15,12,5,
		4,9,12,9,4,
		2,4,5,4,2
	);

	float kernelFactor = 1.0f/159.0f;
	vec4 endResult = vec4(0);
	for(int i=0;i<25;++i) {
		vec4 pixelValue = texture2D(u_texture, texCoord + offsetFetch[i]);
		endResult.xyz += pixelValue.xyz * (kernel[i]);
	}
	endResult.a = texture2D(u_texture, texCoord).a;
	endResult.xyz = endResult.xyz * kernelFactor;
	return endResult;
}



void main() {
	gl_FragColor = (texture2D(u_texture, vTexCoord)) * vColor;
}