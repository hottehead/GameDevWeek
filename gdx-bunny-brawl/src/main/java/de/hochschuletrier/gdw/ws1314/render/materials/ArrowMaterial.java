package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class ArrowMaterial extends MaterialDefinition {

	public ArrowMaterial() {
		super(38, 6, 10);
	}

	@Override
	public void build() {
		
		newMaterial("arrow", EntityStates.NONE, null, false);
		
	}

}
