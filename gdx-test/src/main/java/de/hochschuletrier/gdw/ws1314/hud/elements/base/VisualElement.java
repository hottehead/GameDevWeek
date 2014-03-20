package de.hochschuletrier.gdw.ws1314.hud.elements.base;

public abstract class VisualElement {
	protected float positionX; 
	protected float positionY;

	public VisualElement(float positionX, float positionY) {
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	public abstract void draw();
}
