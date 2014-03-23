package de.hochschuletrier.gdw.ws1314.render;


import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientLevelObject;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ClientProjectile;

public class RenderObject implements Comparable<RenderObject> {

	
	protected ClientEntity entity;
	protected HashMap<RenderState, Material> materialAtlas;
	float stateTime;
	private RenderState stateStorage;
	
	public RenderObject(HashMap<RenderState, Material> material, ClientEntity entity) {
		this.entity = entity;
		this.materialAtlas = material;
		stateTime = 0;
		stateStorage = new RenderState();
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((RenderObject)obj).entity.getID() == this.entity.getID();
	}
	
	@Override
	public int compareTo(RenderObject o) {
		final int GREATER_THAN = 1;
		final int EQUAL_TO = 0;
		final int LESS_THAN = -1;
		if(materialAtlas==null) {
			return GREATER_THAN;
		}
		if(o.materialAtlas==null) {
			return GREATER_THAN;
		}
		Material activeMaterialThis = materialAtlas.get(this.getActiveState());
		Material activeMaterialOther= o.materialAtlas.get(o.getActiveState());
		if(activeMaterialThis==null) {
			return GREATER_THAN;
		}
		if(activeMaterialOther==null) {
			return GREATER_THAN;
		}

		float thisTop = this.entity.getPosition().y - activeMaterialThis.height*0.5f;
		float thisBot = this.entity.getPosition().y + activeMaterialThis.height*0.5f;
		float otherTop = o.entity.getPosition().y - activeMaterialOther.height*0.5f;
		float otherBot = o.entity.getPosition().y + activeMaterialOther.height*0.5f;
		
		return activeMaterialThis.compareTo(activeMaterialOther);
		
	}
	
	protected RenderState getActiveState() {
		if(this.entity instanceof ClientPlayer) {
			ClientPlayer playerEntity = (ClientPlayer)entity;
			stateStorage.setState(playerEntity.getCurrentPlayerState(), entity.getFacingDirection());
			
		}
		else if(this.entity instanceof ClientLevelObject) {
			ClientLevelObject levelEntity = (ClientLevelObject)entity;
			stateStorage.setState(levelEntity.getLevelObjectState(), null);
		}
		else if(this.entity instanceof ClientProjectile) {
			stateStorage.setState(EntityStates.NONE, null);
		}
		else {
			stateStorage.setState(EntityStates.NONE, null);
		}
		return stateStorage;
	}
	
	public Material getActiveMaterial() {
		if(materialAtlas!=null) {
			if(materialAtlas.containsKey(this.getActiveState())) {
				return materialAtlas.get(this.getActiveState());
			}
			if(materialAtlas.containsKey(EntityStates.NONE)) {
				return materialAtlas.get(EntityStates.NONE);
			}
		}
		return null; // no entry found use debug
	}
	
	public TextureRegion getActiveTexture() {
		Material material = materialAtlas.get(this.getActiveState());
		return material.getActiveTexture(entity.getStateTime());
	}

	public boolean isVisible() {
		if(entity instanceof ClientLevelObject) {
			return ((ClientLevelObject)entity).getVisible();
		}
		return true;
	}
}
