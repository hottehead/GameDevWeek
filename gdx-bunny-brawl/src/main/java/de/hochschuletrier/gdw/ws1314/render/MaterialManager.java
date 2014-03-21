package de.hochschuletrier.gdw.ws1314.render;

import java.util.HashMap;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;

public class MaterialManager {

	private static Material dbgMaterial;

	@SuppressWarnings("rawtypes")
	private HashMap<EntityType, Material> map;
	private AssetManagerX assetManager;

	@SuppressWarnings("rawtypes")
	public MaterialManager(AssetManagerX assetManager) {
		map = new HashMap<EntityType, Material>();
		this.assetManager = assetManager;

		dbgMaterial = new Material(assetManager, new MaterialInfo("fallback",
				32, 32, Integer.MAX_VALUE, false));

		this.provideMaterial(EntityType.Tank, new MaterialInfo(
				"knightWhiteIdleDown", 110, 110, 1, true));

		this.provideMaterial(EntityType.Hunter, new MaterialInfo(
				"hunterWhiteIdleDown", 110, 74, 1, true));

		this.provideMaterial(EntityType.Projectil, new MaterialInfo(
				"debugArrow", 64, 64, 1, false));
		this.provideMaterial(EntityType.Carrot, new MaterialInfo("carrot", 32,
				32, -1, false));
		this.provideMaterial(EntityType.Ei, new MaterialInfo("egg", 32, 32, -1,
				false));
		this.provideMaterial(EntityType.Spinach, new MaterialInfo("spinach",
				32, 32, -1, false));
		this.provideMaterial(EntityType.BridgeSwitch, new MaterialInfo(
				"switch", 32, 32, -1, false));
		this.provideMaterial(EntityType.Clover, new MaterialInfo("clover", 32,
				32, -1, false));
		this.provideMaterial(EntityType.Bush, new MaterialInfo("bush", 32, 32,
				10, false));

		this.provideMaterial(EntityType.ContactMine, new MaterialInfo(
				"contactMine", 32, 32, -1, false));

	}

	public void provideMaterial(EntityType materialType,
			MaterialInfo materialInfo) {
		Material material = null;
		if ((materialType != EntityType.None) && map.containsKey(materialType)) {
			// Overwriting existing Material
			material = map.get(materialType);
			material.putInfo(assetManager, materialInfo);
		} else {
			material = new Material(assetManager, materialInfo);
		}
		map.put(materialType, material);
	}

	public Material fetch(EntityType entityType) {
		if (map.containsKey(entityType)) {
			return map.get(entityType);
		} else {
			System.out.println("Cannot find material for "
					+ entityType.toString());
			return dbgMaterial;
		}
	}

}
