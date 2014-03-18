package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

public abstract class BarDecorator extends VisualBar {

	VisualBar decoratedBar;
	Texture decoration;

	public BarDecorator(VisualBar decoratedBarBar, Texture decoration) {
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
		DrawUtil.batch.draw(this.decoration, this.positionX, this.positionY
				+ this.height, this.width, -this.height);
	}

	public abstract void draw();

}
