package de.hochschuletrier.gdw.ws1314.hud.elements.base;

import com.badlogic.gdx.graphics.Texture;

public class VisualBar extends VisualBox {

	final MinMaxValue watchedValue;

	public VisualBar(Texture tex, float positionX, float positionY,
			float width, float height, MinMaxValue watchedValue) {
		super(tex, positionX, positionY, width, height);
		this.watchedValue = watchedValue;
	}
	
	public void draw() {
		HudRendering.drawElement(texture, positionX, positionY,
				this.watchedValue.getValueFactor() * width, height);
	}
}
