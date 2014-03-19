package de.hochschuletrier.gdw.ws1314.render;

public class MaterialInfo {
	final protected String textureName;
	final protected float width, height;
	final protected int layer;
	
	final String shaderVertPath;
	final String shaderFragPath;
	
	public MaterialInfo(String textureName, float width, float height, int layer) {
		this(textureName, width, height, layer, null, null);
	}
	
	public MaterialInfo(String textureName, float width, float height, int layer, String shaderVertPath, String shaderFrag) {
		this.textureName = textureName;
		this.width = width;
		this.height = height;
		this.layer = layer;
		
		this.shaderVertPath = shaderVertPath;
		this.shaderFragPath = shaderFrag;
	}
}
