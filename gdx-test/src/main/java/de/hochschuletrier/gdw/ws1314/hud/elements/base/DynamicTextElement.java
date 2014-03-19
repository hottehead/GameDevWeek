package de.hochschuletrier.gdw.ws1314.hud.elements.base;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

public class DynamicTextElement extends StaticTextElement {

	MinMaxValue watchedValue;
	StringBuffer textBuffer;
	private int decimalPlace = 2;
	
	public DynamicTextElement(BitmapFont font, String text, float positionX,
			float positionY, MinMaxValue watchedValue) {
		super(font, text, positionX, positionY);
		this.watchedValue = watchedValue;
		textBuffer = new StringBuffer(8); // 8 zeichen buffer
	}
	
	public void draw() {
		textBuffer.setLength(0);
		
		if (decimalPlace != 0) {
			float temp = 1;
			for (int i=0; i < decimalPlace; ++i) {
				temp *= 10;
			}
			textBuffer.append(text);
			textBuffer.append(Math.floor(watchedValue.getValue()*temp)/temp);
		}
		else {
			textBuffer.append((int)watchedValue.getValue());
		}
			
		offsetX = font.getBounds(textBuffer).width * 0.5f;
		this.font.draw(DrawUtil.batch, textBuffer, positionX - offsetX, positionY);
	}
	
	public void setDecimalPLace(int n) {
		this.decimalPlace = n <= 3 ? n : 2;
		this.decimalPlace = n >= 0 ? this.decimalPlace : 2;
	}
}
