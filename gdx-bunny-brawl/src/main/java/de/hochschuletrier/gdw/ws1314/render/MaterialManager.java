package de.hochschuletrier.gdw.ws1314.render;

import java.util.HashMap;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientBridgeSwitch;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientBush;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientCarrot;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientClover;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientEgg;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientSpinach;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ClientProjectile;

public class MaterialManager {

	private static Material dbgMaterial;
	
	@SuppressWarnings("rawtypes")
	private HashMap<Class, Material> map;
	private AssetManagerX assetManager;
	
	@SuppressWarnings("rawtypes")
	public MaterialManager(AssetManagerX assetManager) {
		map = new HashMap<Class, Material>();
		this.assetManager = assetManager;
		dbgMaterial = new Material(assetManager.getTexture("fallback"), new MaterialInfo("", 32, 32, Integer.MAX_VALUE));
		
		this.provideMaterial(ClientPlayer.class, new MaterialInfo(
				"singleBunny", 110, 110, 1));
		this.provideMaterial(ClientProjectile.class,
				new MaterialInfo("debugArrow", 16, 16, 1));
		this.provideMaterial(ClientCarrot.class, new MaterialInfo(
				"carrot", 32, 32, 0));
		this.provideMaterial(ClientEgg.class, new MaterialInfo(
				"egg", 32, 32, -1));
		this.provideMaterial(ClientSpinach.class, new MaterialInfo(
				"spinach", 32, 32, 0));
		this.provideMaterial(ClientBridgeSwitch.class, new MaterialInfo(
				"switch", 32, 32, 0));
		this.provideMaterial(ClientClover.class, new MaterialInfo(
				"clover", 32, 32, 0));
		this.provideMaterial(ClientBush.class, new MaterialInfo(
				"bush", 32, 32, 0));
		
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
		if(map.containsKey(materialClass)) {
			return map.get(materialClass);
		}
		else {
			return dbgMaterial;
		}
	}
	
}
