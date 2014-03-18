package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.graphics.Texture;


public class BarFrontDecorator extends BoxDecorator {

	public BarFrontDecorator(VisualBox valueBar, Texture texture) {
		super(valueBar, new VisualBox(texture, valueBar.positionX,
				valueBar.positionY, valueBar.width, valueBar.height));
	}
	
	public BarFrontDecorator(VisualBox valueBar, Texture texture, NinePatchSettings ninePatchSettings) {
		super(valueBar, new NinePatchStaticBar(texture, valueBar.positionX,
				valueBar.positionY, valueBar.width, valueBar.height, ninePatchSettings));
	}

	@Override
	public void draw() {
		super.drawBar();
		super.drawDecoration();
	}

	
}
