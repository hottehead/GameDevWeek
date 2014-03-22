package de.hochschuletrier.gdw.ws1314.render;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.render.materials.ArrowMaterialDef;
import de.hochschuletrier.gdw.ws1314.render.materials.BridgeHorizontalLeftDef;
import de.hochschuletrier.gdw.ws1314.render.materials.BridgeHorizontalMiddleDef;
import de.hochschuletrier.gdw.ws1314.render.materials.BridgeHorizontalRightDef;
import de.hochschuletrier.gdw.ws1314.render.materials.BridgeVerticalBottomDef;
import de.hochschuletrier.gdw.ws1314.render.materials.BridgeVerticalMiddleDef;
import de.hochschuletrier.gdw.ws1314.render.materials.BridgeVerticalTopDef;
import de.hochschuletrier.gdw.ws1314.render.materials.BushMaterialDef;
import de.hochschuletrier.gdw.ws1314.render.materials.CarrotMaterialDef;
import de.hochschuletrier.gdw.ws1314.render.materials.CloverMaterialDef;
import de.hochschuletrier.gdw.ws1314.render.materials.ContactMineMaterialDef;
import de.hochschuletrier.gdw.ws1314.render.materials.EggMaterialDef;
import de.hochschuletrier.gdw.ws1314.render.materials.HunterMaterialDef;
import de.hochschuletrier.gdw.ws1314.render.materials.KnightMaterialDef;
import de.hochschuletrier.gdw.ws1314.render.materials.SpinachMaterialDef;
import de.hochschuletrier.gdw.ws1314.render.materials.SwitchMaterialDef;

public class MaterialManager {
	private static final Logger logger = LoggerFactory.getLogger(MaterialManager.class);
	protected static Material dbgMaterial;

	@SuppressWarnings("rawtypes")
	private HashMap<EntityType, HashMap<RenderState, Material>> map;
	private AssetManagerX assetManager;

	@SuppressWarnings("rawtypes")
	public MaterialManager(AssetManagerX assetManager) {
		map = new HashMap<EntityType, HashMap<RenderState, Material>>();
		this.assetManager = assetManager;

		dbgMaterial = new Material(assetManager, new MaterialInfo("fallback",
				RenderState.NONE, 32, 32, Integer.MAX_VALUE, false));

		this.provideMaterials(EntityType.Tank, new KnightMaterialDef().get());
		this.provideMaterials(EntityType.Hunter, new HunterMaterialDef().get());
		
		this.provideMaterials(EntityType.Projectil, new ArrowMaterialDef().get());

		this.provideMaterials(EntityType.BridgeSwitch, new SwitchMaterialDef().get());
		
		this.provideMaterials(EntityType.Carrot, new CarrotMaterialDef().get());
		this.provideMaterials(EntityType.Ei, new EggMaterialDef().get());
		this.provideMaterials(EntityType.Spinach, new SpinachMaterialDef().get());
		this.provideMaterials(EntityType.Clover, new CloverMaterialDef().get());
		this.provideMaterials(EntityType.Bush, new BushMaterialDef().get());
		this.provideMaterials(EntityType.ContactMine, new ContactMineMaterialDef().get());
		
		//bridge parts
		this.provideMaterials(EntityType.BRIDGE_HORIZONTAL_LEFT, new BridgeHorizontalLeftDef().get());
		this.provideMaterials(EntityType.BRIDGE_HORIZONTAL_MIDDLE, new BridgeHorizontalMiddleDef().get());
		this.provideMaterials(EntityType.BRIDGE_HORIZONTAL_RIGHT, new BridgeHorizontalRightDef().get());
		this.provideMaterials(EntityType.BRIDGE_VERTICAL_BOTTOM, new BridgeVerticalBottomDef().get());
		this.provideMaterials(EntityType.BRIDGE_VERTICAL_MIDDLE, new BridgeVerticalMiddleDef().get());
		this.provideMaterials(EntityType.BRIDGE_VERTICAL_TOP, new BridgeVerticalTopDef().get());

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
				if((material.isAnimation && material.animation==null) || (!material.isAnimation && material.texture==null)) {
					logger.error("Material name inconsistency! "+materialInfo.textureName+" of "+entityType.toString());
				}
				if (map.get(entityType) == null) {
					map.put(entityType, new HashMap<RenderState, Material>());
				}
			}
			map.get(entityType).put(materialInfo.stateUsed, material);
//			logger.info("Setting " + entityType.name()
//					+ " material for " + materialInfo.stateUsed);
		}
	}

	public HashMap<RenderState, Material> fetch(EntityType entityType) {
		if (map.containsKey(entityType)) {
			return map.get(entityType);
		} else {
			logger.error("Cannot find material for "
					+ entityType.toString());
			return null;
		}
	}

}
