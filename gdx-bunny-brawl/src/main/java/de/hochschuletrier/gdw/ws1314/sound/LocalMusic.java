package de.hochschuletrier.gdw.ws1314.sound;

import com.badlogic.gdx.audio.*;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class LocalMusic {
	private AssetManagerX assetManager;
	private Music musicHandle;
	
	private static float SystemVolume = 0.9f;
	
	// System Volume
	public static void setSystemVolume(float systemVolume) {
		LocalMusic.SystemVolume = systemVolume;
	}
	
	public static float getSystemVolume() {
		return LocalMusic.SystemVolume;
	}
	// END System Volume
	
	public LocalMusic(AssetManagerX assetManager) {
		this.assetManager = assetManager;
	}
	
	public void play(String title) {
		this.musicHandle = this.assetManager.getMusic(title);
		this.musicHandle.play();
		this.musicHandle.setVolume(LocalMusic.SystemVolume);
		this.musicHandle.setLooping(true);
		
		
	}
	
	public void stop() {
		this.musicHandle.stop();
	}
}