package de.hochschuletrier.gdw.ws1314.hud.elements.base;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

public class DynamicTextElement extends StaticTextElement {

	MinMaxValue watchedValue;
	StringBuffer textBuffer;
	
	public DynamicTextElement(BitmapFont font, String text, float positionX,
			float positionY, MinMaxValue watchedValue) {
		super(font, text, positionX, positionY);
		this.watchedValue = watchedValue;
		textBuffer = new StringBuffer(8); // 8 zeichen buffer
	}
	
	@Override
	public void draw() {
		textBuffer.setLength(0);
		
		textBuffer.append(text);
		textBuffer.append(Math.floor(watchedValue.getValue()*100.0f)/100.0f);
		
		offsetX = font.getBounds(textBuffer).width * 0.5f;
		this.font.draw(DrawUtil.batch, textBuffer, positionX - offsetX, positionY);
	}

}
