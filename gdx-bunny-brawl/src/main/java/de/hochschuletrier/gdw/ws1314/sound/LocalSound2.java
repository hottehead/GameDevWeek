package de.hochschuletrier.gdw.ws1314.sound;

import java.sql.ClientInfoStatus;

import com.badlogic.gdx.audio.*;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.preferences.PreferenceKeys;

public class LocalSound2 {
	private Sound soundHandle;
	private long soundID;
	private long loopID;
	
	public static AssetManagerX assetManager;
	private static float SystemVolume = 1.0f;
	private static float maxDistance = 300.0f;
	private static ClientPlayer LocalPlayer;
	
	public static void init(AssetManagerX assetManager) {
		LocalSound2.assetManager = assetManager;
	}
	
	public static void setSystemVolume(float systemVolume) {
		LocalSound2.SystemVolume = systemVolume;
		Main.getInstance().gamePreferences.putFloat(PreferenceKeys.volumeSound, systemVolume);
	}
	
	public static float getSystemVolume() {
		return LocalSound2.SystemVolume;
	}
	
	public LocalSound2() {
		
	}
	
	private void play(String soundName, float volume) {
		this.soundHandle = LocalSound2.assetManager.getSound(soundName);
		this.soundID = this.soundHandle.play();
		soundHandle.setVolume(this.soundID, LocalSound2.SystemVolume * volume);
	}
	
	private void loop(String soundName, float volume) {
		this.stopLoop();
		this.soundHandle = LocalSound2.assetManager.getSound(soundName);
		this.loopID = this.soundHandle.loop(LocalSound2.SystemVolume * volume);
	}
	
	private void stopLoop() {
		if (this.soundHandle != null)
			this.soundHandle.stop(this.loopID);	
	}
	
	private void remoteSound(String sound, ClientEntity remotePlayer, boolean loop) {
		double localX, localY, remoteX, remoteY;
		float volume, distance;
		
		localX = LocalSound2.LocalPlayer.getPosition().x;
		localY = LocalSound2.LocalPlayer.getPosition().y;
			
		remoteX = remotePlayer.getPosition().x;
		remoteY = remotePlayer.getPosition().y;
		
		//  SQRT( ( x1 - x2 )² + ( y1 - y2 )² )  <<< just pythagoras
		distance = (float) Math.sqrt( Math.pow( (localX - remoteX), 2 ) + Math.pow( (localY - remoteY), 2 ) );
		
		// volume will be [volume]% (percent) of systemVolume
		if (distance <= LocalSound2.maxDistance)
			volume = (1.0f - (distance * 100.f / LocalSound2.maxDistance) / 100.0f);
		else
			volume = 0;
		
		if (loop)
			this.loop(sound, volume);
		else
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
				return "speech-tank-yeay_3";
			case EGG_PICKUP:
				if(entity.getEntityType() == EntityType.Tank) {
					int random = this.random(1, 3);
					return "speech-tank-yeay_" + random;					
				}
				else if (entity.getEntityType() == EntityType.Hunter || entity.getEntityType() == EntityType.Knight) {
					int random = this.random(1, 3);
					return "speech-general-yeay_" + random;
				}
			case WALK_GRASS:
			case WALK_WAY:
			case WALK_BRIDGE:
				return "walk-general-grass";
			default:
				return "speech-tank-nom_1";
		}
		
	}
	
	private int random (int low, int high) {
		return (int) Math.round(Math.random() * (high - low) + low);
	}
	
	public void playSoundByAction(EventType event, ClientEntity entity) {
		LocalSound2.LocalPlayer = (ClientPlayer) ClientEntityManager.getInstance().getEntityById(ClientEntityManager.getInstance().getPlayerEntityID());
		if (event.equals(EventType.IDLE))
			this.stopLoop();
		else {
			String soundAction = this.connectSoundToAction(event, entity);
			boolean loop;
		System.out.println("Event getriggert :: " + event);
			switch(soundAction) {
				case "walk-general-grass":
					loop = true;
					break;
				default:
					loop = false;
					break;
			}
		
			if (entity.getID() == LocalSound2.LocalPlayer.getID()) {
				if (loop)
					this.loop(this.connectSoundToAction(event, entity), LocalSound.getSystemVolume());
				else
					this.play(this.connectSoundToAction(event, entity), LocalSound.getSystemVolume());
			}
			else {
				this.remoteSound(this.connectSoundToAction(event, entity), entity, loop);
			}
		}
	}
	
	public void stop() {
		this.soundHandle.stop();
	}
	
}
