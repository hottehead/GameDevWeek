package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.graphics.Texture;

public class BarBackgroundDecoration extends BoxDecorator {

	public BarBackgroundDecoration(VisualBar valueBar, Texture decoration) {
		super(valueBar, new VisualBox(decoration, valueBar.positionX,
				valueBar.positionY, valueBar.width, valueBar.height));
	}

	@Override
	public void draw() {
		super.drawDecoration();
		super.drawBar();
	}

}
