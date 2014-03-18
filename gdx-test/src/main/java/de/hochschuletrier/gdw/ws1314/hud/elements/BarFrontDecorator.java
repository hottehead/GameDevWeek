package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.graphics.Texture;


public class BarFrontDecorator extends BarDecorator {

	public BarFrontDecorator(VisualBar valueBar, Texture decoration) {
		super(valueBar, new StaticVisualBar(decoration, valueBar.positionX,
				valueBar.positionY, valueBar.width, valueBar.height));
	}
	
	public BarFrontDecorator(VisualBar valueBar, Texture decoration, NinePatchSettings ninePatchSettings) {
		super(valueBar, new NinePatchStaticBar(decoration, valueBar.positionX,
				valueBar.positionY, valueBar.width, valueBar.height, ninePatchSettings));
	}

	@Override
	public void draw() {
		super.drawBar();
		super.drawDecoration();
	}

	
}
