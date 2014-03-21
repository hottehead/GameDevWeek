package de.hochschuletrier.gdw.ws1314.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Reads preferences from and writes them to file.
 * 
 * @author MikO
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
	
	public void putBool(PreferenceKeys key, boolean val) {
		this.settingsHandler.putBoolean(key.toString(), val);
		if (this.getBool(key) != val) 
			throw new GdxRuntimeException("saving boolean failed");
		else
			this.settingsHandler.flush();
	}
	
	private boolean getBool(PreferenceKeys key) {
		return this.settingsHandler.getBoolean(key.toString());
	}
	
	public boolean getBool(PreferenceKeys key, boolean defValue) {
		return this.settingsHandler.getBoolean(key.toString(), defValue);
	}
	
	public void putInt(PreferenceKeys key, int val) {
		this.settingsHandler.putInteger(key.toString(), val);
		if (this.getInt(key) != val)
			throw new GdxRuntimeException("saving integer failed");
		else
			this.settingsHandler.flush();
	}
	
	private int getInt(PreferenceKeys key) {
		return this.settingsHandler.getInteger(key.toString());
	}
	
	public int getInt(PreferenceKeys key, int defValue) {
		return this.settingsHandler.getInteger(key.toString(), defValue);
	}
	
	public void putLong(PreferenceKeys key, long val) {
		this.settingsHandler.putLong(key.toString(), val);
		if (this.getLong(key) != val)
			throw new GdxRuntimeException("saving long failed");
		else
			this.settingsHandler.flush();
	}
	
	private long getLong(PreferenceKeys key) {
		return this.settingsHandler.getLong(key.toString());
	}
	
	public long getLong(PreferenceKeys key, long defValue) {
		return this.settingsHandler.getLong(key.toString(), defValue);
	}
	
	public void putFloat(PreferenceKeys key, float val) {
		this.settingsHandler.putFloat(key.toString(), val);
		if (this.getFloat(key) != val)
			throw new GdxRuntimeException("saving float failed");
		else
			this.settingsHandler.flush();
	}
	
	private float getFloat(PreferenceKeys key) {
		return this.settingsHandler.getFloat(key.toString());
	}
	
	public float getFloat(PreferenceKeys key, float defValue) {
		return this.settingsHandler.getFloat(key.toString(), defValue);
	}
	
	public void putString(PreferenceKeys key, String val) {
		this.settingsHandler.putString(key.toString(), val);
		if (this.getString(key) != val)
			throw new GdxRuntimeException("saving string failed");
		else
			this.settingsHandler.flush();
	}
	
	private String getString(PreferenceKeys key) {
		String val = this.settingsHandler.getString(key.toString());
		return val;
	}
	
	public String getString(PreferenceKeys key, String defValue) {
		return this.settingsHandler.getString(key.toString(), defValue);
	}
	
	public void saveToFile() {
		this.settingsHandler.flush();
	}
}
