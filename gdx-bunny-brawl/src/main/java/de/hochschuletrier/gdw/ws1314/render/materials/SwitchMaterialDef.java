package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class SwitchMaterialDef extends MaterialDefinition {

	public SwitchMaterialDef() {
		super(64, 64, MaterialLayer.SWITCH_LAYER);
	}
	
	@Override
	public void build() {
		newMaterial("switch", EntityStates.NONE, null, false);
	}

}
