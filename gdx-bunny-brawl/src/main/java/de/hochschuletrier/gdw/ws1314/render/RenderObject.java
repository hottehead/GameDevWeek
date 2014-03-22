package de.hochschuletrier.gdw.ws1314.render;


import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientLevelObject;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;

public class RenderObject implements Comparable<RenderObject> {

	protected ClientEntity entity;
	protected HashMap<EntityStates, Material> materialAtlas;
	float stateTime;
	
	public RenderObject(HashMap<EntityStates, Material> material, ClientEntity entity) {
		this.entity = entity;
		this.materialAtlas = material;
		stateTime = 0;
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
	
	protected EntityStates getActiveState() {
		if(this.entity instanceof ClientPlayer) {
			return ((ClientPlayer)entity).getCurrentPlayerState();
		}
		if(this.entity instanceof ClientLevelObject) {
			return ((ClientLevelObject)entity).getLevelObjectState();
		}
		return EntityStates.NONE;
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
