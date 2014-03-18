package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

public class VisualBar extends VisualElement {

	protected Texture tex;
	float width, height;
	final MinMaxValue watchedValue;

	public VisualBar(Texture tex, float positionX, float positionY,
			float width, float height, MinMaxValue watchedValue) {
		this.watchedValue = watchedValue;
		this.tex = tex;
		super.positionX = positionX;
		super.positionY = positionY;
		this.height = height;
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void draw() {
		DrawUtil.batch.draw(tex, this.positionX, this.positionY + this.height,
				this.watchedValue.getValueFactor() * width, -this.height);
	}
}
