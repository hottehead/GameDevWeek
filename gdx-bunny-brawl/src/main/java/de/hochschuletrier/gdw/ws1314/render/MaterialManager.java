package de.hochschuletrier.gdw.ws1314.render;

import java.util.HashMap;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;

public class MaterialManager {

	protected static HashMap<EventType, Material> dbgMaterialAtlas;

	@SuppressWarnings("rawtypes")
	private HashMap<EntityType, HashMap<EventType, Material>> map;
	private AssetManagerX assetManager;

	@SuppressWarnings("rawtypes")
	public MaterialManager(AssetManagerX assetManager) {
		map = new HashMap<EntityType, HashMap<EventType,Material>>();
		this.assetManager = assetManager;

		Material dbgMaterial = new Material(assetManager, new MaterialInfo("fallback", EventType.ANY,
				32, 32, Integer.MAX_VALUE, false));
		dbgMaterialAtlas = new HashMap<EventType, Material>();
		dbgMaterialAtlas.put(EventType.ANY, dbgMaterial);

		this.provideMaterial(EntityType.Tank, new MaterialInfo(
				"knightWhiteIdleDown", EventType.IDLE, 110, 110, 1, true));
		this.provideMaterial(EntityType.Tank, new MaterialInfo(
				"hunterWhiteWalkLeft", EventType.WALK_LEFT, 110, 110, 1, true));

		this.provideMaterial(EntityType.Hunter, new MaterialInfo(
				"hunterWhiteIdleDown", EventType.IDLE, 110, 74, 1, true));
		this.provideMaterial(EntityType.Hunter, new MaterialInfo(
				"hunterWhiteWalkLeft", EventType.WALK_LEFT, 110, 74, 1, true));
		
		this.provideMaterial(EntityType.Projectil, new MaterialInfo(
				"debugArrow", EventType.IDLE, 64, 64, 1, false));
		this.provideMaterial(EntityType.Carrot, new MaterialInfo("carrot", EventType.IDLE, 32,
				32, -1, false));
		this.provideMaterial(EntityType.Ei, new MaterialInfo("egg", EventType.IDLE, 32, 32, -1,
				false));
		this.provideMaterial(EntityType.Spinach, new MaterialInfo("spinach",
				EventType.IDLE, 32, 32, -1, false));
		this.provideMaterial(EntityType.BridgeSwitch, new MaterialInfo(
				"switch", EventType.IDLE, 32, 32, -1, false));
		this.provideMaterial(EntityType.Clover, new MaterialInfo("clover", EventType.IDLE, 32,
				32, -1, false));
		this.provideMaterial(EntityType.Bush, new MaterialInfo("bush", EventType.IDLE, 32, 32,
				10, false));

		this.provideMaterial(EntityType.ContactMine, new MaterialInfo(
				"contactMine", EventType.IDLE, 32, 32, -1, false));

	}

	public void provideMaterial(EntityType entityType,
			MaterialInfo materialInfo) {
		Material material = null;
		if ((entityType != EntityType.None) && map.containsKey(entityType) && map.get(entityType).containsKey(materialInfo.stateUsed)) {
			// Overwriting existing Material
			material = map.get(entityType).get(materialInfo.stateUsed);
			material.putInfo(assetManager, materialInfo);
		} else {
			material = new Material(assetManager, materialInfo);
			if(map.get(entityType)==null) {
				map.put(entityType, new HashMap<EventType, Material>());
			}
		}
		map.get(entityType).put(materialInfo.stateUsed, material);
	}

	public HashMap<EventType, Material> fetch(EntityType entityType) {
		if (map.containsKey(entityType)) {
			return map.get(entityType);
		} else {
			System.out.println("Cannot find material for "
					+ entityType.toString());
			return dbgMaterialAtlas;
		}
	}

}
