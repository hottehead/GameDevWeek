package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class BridgeVerticalBottomDef extends BridgeVertDef {

	@Override
	public void build() {
		newMaterial("bridgeVertBottom", EntityStates.NONE, null, false);
	}

}
