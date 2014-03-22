package de.hochschuletrier.gdw.ws1314.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;

public class FrameBufferFBO implements ScreenListener {
	FrameBuffer frameBuffer;
	TextureRegion textureRegions;
	
	Format format;
	int width, height;
	boolean hasDepth;
	
	public FrameBufferFBO(Format format, int width, int height, boolean hasDepth) {
		this.format = format; this.width = width; this.height = height; this.hasDepth = hasDepth;
		frameBuffer = new FrameBuffer(format, width, height, hasDepth);
		textureRegions = new TextureRegion(frameBuffer.getColorBufferTexture());
	}
	
	public void begin() {
		frameBuffer.begin();
	}
	public void end() {
		frameBuffer.end();
	}
	
	public TextureRegion getActiveFrameBuffer() {
		return textureRegions;
	}
	
	public void bindToTexture(int textureId) {
		GL20 gl = Gdx.graphics.getGL20();
		gl.glBindTexture(textureId, this.frameBuffer.getColorBufferTexture().getTextureObjectHandle());
	}

	@Override
	public void resize(int width, int height) {
		frameBuffer.dispose();
		frameBuffer = new FrameBuffer(format, width, height, hasDepth);
		this.textureRegions = new TextureRegion(frameBuffer.getColorBufferTexture());
	}
	
}
