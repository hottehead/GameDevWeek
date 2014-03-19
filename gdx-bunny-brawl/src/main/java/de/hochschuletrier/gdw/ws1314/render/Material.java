package de.hochschuletrier.gdw.ws1314.render;

import com.badlogic.gdx.graphics.Texture;

// 32 bit coded material id
public class Material implements Comparable<Material> {

	protected int Layer;
	protected float width, height;
	protected Texture texture;

	protected Material(Texture texture, MaterialInfo materialInfo) {
		putInfo(texture, materialInfo);
	}
	
	protected void putInfo(Texture texture, MaterialInfo materialInfo) {
		this.texture = texture;
		this.Layer = materialInfo.layer;
		this.width = materialInfo.width; 
		this.height = materialInfo.height;
	}

	/*
	 * Compares this object with the specified object for order. Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.
	 */
	@Override
	public int compareTo(Material o) {
		final int GREATER_THAN = 1;
		final int EQUAL_TO = 0;
		final int LESS_THAN = -1;
		if(this.Layer < o.Layer) {
			return LESS_THAN;
		}
		
		return EQUAL_TO;
	}

}
