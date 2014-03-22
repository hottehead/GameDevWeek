package de.hochschuletrier.gdw.ws1314.hud;

import com.badlogic.gdx.scenes.scene2d.Stage;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public abstract class AutoResizeStage extends Stage {
	
	protected int lastWidth =0;
	protected int lastHeight=0;
	protected float xScale = 0;
	protected float yScale = 0;
	
	public AutoResizeStage() {
		HudResizer.provide(this);
	}
	
	public void resize(int width, int height) {
		xScale = lastWidth/width;
		yScale = lastWidth/height;
		this.getViewport().update(width, height, true);
		lastWidth = width;
		lastHeight= height;
	}
	
	public abstract void init(AssetManagerX assetManager);

	public abstract void render();

	public final void dispose() {
		super.dispose();
	}
}
