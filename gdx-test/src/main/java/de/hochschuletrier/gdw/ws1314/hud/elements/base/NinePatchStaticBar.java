package de.hochschuletrier.gdw.ws1314.hud.elements.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;

public class NinePatchStaticBar extends VisualElement {
	float width, height;
	NinePatch ninePatch;

	public NinePatchStaticBar(Texture tex, float positionX, float positionY,
			float width, float height, NinePatchSettings ninePatchSettings) {
		super(positionX, positionY);
		this.ninePatch = new NinePatch(tex, ninePatchSettings.leftBorder,
				ninePatchSettings.rightBorder, ninePatchSettings.topBorder,
				ninePatchSettings.bottomBorder);
		this.height = height;
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void draw() {
		HudRendering.drawNinePatch(ninePatch, positionX, positionY, width,
				height);
	}
}
