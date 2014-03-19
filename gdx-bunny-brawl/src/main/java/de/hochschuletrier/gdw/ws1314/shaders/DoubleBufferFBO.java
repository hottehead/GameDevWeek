package de.hochschuletrier.gdw.ws1314.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class DoubleBufferFBO {
	int renderToIdx;
	FrameBuffer frameBuffer[];
	TextureRegion textureRegions[];
	boolean isRenderingToTexture;
	
	public DoubleBufferFBO(Format format, int width, int height, boolean hasDepth) {
		frameBuffer = new FrameBuffer[2];
		frameBuffer[0] = new FrameBuffer(format, width, height, hasDepth);
		frameBuffer[1] = new FrameBuffer(format, width, height, hasDepth);
		renderToIdx = 1;
		
		textureRegions = new TextureRegion[2];
		textureRegions[0] = new TextureRegion(frameBuffer[0].getColorBufferTexture());
		textureRegions[1] = new TextureRegion(frameBuffer[1].getColorBufferTexture());
		isRenderingToTexture = false;
	}
	
	public void begin() {
		frameBuffer[renderToIdx].begin();
		isRenderingToTexture = true;
	}
	public void end() {
		frameBuffer[renderToIdx].end();
		isRenderingToTexture = false;
	}
	
	public void swap() {
		if(!isRenderingToTexture) {
			renderToIdx = 1 - renderToIdx;
		}
	}
	
	public TextureRegion getActiveFrameBuffer() {
		return textureRegions[1-renderToIdx];
	}
	
	public void bindOtherBufferTo(int textureId) {
		GL20 gl = Gdx.graphics.getGL20();
		gl.glBindTexture(GL20.GL_TEXTURE1, this.frameBuffer[1-renderToIdx].getColorBufferTexture().getTextureObjectHandle());
	}
	
}
