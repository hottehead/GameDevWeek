package de.hochschuletrier.gdw.ws1314.hud.elements;


public class BarTextDecorator extends BarDecorator {

	public BarTextDecorator(VisualBar decoratedBar, VisualElement decoration) {
		super(decoratedBar, decoration);
		decoration.positionX = decoration.positionX + decoratedBar.positionX;
		decoration.positionY = decoration.positionY + decoratedBar.positionY;
	}

	@Override
	public void draw() {
		super.drawBar();
		super.drawDecoration();
	}

	
	
}
