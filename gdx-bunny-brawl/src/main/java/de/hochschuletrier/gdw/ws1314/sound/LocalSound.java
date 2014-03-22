package de.hochschuletrier.gdw.ws1314.sound;

import com.badlogic.gdx.audio.*;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;
import de.hochschuletrier.gdw.ws1314.entity.player.*;

/**
 * Class for handling sound effects in GameplayState
 * The class is singleton and should be initialized in GameplayState
 * 
 * @author MikO
 */
public class LocalSound {
	private AssetManagerX assetManager;
	private Sound soundHandle;
	private ClientPlayer localPlayer;
	private long soundID;
	
	private static LocalSound localsound;
	private static float SystemVolume = 1.0f;
	private static float maxDistance = 300;
	
	/**
	 * Change the general volume for sounds
	 * The volume of all sounds will be a percentage of this systemVolume
	 * 
	 * @param systemVolume
	 */
	public static void setSystemVolume(float systemVolume) {
		LocalSound.SystemVolume = systemVolume;
	}
	
	/**
	 * Get the current set general volume for sounds
	 * 
	 * @return LocalSound.SystemVolume
	 */
	public static float getSystemVolume() {
		return LocalSound.SystemVolume;
	}
	
	/**
	 * 
	 * @return reference of actual instance of singleton
	 */
	public static LocalSound getInstance()	{
		if(localsound == null){
			localsound = new LocalSound();
		}
		
		return localsound;
	}
	
	private LocalSound() {}
	
	/**
	 * Initializes an instance of LocalSound
	 * 
	 * @param assetManager
	 */
	public void init(AssetManagerX assetManager) {
		long playerEntityID = ClientEntityManager.getInstance().getPlayerEntityID();	
		this.assetManager = assetManager;
		this.localPlayer = (ClientPlayer) ClientEntityManager.getInstance().getEntityById(playerEntityID);
		this.soundID = 0;		
	}
	
	/**
	 * Plays a given sound for a remote player
	 * Calculates the distance between local player and remote player and fits the volume
	 * to the calculated distance.
	 * 
	 * @param sound to play
	 * @param remotePlayer Object
	 */
	private void remoteSound(String sound, ClientEntity remotePlayer) {
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
	
	/**
	 * plays a sound in dependency to the system volume
	 * 	
	 * @param sound
	 * @param volume
	 */
	private void play(String sound, float volume) {
		this.soundHandle = this.assetManager.getSound(sound);
		this.soundID = soundHandle.play();
		soundHandle.setVolume(this.soundID, LocalSound.SystemVolume * volume);
	}
	
	/**
	 * Connects an enum EventType to an existing sound.
	 * 
	 * @param event
	 * @return name of dependend sound or null
	 */
	private String connectSoundToAction(EventType event, ClientEntity entity) { 
		switch (event) {
			case HIT_BY_ATTACK_1:
			case HIT_BY_ATTACK_2:
				if(entity.getEntityType() == EntityType.Tank) {
					int random = this.random(1, 6);
					return "speech-tank-pain_" + random;					
				}
				else if (entity.getEntityType() == EntityType.Hunter || entity.getEntityType() == EntityType.Knight) {
					int random = this.random(1, 6);
					return "speech-general-pain_" + random;
				}
			case ATTACK_1:
				return "weapons-arrow-shot";
			case EAT_PICKUP:
				if(entity.getEntityType() == EntityType.Tank) {
					int random = this.random(1, 2);
					return "speech-tank-nom_" + random;					
				}
				else if (entity.getEntityType() == EntityType.Hunter || entity.getEntityType() == EntityType.Knight) {
					int random = this.random(1, 3);
					return "speech-general-nom_" + random;
				}
			case KO:
			case DIE_PLAYER:
				if(entity.getEntityType() == EntityType.Tank) {
					int random = this.random(1, 2);
					return "speech-tank-ko_" + random;					
				}
				else if (entity.getEntityType() == EntityType.Hunter || entity.getEntityType() == EntityType.Knight) {
					int random = this.random(1, 3);
					return "speech-general-ko_" + random;
				}
			case FALLEN:
				if(entity.getEntityType() == EntityType.Tank) {
					int random = this.random(1, 2);
					return "speech-tank-fall_" + random;					
				}
				else if (entity.getEntityType() == EntityType.Hunter || entity.getEntityType() == EntityType.Knight) {
					int random = this.random(1, 1);
					return "speech-general-fall_" + random;
				}
			default:
				return "speech-tank-nom_1";
		}
		
	}
	
	private int random (int low, int high) {
		return (int) Math.round(Math.random() * (high - low) + low);
	}
	
	/**
	 * Differs between the local player or a remote player
	 * Calls either play for local player or remoteSound for remote player
	 * 
	 * @param event
	 * @param player
	 * @see play
	 * @see remoteSound
	 */
	public void playSoundByAction(EventType event, ClientEntity player) {
		this.localPlayer = (ClientPlayer) ClientEntityManager.getInstance().getEntityById(ClientEntityManager.getInstance().getPlayerEntityID());
		
		if (player.getID() == this.localPlayer.getID()) {
			this.play(this.connectSoundToAction(event, player), LocalSound.getSystemVolume());
		}
		else {
			this.remoteSound(this.connectSoundToAction(event, player), player);
		}
		System.out.println("EVENT TRIGGERED :: " + event + ", by player " + player.getID());
	}

	/**
	 * Stops the atm playing sound
	 */
	public void stop() {
		this.soundHandle.stop();
	}
}
