package de.hochschuletrier.gdw.ws1314.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * 
 * @author MikO
 *
 */
public class GamePreferences {	
	private Preferences settingsHandler;
	
	private static GamePreferences gamePreferences;
	private static String Filename = "settings.brwl";
	
	public GamePreferences() {}
	
	public void init() {
		this.settingsHandler = Gdx.app.getPreferences(GamePreferences.Filename);
	}
	
	public static GamePreferences getInstance()	{
		if(GamePreferences.gamePreferences == null){
			GamePreferences.gamePreferences = new GamePreferences();
		}
		
		return 	GamePreferences.gamePreferences;
	}
	
	public void putBool(String key, boolean val) {
		this.settingsHandler.putBoolean(key, val);
		if (this.getBool(key) != val) 
			throw new GdxRuntimeException("saving boolean failed");
		else
			this.settingsHandler.flush();
	}
	
	public boolean getBool(String key) {
		return this.settingsHandler.getBoolean(key);
	}
	
	public void putInt(String key, int val) {
		this.settingsHandler.putInteger(key, val);
		if (this.getInt(key) != val)
			throw new GdxRuntimeException("saving integer failed");
		else
			this.settingsHandler.flush();
	}
	
	public int getInt(String key) {
		return this.settingsHandler.getInteger(key);
	}
	
	public void putLong(String key, long val) {
		this.settingsHandler.putLong(key, val);
		if (this.getLong(key) != val)
			throw new GdxRuntimeException("saving long failed");
		else
			this.settingsHandler.flush();
	}
	
	public long getLong(String key) {
		return this.settingsHandler.getLong(key);
	}
	
	public void putFloat(String key, float val) {
		this.settingsHandler.putFloat(key, val);
		if (this.getFloat(key) != val)
			throw new GdxRuntimeException("saving float failed");
		else
			this.settingsHandler.flush();
	}
	
	public float getFloat(String key) {
		return this.settingsHandler.getFloat(key);
	}
	
	public void putString(String key, String val) {
		this.settingsHandler.putString(key, val);
		if (this.getString(key, null) != val)
			throw new GdxRuntimeException("saving string failed");
		else
			this.settingsHandler.flush();
	}
	
	public String getString(String key, String defaultValue) {
		String val = this.settingsHandler.getString(key, defaultValue);
		settingsHandler.putString(key, val);
		return val;
	}
	
	public void saveToFile() {
		this.settingsHandler.flush();
	}
}
