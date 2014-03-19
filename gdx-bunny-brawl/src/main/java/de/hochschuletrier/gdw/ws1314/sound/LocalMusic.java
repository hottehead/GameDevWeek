package de.hochschuletrier.gdw.ws1314.sound;

import com.badlogic.gdx.audio.*;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class LocalMusic {
	private AssetManagerX assetManager;
	private Music musicHandle;
	
	public LocalMusic(AssetManagerX assetManager) {
		this.assetManager = assetManager;
	}
	
	public void play(String title) {
		this.musicHandle = this.assetManager.getMusic(title);
		this.musicHandle.play();
		this.musicHandle.setVolume(0.5f);
		this.musicHandle.setLooping(true);
		
		
	}
	
	public void stop() {
		this.musicHandle.stop();
	}
}