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
		p.x = MathUtils.clamp(p.x, x, x+width - Gdx.graphics.getWidth());
		p.y = MathUtils.clamp(p.y, y, y+height- Gdx.graphics.getHeight());
	}
	
	public void boundVector(Vector3 p) {
		p.x = MathUtils.clamp(p.x, x, x+width - Gdx.graphics.getWidth());
		p.y = MathUtils.clamp(p.y, y, y+height- Gdx.graphics.getHeight());
	}
}
