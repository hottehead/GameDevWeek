package de.hochschuletrier.gdw.ws1314.hud.elements.base;

import com.badlogic.gdx.graphics.Texture;

public class BoxBackgroundDecoration extends BoxDecorator {

	public BoxBackgroundDecoration(VisualBar valueBar, Texture decoration) {
		super(valueBar, new VisualBox(decoration, valueBar.positionX,
				valueBar.positionY, valueBar.width, valueBar.height));
	}

	@Override
	public void draw() {
		super.drawDecoration();
		super.drawBar();
	}

}
