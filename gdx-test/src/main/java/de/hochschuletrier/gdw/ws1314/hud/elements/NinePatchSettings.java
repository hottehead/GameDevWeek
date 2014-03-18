package de.hochschuletrier.gdw.ws1314.hud.elements;

public class NinePatchSettings {
	int leftBorder, rightBorder, topBorder, bottomBorder;
	
	public NinePatchSettings() {
		this(0,0,0,0);
	}
	
	public NinePatchSettings(int leftBorder, int rightBorder, int topBorder, int bottomBorder) {
		this.leftBorder = leftBorder;
		this.rightBorder = rightBorder;
		this.topBorder = topBorder;
		this.bottomBorder = bottomBorder;
	}
}
