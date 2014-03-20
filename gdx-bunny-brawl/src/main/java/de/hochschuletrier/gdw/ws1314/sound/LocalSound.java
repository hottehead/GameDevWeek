package de.hochschuletrier.gdw.ws1314.sound;

import com.badlogic.gdx.audio.*;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.EventType;
import de.hochschuletrier.gdw.ws1314.entity.player.*;
/**
 * 
 * @author MikO
 * 
 * @description does some VERY VERY !IMPORTANT THINGS!!!
 *
 */

public class LocalSound {
	private AssetManagerX assetManager;
	private Sound soundHandle;
	private ClientPlayer localPlayer;
	private long soundID;
	
	private static LocalSound localsound;
	private static float SystemVolume;
	private static float maxDistance = 300;
		
	public static void setSystemVolume(float systemVolume) {
		LocalSound.SystemVolume = systemVolume;
	}
	
	public static float getSystemVolume() {
		return LocalSound.SystemVolume;
	}
	
	public static LocalSound getInstance()	{
		if(localsound == null){
			localsound = new LocalSound();
		}
		
		return localsound;
	}
	
	private LocalSound() {}
	
	public void init(AssetManagerX assetManager) {
		long playerEntityID = ClientEntityManager.getInstance().getPlayerEntityID();	
		this.assetManager = assetManager;
		this.soundHandle = null;
		this.localPlayer = (ClientPlayer) ClientEntityManager.getInstance().getEntityById(playerEntityID);
		this.soundID = 0;		
	}
	
	public void remoteSound(String sound, ClientPlayer remotePlayer) {
		double localX, localY, remoteX, remoteY;
		float volume, distance;
		
		localX = this.localPlayer.getPosition().x;
		localY = this.localPlayer.getPosition().y;
		
		remoteX = remotePlayer.getPosition().x;
		remoteY = remotePlayer.getPosition().y;
		
		//  SQRT( ( x1 - x2 )² + ( y1 - y2 )² )  <<< just pythagoras
		distance = (float) Math.sqrt( Math.pow( (localX - remoteX), 2 ) + Math.pow( (localY - remoteY), 2 ) );
		
		// volume will be [volume]% (percent) of systemVolume
		volume = (100 - (distance * 100 / LocalSound.maxDistance)) / 100;
		
		this.play(sound, volume);
	}
	
	private void play(String sound, float volume) {
		this.soundHandle = this.assetManager.getSound(sound);
		this.soundID = soundHandle.play();
		soundHandle.setVolume(this.soundID, LocalSound.SystemVolume * volume);
	}
	
	private String connectSoundToAction(EventType event) {
		switch (event) {
			case HIT_BY_ATTACK_1:
			case HIT_BY_ATTACK_2:
				return "speech-general-nom_1";
			default:
				return null;	
		}
		
	}
	
	public void playSoundByAction(EventType event, ClientPlayer player) {
		if (player.getID() == this.localPlayer.getID()) {
			this.play(this.connectSoundToAction(event), LocalSound.getSystemVolume());
		}
		else {
			this.remoteSound(this.connectSoundToAction(event), player);
		}
	}

	public void stop() {
		this.soundHandle.stop();
	}
}
