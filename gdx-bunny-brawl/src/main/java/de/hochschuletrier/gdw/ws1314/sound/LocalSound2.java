package de.hochschuletrier.gdw.ws1314.sound;

import com.badlogic.gdx.audio.*;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;

public class LocalSound2 {
	private Sound soundHandle;
	private long soundID;
	
	public static AssetManagerX assetManager;
	private static float SystemVolume = 1.0f;
	private static float maxDistance = 300;
	private static ClientPlayer LocalPlayer;
	
	public static void init(AssetManagerX assetManager) {
		LocalSound2.assetManager = assetManager;
	}
	
	public static void setSystemVolume(float systemVolume) {
		LocalSound2.SystemVolume = systemVolume;
	}
	
	public static float getSystemVolume() {
		return LocalSound2.SystemVolume;
	}
	
	public LocalSound2() {
		
	}
	
	private void play(String soundName, float volume) {
		this.soundHandle = LocalSound2.assetManager.getSound(soundName);
		this.soundID = soundHandle.play();
		soundHandle.setVolume(this.soundID, LocalSound2.SystemVolume * volume);
	}
	
	private void remoteSound(String sound, ClientEntity remotePlayer) {
		double localX, localY, remoteX, remoteY;
		float volume, distance;
		
		localX = LocalSound2.LocalPlayer.getPosition().x;
		localY = LocalSound2.LocalPlayer.getPosition().y;
		
		remoteX = remotePlayer.getPosition().x;
		remoteY = remotePlayer.getPosition().y;
		
		//  SQRT( ( x1 - x2 )² + ( y1 - y2 )² )  <<< just pythagoras
		distance = (float) Math.sqrt( Math.pow( (localX - remoteX), 2 ) + Math.pow( (localY - remoteY), 2 ) );
		
		// volume will be [volume]% (percent) of systemVolume
		volume = (100 - (distance * 100 / LocalSound2.maxDistance)) / 100;
		
		this.play(sound, volume);
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
			case DESTROY:
				return "speech-tank-yeay_3speech-tank-yeay_3";
			case EGG_PICKUP:
				if(entity.getEntityType() == EntityType.Tank) {
					int random = this.random(1, 3);
					return "speech-tank-yeay_" + random;					
				}
				else if (entity.getEntityType() == EntityType.Hunter || entity.getEntityType() == EntityType.Knight) {
					int random = this.random(1, 3);
					return "speech-general-yeay_" + random;
				}
			default:
				return "speech-tank-nom_1";
		}
		
	}
	
	private int random (int low, int high) {
		return (int) Math.round(Math.random() * (high - low) + low);
	}
	
	public void playSoundByAction(EventType event, ClientEntity entity) {
		LocalSound2.LocalPlayer = (ClientPlayer) ClientEntityManager.getInstance().getEntityById(ClientEntityManager.getInstance().getPlayerEntityID());
		
		if (entity.getID() == LocalSound2.LocalPlayer.getID()) {
			this.play(this.connectSoundToAction(event, entity), LocalSound.getSystemVolume());
		}
		else {
			this.remoteSound(this.connectSoundToAction(event, entity), entity);
		}
		System.out.println("EVENT TRIGGERED :: " + event + ", by player " + entity.getID());
	}
	
	public void stop() {
		this.soundHandle.stop();
	}
	
}
