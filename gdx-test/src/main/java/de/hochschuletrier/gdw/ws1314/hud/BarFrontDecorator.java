package de.hochschuletrier.gdw.ws1314.hud;

import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.ws1314.hud.elements.BarDecorator;
import de.hochschuletrier.gdw.ws1314.hud.elements.VisualBar;

public class BarFrontDecorator extends BarDecorator {

	public BarFrontDecorator(VisualBar valueBar, Texture decoration) {
		super(valueBar, decoration);
	}

	@Override
	public void draw() {
		super.drawBar();
		super.drawDecoration();
	}

	
}
