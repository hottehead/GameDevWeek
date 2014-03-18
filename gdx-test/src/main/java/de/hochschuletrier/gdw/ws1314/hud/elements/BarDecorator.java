package de.hochschuletrier.gdw.ws1314.hud.elements;


public abstract class BarDecorator extends VisualBar {

	VisualBar decoratedBar;
	VisualElement decoration;

	public BarDecorator(VisualBar decoratedBarBar, VisualElement decoration) {
		super(decoratedBarBar.tex, decoratedBarBar.positionX,
				decoratedBarBar.positionY, decoratedBarBar.width,
				decoratedBarBar.height, decoratedBarBar.watchedValue);
		this.decoratedBar = decoratedBarBar;
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
