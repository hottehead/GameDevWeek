package de.hochschuletrier.gdw.ws1314.hud.elements;


public abstract class BoxDecorator extends VisualBox {

	VisualBox decoratedBar;
	VisualElement decoration;

	public BoxDecorator(VisualBox decoratedBox, VisualElement decoration) {
		super(decoratedBox.texture, decoratedBox.positionX,
				decoratedBox.positionY, decoratedBox.width,
				decoratedBox.height);
		this.decoratedBar = decoratedBox;
		this.decoration = decoration;
	}

	protected void drawBar() {
		decoratedBar.draw();
	}

	protected void drawDecoration() {
		decoration.draw();
	}

	public abstract void draw();

}
