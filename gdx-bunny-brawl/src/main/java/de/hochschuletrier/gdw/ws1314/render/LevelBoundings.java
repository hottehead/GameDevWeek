package de.hochschuletrier.gdw.ws1314.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class LevelBoundings {
	float x, y, width, height;

	public LevelBoundings(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void boundVector(Vector2 p) {
		int gdxWidth = Gdx.graphics.getWidth();
		int gdxHeight = Gdx.graphics.getHeight();
		p.x = MathUtils.clamp(p.x, gdxWidth*0.5f, gdxWidth*0.5f+width - gdxWidth);
		p.y = MathUtils.clamp(p.y, gdxHeight*0.5f, gdxHeight*0.5f+height- gdxHeight);
	}
	
	public void boundVector(Vector3 p) {
		int gdxWidth = Gdx.graphics.getWidth();
		int gdxHeight = Gdx.graphics.getHeight();
		p.x = MathUtils.clamp(p.x, gdxWidth*0.5f, gdxWidth*0.5f+width - gdxWidth);
		p.y = MathUtils.clamp(p.y, gdxHeight*0.5f, gdxHeight*0.5f+height- gdxHeight);
	}
}
