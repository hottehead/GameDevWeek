package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.graphics.Texture;

public class BarBackgroundDecoration extends BarDecorator {

	public BarBackgroundDecoration(VisualBar valueBar, Texture decoration) {
		super(valueBar, new StaticVisualBar(decoration, valueBar.positionX,
				valueBar.positionY, valueBar.width, valueBar.height));
	}

	@Override
	public void draw() {
		super.drawDecoration();
		super.drawBar();
	}

}
