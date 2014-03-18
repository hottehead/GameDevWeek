package de.hochschuletrier.gdw.commons.gdx.assets;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author Santo Pfingsten
 */
public class ImageX {

	protected final Texture texture;

	public ImageX(Texture texture) {
		assert (texture != null);
		this.texture = texture;
	}

	public ImageX(Color color) {
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fill();
		texture = new Texture(pixmap);
		pixmap.dispose();
	}

	public void draw() {
		DrawUtil.batch.draw(texture, 0, 0, texture.getWidth(), texture.getHeight(), 0, 0,
				texture.getWidth(), texture.getHeight(), false, true);
	}

	public void draw(float x, float y) {
		DrawUtil.batch.draw(texture, x, y, texture.getWidth(), texture.getHeight(), 0, 0,
				texture.getWidth(), texture.getHeight(), false, true);
	}

	public void draw(float x, float y, float width, float height) {
		DrawUtil.batch.draw(texture, x, y, width, height, 0, 0, texture.getWidth(),
				texture.getHeight(), false, true);
	}

	public void draw(float x, float y, float scale) {
		DrawUtil.batch.draw(texture, x, y, texture.getWidth() * scale,
				texture.getHeight() * scale, 0, 0, texture.getWidth(),
				texture.getHeight(), false, true);
	}

	public void draw(float x, float y, int width, int height, int srcX, int srcY) {
		DrawUtil.batch.draw(texture, x, y, width, height, srcX, srcY, width, height,
				false, true);
	}

	public void draw(float x, float y, int width, int height, int srcX, int srcY,
			int srcWidth, int srcHeight) {
		DrawUtil.batch.draw(texture, x, y, width, height, srcX, srcY, srcWidth,
				srcHeight, false, true);
	}
    
	public void draw (float x, float y, int srcX, int srcY, float width, float height,
		float scaleX, float scaleY, float rotation) {
        DrawUtil.batch.draw (texture, x, y, 0, 0, width, height, scaleX,
		scaleY, rotation, srcX, srcY, (int)width, (int)height, false, true);
    }

	public int getWidth() {
		return texture.getWidth();
	}

	public int getHeight() {
		return texture.getHeight();
	}

	public void dispose() {
		texture.dispose();
	}

	public void bind() {
		texture.bind();
	}
}

