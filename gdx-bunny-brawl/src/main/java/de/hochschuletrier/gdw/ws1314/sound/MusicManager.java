package de.hochschuletrier.gdw.ws1314.sound;

import java.util.EnumMap;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

public class MusicManager {
	private static MusicManager Instance;
	private EnumMap<GameStates, LocalMusic> musicStreamsList;
	private AssetManagerX assetManager;

	
	public static MusicManager getInstance() {
		if (MusicManager.Instance == null)
			MusicManager.Instance = new MusicManager();
		return MusicManager.Instance;
	}
	
	public MusicManager() {}
	
	public void init(AssetManagerX assetManager) {
		this.musicStreamsList = new EnumMap<GameStates, LocalMusic> (GameStates.class);
		this.assetManager = assetManager;
		this.fillStreamList();
	}
	
	private void fillStreamList() {
		for (GameStates state : GameStates.values()) {
			this.musicStreamsList.put(state, new LocalMusic(this.assetManager));
		}
	}
	
	public LocalMusic getMusicStreamByStateName(GameStates stateName) {
		if (this.musicStreamsList.containsKey(stateName)) 
			return this.musicStreamsList.get(stateName);
		else
			return null;
	}
	
	public void stopAllStreams() {
		for (GameStates state : GameStates.values()) {
			if (this.musicStreamsList.get(state).isMusicPlaying())
				this.musicStreamsList.get(state).stop();
		}		
	}
}
