package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.graphics.Texture;

public class BarBackgroundDecoration extends BarDecorator {

	public BarBackgroundDecoration(VisualBar valueBar, Texture decoration) {
		super(valueBar, decoration);
	}

	@Override
	public void draw() {		
		super.drawDecoration();
		super.drawBar();
	}

}
