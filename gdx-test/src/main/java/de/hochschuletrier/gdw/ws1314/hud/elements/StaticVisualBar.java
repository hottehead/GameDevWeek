package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.graphics.Texture;

public class StaticVisualBar extends VisualElement {
	protected Texture tex;
	float width, height;

	public StaticVisualBar(Texture tex, float positionX, float positionY,
			float width, float height) {
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
		HudRendering.drawElement(tex, positionX, positionY, width, height);
	}
}
