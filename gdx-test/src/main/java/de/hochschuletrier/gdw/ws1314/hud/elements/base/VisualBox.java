package de.hochschuletrier.gdw.ws1314.hud.elements.base;

import com.badlogic.gdx.graphics.Texture;

public class VisualBox extends VisualElement {
	protected Texture texture;
	protected float width;
	protected float height;
	
	public VisualBox(Texture texture, float positionX, float positionY, float width, float height) {
		super(positionX, positionY);
		this.width = width;
		this.height = height;
		this.texture = texture;
	}

	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void draw() {
		HudRendering.drawElement(texture, positionX, positionY, width, height);
	}

}
