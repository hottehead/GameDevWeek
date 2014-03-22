package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class BridgeHorizontalMiddleDef extends BridgeHoriDef {

	@Override
	public void build() {
		newMaterial("bridgeHoriMiddle", EntityStates.NONE, null, false);
	}

	
}
