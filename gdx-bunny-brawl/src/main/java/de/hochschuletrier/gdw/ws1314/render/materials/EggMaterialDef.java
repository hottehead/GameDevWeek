package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class EggMaterialDef extends MaterialDefinition {

	public EggMaterialDef() {
		super(32, 32, MaterialLayer.PICKUP_LAYER);
	}
	
	@Override
	public void build() {
		newMaterial("egg",EntityStates.NONE, null, false);
	}

}
