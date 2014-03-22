package de.hochschuletrier.gdw.ws1314.render;

import java.util.HashMap;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;

public class MaterialManager {

	protected static HashMap<EntityStates, Material> dbgMaterialAtlas;

	@SuppressWarnings("rawtypes")
	private HashMap<EntityType, HashMap<EntityStates, Material>> map;
	private AssetManagerX assetManager;

	@SuppressWarnings("rawtypes")
	public MaterialManager(AssetManagerX assetManager) {
		map = new HashMap<EntityType, HashMap<EntityStates, Material>>();
		this.assetManager = assetManager;

		Material dbgMaterial = new Material(assetManager,
				new MaterialInfo("fallback", EntityStates.NONE, 32, 32,
						Integer.MAX_VALUE, false));
		dbgMaterialAtlas = new HashMap<EntityStates, Material>();
		dbgMaterialAtlas.put(EntityStates.NONE, dbgMaterial);

		this.provideMaterial(EntityType.Tank, new MaterialInfo(
				"knightWhiteIdleDown", EntityStates.IDLE, 110, 110, 1, true),
				new MaterialInfo("hunterWhiteWalkLeft", EntityStates.WALKING,
						110, 110, 1, true));

		this.provideMaterial(EntityType.Hunter, new MaterialInfo(
				"hunterWhiteIdleDown", EntityStates.IDLE, 110, 74, 1, true),
				new MaterialInfo("hunterWhiteWalkLeft", EntityStates.WALKING,
						110, 74, 1, true),
						new MaterialInfo("hunterWhiteAttackDown", EntityStates.ATTACK, 110, 74, 1, true));

		this.provideMaterial(EntityType.Projectil, new MaterialInfo(
				"debugArrow", EntityStates.NONE, 64, 64, 1, false));

		this.provideMaterial(EntityType.Carrot, new MaterialInfo("carrot",
				EntityStates.NONE, 32, 32, -1, false));
		this.provideMaterial(EntityType.Ei, new MaterialInfo("egg",
				EntityStates.NONE, 32, 32, -1, false));
		this.provideMaterial(EntityType.Spinach, new MaterialInfo("spinach",
				EntityStates.NONE, 32, 32, -1, false));
		this.provideMaterial(EntityType.BridgeSwitch, new MaterialInfo(
				"switch", EntityStates.NONE, 32, 32, -1, false));
		this.provideMaterial(EntityType.Clover, new MaterialInfo("clover",
				EntityStates.NONE, 32, 32, -1, false));
		this.provideMaterial(EntityType.Bush, new MaterialInfo("bush",
				EntityStates.NONE, 32, 32, 10, false));

		this.provideMaterial(EntityType.ContactMine, new MaterialInfo(
				"contactMine", EntityStates.NONE, 32, 32, -1, false));

	}

	public void provideMaterial(EntityType entityType,
			MaterialInfo... materialInfos) {
		for (MaterialInfo materialInfo : materialInfos) {
			Material material = null;
			if ((entityType != EntityType.None) && map.containsKey(entityType)
					&& map.get(entityType).containsKey(materialInfo.stateUsed)) {
				// Overwriting existing Material
				material = map.get(entityType).get(materialInfo.stateUsed);
				material.putInfo(assetManager, materialInfo);
			} else {
				material = new Material(assetManager, materialInfo);
				if (map.get(entityType) == null) {
					map.put(entityType, new HashMap<EntityStates, Material>());
				}
			}
			map.get(entityType).put(materialInfo.stateUsed, material);
		}
	}

	public HashMap<EntityStates, Material> fetch(EntityType entityType) {
		if (map.containsKey(entityType)) {
			return map.get(entityType);
		} else {
			System.out.println("Cannot find material for "
					+ entityType.toString());
			return dbgMaterialAtlas;
		}
	}

}
