package de.hochschuletrier.gdw.ws1314.render;

import java.util.HashMap;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;

public class MaterialManager {

	protected static Material dbgMaterial;

	@SuppressWarnings("rawtypes")
	private HashMap<EntityType, HashMap<RenderState, Material>> map;
	private AssetManagerX assetManager;

	@SuppressWarnings("rawtypes")
	public MaterialManager(AssetManagerX assetManager) {
		map = new HashMap<EntityType, HashMap<RenderState, Material>>();
		this.assetManager = assetManager;

		dbgMaterial = new Material(assetManager, new MaterialInfo(
				"fallback", RenderState.NONE, 32, 32, Integer.MAX_VALUE, false));

		this.provideMaterials(EntityType.Tank, new MaterialInfo(
				"knightWhiteIdleDown", new RenderState(EntityStates.IDLE,
						FacingDirection.DOWN), 110, 110, 1, true),
				new MaterialInfo("hunterWhiteWalkLeft", new RenderState(
						EntityStates.WALKING, FacingDirection.LEFT), 110, 110,
						1, true));

		this.provideMaterials(EntityType.Hunter, new MaterialInfo(
				"hunterWhiteIdleDown", new RenderState(EntityStates.IDLE,
						FacingDirection.DOWN), 110, 74, 1, true),
				new MaterialInfo("hunterWhiteWalkLeft", new RenderState(
						EntityStates.WALKING, FacingDirection.LEFT), 110, 74,
						1, true), 
				new MaterialInfo("hunterWhiteWalkDown",
						new RenderState(EntityStates.WALKING,
								FacingDirection.DOWN), 110, 74, 1, true),
				new MaterialInfo("hunterWhiteAttackDown", new RenderState(
						EntityStates.ATTACK, FacingDirection.DOWN), 110, 74, 1,
						true)
		);

		this.provideMaterials(EntityType.Projectil, new MaterialInfo(
				"debugArrow", new RenderState(EntityStates.NONE), 64, 64, 1,
				false));

		this.provideMaterials(EntityType.Carrot, new MaterialInfo("carrot",
				new RenderState(EntityStates.NONE), 32, 32, -1, false));
		this.provideMaterials(EntityType.Ei, new MaterialInfo("egg",
				new RenderState(EntityStates.NONE), 32, 32, -1, false));
		this.provideMaterials(EntityType.Spinach, new MaterialInfo("spinach",
				new RenderState(EntityStates.NONE), 32, 32, -1, false));
		this.provideMaterials(EntityType.BridgeSwitch,
				new MaterialInfo("switch", new RenderState(EntityStates.NONE),
						32, 32, -1, false));
		this.provideMaterials(EntityType.Clover, new MaterialInfo("clover",
				new RenderState(EntityStates.NONE), 32, 32, -1, false));
		this.provideMaterials(EntityType.Bush, new MaterialInfo("bush",
				new RenderState(EntityStates.NONE), 32, 32, 10, false));

		this.provideMaterials(EntityType.ContactMine, new MaterialInfo(
				"contactMine", new RenderState(EntityStates.NONE), 32, 32, -1,
				false));

	}

	public void provideMaterials(EntityType entityType,
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
					map.put(entityType, new HashMap<RenderState, Material>());
				}
			}
			map.get(entityType).put(materialInfo.stateUsed, material);
			System.out.println("Setting " + entityType.name() + " material for " + materialInfo.stateUsed);
		}
	}

	public HashMap<RenderState, Material> fetch(EntityType entityType) {
		if (map.containsKey(entityType)) {
			return map.get(entityType);
		} else {
			System.out.println("Cannot find material for "
					+ entityType.toString());
			return null;
		}
	}

}
