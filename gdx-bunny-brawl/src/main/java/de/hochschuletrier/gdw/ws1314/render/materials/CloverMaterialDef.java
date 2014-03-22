package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class CloverMaterialDef extends MaterialDefinition {

	public CloverMaterialDef() {
		super(32, 32, MaterialLayer.PICKUP_LAYER);
	}
	
	@Override
	public void build() {
		newMaterial("clover", EntityStates.NONE, null, false);
	}

}
