package de.hochschuletrier.gdw.ws1314.render;


import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;

public class RenderObject implements Comparable<RenderObject> {

	protected ClientEntity entity;
	protected Material material;
	float stateTime;
	
	public RenderObject(Material material, ClientEntity entity) {
		this.entity = entity;
		this.material = material;
		stateTime = 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((RenderObject)obj).entity.getID() == this.entity.getID();
	}
	
	@Override
	public int compareTo(RenderObject o) {
		return material.compareTo(o.material);
	}
	
	public TextureRegion getActiveTexture() {
		
		return material.getActiveTexture(entity.getStateTime());
	}
	
	
}
