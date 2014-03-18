package de.hochschuletrier.gdw.ws1314.hud.elements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

public class StaticTextElement extends VisualElement {

	BitmapFont font;
	CharSequence text;
	float offsetX = 0;
	
	public StaticTextElement(BitmapFont font, String text, float positionX, float positionY) {
		super(positionX, positionY);
		this.font = font;
		setText(text);
	}
	
	@Override
	public void draw() {
		font.draw(DrawUtil.batch, text, positionX - offsetX, positionY);
	}
	
	public void setText(String text) {
		this.text = text;
		offsetX = this.font.getBounds(text).width * 0.5f;
	}

}
