package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class ArrowMaterialDef extends MaterialDefinition {

	public ArrowMaterialDef() {
		super(38, 6, MaterialLayer.ARROW_LAYER);
	}

	@Override
	public void build() {
		
		newMaterial("arrow", EntityStates.NONE, null, false);
		
	}

}
