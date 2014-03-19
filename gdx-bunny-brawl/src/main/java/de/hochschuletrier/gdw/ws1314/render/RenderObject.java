package de.hochschuletrier.gdw.ws1314.render;


import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;

public class RenderObject implements Comparable<RenderObject> {

	protected ClientEntity entity;
	protected Material material;
	
	public RenderObject(Material material, ClientEntity entity) {
		this.entity = entity;
		this.material = material;
	}
	
	@Override
	public int compareTo(RenderObject o) {
		return material.compareTo(o.material);
	}
	
	
	
	
}
