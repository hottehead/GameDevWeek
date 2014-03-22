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
		Material activeMaterialThis = materialAtlas.get(this.getActiveState());
		Material activeMaterialOther= o.materialAtlas.get(o.getActiveState());
		return activeMaterialThis.compareTo(activeMaterialOther);
	}
	
	protected RenderState getActiveState() {
		if(this.entity instanceof ClientPlayer) {
			ClientPlayer playerEntity = (ClientPlayer)entity;
			stateStorage.setState(playerEntity.getCurrentPlayerState(), entity.getFacingDirection());
			
		}
		if(this.entity instanceof ClientLevelObject) {
			ClientLevelObject levelEntity = (ClientLevelObject)entity;
			stateStorage.setState(levelEntity.getLevelObjectState(), entity.getFacingDirection());
		}
		if(this.entity instanceof ClientProjectile) {
			stateStorage.setState(EntityStates.NONE, entity.getFacingDirection());
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
		return materialAtlas.get(this.getActiveState()).getActiveTexture(entity.getStateTime());
	}
}
