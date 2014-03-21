package de.hochschuletrier.gdw.ws1314.render;


import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.EventType;

public class RenderObject implements Comparable<RenderObject> {

	protected ClientEntity entity;
	protected HashMap<EventType, Material> materialAtlas;
	float stateTime;
	
	public RenderObject(HashMap<EventType, Material> material, ClientEntity entity) {
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
		Material activeMaterialThis = materialAtlas.get(entity.activeAction);
		Material activeMaterialOther= o.materialAtlas.get(entity.activeAction);
		return activeMaterialThis.compareTo(activeMaterialOther);
	}
	
	public Material getActiveMaterial() {
		if(materialAtlas!=null) {
			if(materialAtlas.containsKey(entity.activeAction)) {
				return materialAtlas.get(entity.activeAction);
			}
			if(materialAtlas.containsKey(EventType.ANY)) {
				return materialAtlas.get(EventType.ANY);
			}
		}
		return null; // no entry found use debug
	}
	
	public TextureRegion getActiveTexture() {
		return materialAtlas.get(entity.activeAction).getActiveTexture(entity.getStateTime());
	}
}
