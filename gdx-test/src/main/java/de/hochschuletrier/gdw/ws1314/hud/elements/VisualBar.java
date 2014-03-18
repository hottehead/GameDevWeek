package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

public class VisualBar extends MinMaxValue {

	float posX, posY;
	Texture tex;
	float width, height;
	
	
	
	public VisualBar(Texture tex, float positionX, float positionY, float width, float height, float minValue,
			float maxValue, float stepSize) {
		super(minValue, maxValue, stepSize);
		
		this.tex 	= tex;
		this.posX 	= positionX;
		this.posY 	= positionY;
		this.height = height;
		this.width	= width;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public void draw() {
		DrawUtil.batch.draw(tex, this.posX, this.posY, 0 + this.getValueFactor()*width, this.height);
	}

	public void step(float dt) {
		this.stepValue();
	}
	
}
