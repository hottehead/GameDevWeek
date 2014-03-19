package de.hochschuletrier.gdw.ws1314.shaders;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class DoubleBufferFBO {
	int renderToIdx;
	FrameBuffer frameBuffer[];
	TextureRegion textureRegions[];
	
	DoubleBufferFBO(Format format, int width, int height, boolean hasDepth) {
		frameBuffer = new FrameBuffer[2];
		frameBuffer[0] = new FrameBuffer(format, width, height, hasDepth);
		frameBuffer[1] = new FrameBuffer(format, width, height, hasDepth);
		renderToIdx = 1;
		
		textureRegions = new TextureRegion[2];
		textureRegions[0] = new TextureRegion(frameBuffer[0].getColorBufferTexture());
		textureRegions[1] = new TextureRegion(frameBuffer[1].getColorBufferTexture());
	}
	
	public void begin() {
		frameBuffer[renderToIdx].begin();
	}
	
	public void swap() {
		renderToIdx = 1 - renderToIdx;
	}
	
	
}
