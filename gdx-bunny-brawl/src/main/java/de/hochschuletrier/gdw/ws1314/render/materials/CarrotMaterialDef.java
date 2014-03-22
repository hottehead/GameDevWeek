package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class CarrotMaterialDef extends MaterialDefinition {

	public CarrotMaterialDef() {
		// TODO Auto-generated constructor stub
		super(32, 32, MaterialLayer.PICKUP_LAYER);
	}
	
	@Override
	public void build() {
		newMaterial("carrot",EntityStates.NONE, null, false);
	}

}
