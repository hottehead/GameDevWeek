package de.hochschuletrier.gdw.ws1314.shaders;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class DemoShader extends ShaderProgram {

	int tintLoc;
	Color from = Color.valueOf("d5320d");
	Color to = Color.MAGENTA;
	Color current = from.cpy();

	public DemoShader(FileHandle vertexShader, FileHandle fragmentShader) {
		super(vertexShader, fragmentShader);
		tintLoc = getUniformLocation("u_tint");
	}

	@Override
	public void begin() {

		super.begin();
		setUniformf(tintLoc, current);
	}

	public void setTintAmount(int amount) {

		float t = 0f;
		current.set(from);
		current.lerp(to, t);
	}
}
