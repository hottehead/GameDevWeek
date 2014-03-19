package de.hochschuletrier.gdw.ws1314.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class TextureAdvection extends ShaderProgram {

	
	
	public TextureAdvection(String vertexShader, String fragmentShader) {
		super(Gdx.files.internal(vertexShader), Gdx.files.internal(fragmentShader));
		
		
	}

}
