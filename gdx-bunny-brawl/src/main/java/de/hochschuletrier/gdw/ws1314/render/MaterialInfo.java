package de.hochschuletrier.gdw.ws1314.render;

public class MaterialInfo {
	final protected String textureName;
	final protected float width, height;
	final protected int layer;
	
	public MaterialInfo(String textureName, float width, float height, int layer) {
		this.textureName = textureName;
		this.width = width;
		this.height = height;
		this.layer = layer;
	}
}
