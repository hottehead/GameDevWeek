package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class BridgeVerticalTopDef extends BridgeVertDef {

	@Override
	public void build() {
		newMaterial("bridgeVertTop", EntityStates.NONE, null, false);
	}

}
