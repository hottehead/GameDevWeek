package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class BushMaterialDef extends MaterialDefinition {

	public BushMaterialDef() {
		super(64, 64, MaterialLayer.BUSH_LAYER);
	}
	
	@Override
	public void build() {
		newMaterial("bush", EntityStates.NONE, null, false);
		newMaterial("bushDisposeAnimation", EntityStates.DISPOSE, null, true);
	}

}
