package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class StrawMaterialDef extends MaterialDefinition {

	public StrawMaterialDef() {
		super(64, 64, MaterialLayer.BUSH_LAYER);
	}
	
	@Override
	public void build() {
		newMaterial("straw", EntityStates.NONE, null, false);
		newMaterial("straw", EntityStates.WET, null, false);
		newMaterial("straw", EntityStates.KNOCKBACK, null, false);
	}

}
