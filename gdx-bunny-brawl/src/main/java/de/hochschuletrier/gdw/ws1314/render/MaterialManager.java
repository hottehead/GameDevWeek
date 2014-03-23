package de.hochschuletrier.gdw.ws1314.render;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
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
import de.hochschuletrier.gdw.ws1314.render.materials.StrawMaterialDef;
import de.hochschuletrier.gdw.ws1314.render.materials.SwitchMaterialDef;
import de.hochschuletrier.gdw.ws1314.render.materials.TankMaterialDef;

public class MaterialManager {
	private static final Logger logger = LoggerFactory.getLogger(MaterialManager.class);
	protected static Material dbgMaterial;

	@SuppressWarnings("rawtypes")
	private HashMap<RenderType, HashMap<RenderState, Material>> map;
	private AssetManagerX assetManager;

	@SuppressWarnings("rawtypes")
	public MaterialManager(AssetManagerX assetManager) {
		map = new HashMap<RenderType, HashMap<RenderState, Material>>();
		this.assetManager = assetManager;

		dbgMaterial = new Material(assetManager, new MaterialInfo("fallback",
				RenderState.NONE, 32, 32, Integer.MAX_VALUE, false));

		this.provideMaterials(new RenderType(EntityType.Knight, TeamColor.WHITE), new KnightMaterialDef(TeamColor.WHITE).get());
		this.provideMaterials(new RenderType(EntityType.Hunter, TeamColor.WHITE), new HunterMaterialDef(TeamColor.WHITE).get());
		this.provideMaterials(new RenderType(EntityType.Tank, TeamColor.WHITE), new TankMaterialDef(TeamColor.WHITE).get());
		
		this.provideMaterials(new RenderType(EntityType.Knight, TeamColor.BLACK), new KnightMaterialDef(TeamColor.BLACK).get());
		this.provideMaterials(new RenderType(EntityType.Hunter, TeamColor.BLACK), new HunterMaterialDef(TeamColor.BLACK).get());
		this.provideMaterials(new RenderType(EntityType.Tank, TeamColor.BLACK), new TankMaterialDef(TeamColor.BLACK).get());
		
		this.provideMaterials(new RenderType(EntityType.Projectil), new ArrowMaterialDef().get());

		this.provideMaterials(new RenderType(EntityType.BridgeSwitch), new SwitchMaterialDef().get());
		
		this.provideMaterials(new RenderType(EntityType.Carrot), new CarrotMaterialDef().get());
		this.provideMaterials(new RenderType(EntityType.Ei), new EggMaterialDef().get());
		this.provideMaterials(new RenderType(EntityType.Spinach), new SpinachMaterialDef().get());
		this.provideMaterials(new RenderType(EntityType.Clover), new CloverMaterialDef().get());
		this.provideMaterials(new RenderType(EntityType.Bush), new BushMaterialDef().get());
		this.provideMaterials(new RenderType(EntityType.ContactMine), new ContactMineMaterialDef().get());
		this.provideMaterials(new RenderType(EntityType.HayBale), new StrawMaterialDef().get());
		
		//bridge parts
		this.provideMaterials(new RenderType(EntityType.BRIDGE_HORIZONTAL_LEFT), new BridgeHorizontalLeftDef().get());
		this.provideMaterials(new RenderType(EntityType.BRIDGE_HORIZONTAL_MIDDLE), new BridgeHorizontalMiddleDef().get());
		this.provideMaterials(new RenderType(EntityType.BRIDGE_HORIZONTAL_RIGHT), new BridgeHorizontalRightDef().get());
		this.provideMaterials(new RenderType(EntityType.BRIDGE_VERTICAL_BOTTOM), new BridgeVerticalBottomDef().get());
		this.provideMaterials(new RenderType(EntityType.BRIDGE_VERTICAL_MIDDLE), new BridgeVerticalMiddleDef().get());
		this.provideMaterials(new RenderType(EntityType.BRIDGE_VERTICAL_TOP), new BridgeVerticalTopDef().get());
		
	}

	public void provideMaterials(RenderType renderType,
			MaterialInfo... materialInfos) {
		for (MaterialInfo materialInfo : materialInfos) {
			Material material = null;
			if ((renderType.entityType != EntityType.None) && map.containsKey(renderType)
					&& map.get(renderType).containsKey(materialInfo.stateUsed)) {
				// Overwriting existing Material
				material = map.get(renderType).get(materialInfo.stateUsed);
				material.putInfo(assetManager, materialInfo);
			} else {
				material = new Material(assetManager, materialInfo);
				if((material.isAnimation && material.animation==null) || (!material.isAnimation && material.texture==null)) {
					logger.error("Material name inconsistency! "+materialInfo.textureName+" of "+renderType.toString());
				}
				if (map.get(renderType) == null) {
					map.put(renderType, new HashMap<RenderState, Material>());
				}
			}
			map.get(renderType).put(materialInfo.stateUsed, material);
		}
	}

	public HashMap<RenderState, Material> fetch(RenderType renderType) {
		if (map.containsKey(renderType)) {
			return map.get(renderType);
		} else {
			logger.error("Cannot find material for "
					+ renderType.toString());
			return null;
		}
	}

}
