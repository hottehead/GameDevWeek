package de.hochschuletrier.gdw.ws1314.render;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.render.materials.MaterialDefinition;
import de.hochschuletrier.gdw.ws1314.render.materials.MaterialLayer;

public class SpinachMaterialDef extends MaterialDefinition {

	public SpinachMaterialDef() {
		super(32, 32, MaterialLayer.PICKUP_LAYER);
	}
	
	@Override
	public void build() {
		newMaterial("spinach", EntityStates.NONE, null, false);
	}

}
