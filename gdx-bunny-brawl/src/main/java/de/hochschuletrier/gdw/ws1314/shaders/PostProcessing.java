package de.hochschuletrier.gdw.ws1314.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class PostProcessing extends ShaderProgram {
	
	public PostProcessing(String vertexShader, String fragmentShader) {
		super(Gdx.files.internal(vertexShader), Gdx.files.internal(fragmentShader));
	}
	
	@Override
	public void begin() {
		super.begin();
		this.setupUniforms();
	}

	protected abstract void setupUniforms();

}
