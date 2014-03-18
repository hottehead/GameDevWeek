package de.hochschuletrier.gdw.commons.gdx.assets;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * 
 * @author Santo Pfingsten
 */
public class FontX {
	private final BitmapFont bitmapFont;

	public FontX(BitmapFont font) {
		assert (font != null);
		this.bitmapFont = font;
	}

	public BitmapFont.TextBounds getBounds(String text) {
		return bitmapFont.getBounds(text);
	}

	public BitmapFont.TextBounds getMultiLineBounds(String text) {
		return bitmapFont.getMultiLineBounds(text);
	}

	public float getWidth(String text) {
		return bitmapFont.getBounds(text).width;
	}

	public float getMultiLineWidth(String text) {
		return bitmapFont.getMultiLineBounds(text).width;
	}

	public float getLineHeight() {
		return bitmapFont.getLineHeight();
	}

	public void setColor(Color color) {
		bitmapFont.setColor(color);
	}

	public void draw(String text, float x, float y) {
		bitmapFont.draw(DrawUtil.batch, text, x, y);
	}

	public void drawRight(String text, float x, float y) {
		BitmapFont.TextBounds bounds = bitmapFont.getBounds(text);
		bitmapFont.draw(DrawUtil.batch, text, x - bounds.width, y);
	}

	public void drawCentered(String text, float x, float y) {
		BitmapFont.TextBounds bounds = bitmapFont.getBounds(text);
		bitmapFont.draw(DrawUtil.batch, text, x - bounds.width / 2, y);
	}

	public void drawMultiLine(String text, float x, float y) {
		bitmapFont.drawMultiLine(DrawUtil.batch, text, x, y);
	}
}
