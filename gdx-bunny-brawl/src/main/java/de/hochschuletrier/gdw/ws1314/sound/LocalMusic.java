package de.hochschuletrier.gdw.ws1314.sound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.preferences.PreferenceKeys;
import de.hochschuletrier.gdw.ws1314.states.MainMenuState;

/**
 * Class for handling the music in GameplayState
 * should be initialized when needed
 * 
 * @author MikO
 */
public class LocalMusic {
	public static final Logger logger = LoggerFactory.getLogger(LocalMusic.class);
	
	private AssetManagerX assetManager;
	private Music musicHandle;
	private boolean fading;
	private char fadingDirection;
	private int duration;
	
	// FIXME (if music's not playing as should)
	private static float SystemVolume = Main.getInstance().gamePreferences.getFloat(PreferenceKeys.volumeMusic, 0.9f);
	/**
	 * Change the general volume for music
	 * The volume of all music will be a percentage of this systemVolume
	 * 
	 * @param systemVolume
	 */
	public static void setSystemVolume(float systemVolume) {
		LocalMusic.SystemVolume = systemVolume;
		Main.getInstance().gamePreferences.putFloat(PreferenceKeys.volumeMusic, systemVolume);
	}
	
	/**
	 * Get the current set general volume for music
	 * @return
	 */
	public static float getSystemVolume() {
		return LocalMusic.SystemVolume;
	}
	
	public void setFade(char fadingDirection, int duration) {
		this.duration = duration;
		this.fading = this.fading == true ? false : true;
		this.fadingDirection = fadingDirection;
	}

	/**
	* Constructor of the class LocalMusic
	* 	
	* @param assetManager
	*/
	public LocalMusic(AssetManagerX assetManager) {
		this.assetManager = assetManager;
		this.musicHandle = null;
	}
	
	public void setVolume(float volume) {
		this.musicHandle.setVolume(volume * LocalMusic.SystemVolume);
	}
	
	public char getFadingDirection() { return this.fadingDirection; }
	public boolean getFading() { return this.fading; }

	
	public void update() {
		float delta = Gdx.graphics.getDeltaTime();
		float step = delta * (1000.0f / this.duration);
		
		if (this.fading && this.musicHandle != null) {
			float volume = this.musicHandle.getVolume();
			
			if (this.fadingDirection == 'i') {
				volume += step;
			}
			else if (this.fadingDirection == 'o') {
				volume -= step;
				volume = volume < delta * (1000.0f / this.duration) ? 0.0f : volume;
				this.fading = volume == 0.0f ? false : true;
			}
		
		this.musicHandle.setVolume(volume);
		volume = volume > LocalMusic.SystemVolume ? LocalMusic.SystemVolume : volume;
		}
	}
	
	/**
	 * Plays the music with the given title
	 * 
	 * @param title
	 */
	public void play(String title) {
		this.musicHandle = this.assetManager.getMusic(title);
		this.musicHandle.play();
		this.musicHandle.setVolume(LocalMusic.SystemVolume);
		this.musicHandle.setLooping(true);
	}
	
	/**
	 * Stops actual playing music
	 */
	public void stop() {
		this.musicHandle.stop();
	}
	
	/**
	 * pauses actual playing music
	 */
	public void pause() {
		this.musicHandle.pause();
	}
	
	/**
	 * Sets the volume of the actual playing track to zero
	 */
	public void mute() {
		this.musicHandle.setVolume(0.0f);
	}
	
	/**
	 * Turns volume of the actual playing track back on
	 */
	public void deMute() {
		this.musicHandle.setVolume(LocalMusic.SystemVolume);
	}
	
	/**
	 * returns true if music ist playing and false if not
	 * 
	 * @return isMusicPlaying
	 */
	public boolean isMusicPlaying() {
		if (this.musicHandle != null)
			return this.musicHandle.isPlaying();
		else 
			return false;
	}
}