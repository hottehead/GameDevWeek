package de.hochschuletrier.gdw.ws1314.render;

import java.util.HashMap;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class MaterialManager {

	@SuppressWarnings("rawtypes")
	private HashMap<Class, Material> map;
	private AssetManagerX assetManager;
	
	@SuppressWarnings("rawtypes")
	public MaterialManager(AssetManagerX assetManager) {
		map = new HashMap<Class, Material>();
		this.assetManager = assetManager;
	}
	
	public <E> void provideMaterial(Class<E> materialClass, MaterialInfo materialInfo) {
		Material material = null;
		if(map.containsKey(materialClass)) {
			//Overwriting existing Material
			material = map.get(materialClass);
			material.putInfo(assetManager.getTexture(materialInfo.textureName),
					materialInfo);
		}
		else {
			material = new Material(assetManager.getTexture(materialInfo.textureName),
					materialInfo);
		}
		map.put(materialClass, material);
	}
	
	public <E> Material fetch(Class<E> materialClass) {
		return map.get(materialClass);
	}
	
}
