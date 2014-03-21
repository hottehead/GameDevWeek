package de.hochschuletrier.gdw.ws1314.hud;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public abstract class AutoResizeStage extends Stage {
	public AutoResizeStage() {
		HudResizer.provide(this);
	}
	public final void resize(int width, int height) {
		this.getViewport().update(width, height);
	}
	
	public abstract void init(AssetManagerX assetManager);

	public abstract void render();

	public final void dispose() {
		super.dispose();
	}
}
