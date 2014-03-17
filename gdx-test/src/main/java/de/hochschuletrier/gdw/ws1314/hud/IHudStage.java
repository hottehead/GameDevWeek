package de.hochschuletrier.gdw.ws1314.hud;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public interface IHudStage {

	public abstract void resize(int width, int height);
	
	public abstract void init(AssetManagerX assetManager);

	public abstract void render();

	public abstract void dispose();

}