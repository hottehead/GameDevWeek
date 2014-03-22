package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class BridgeVerticalMiddleDef extends BridgeVertDef {

	@Override
	public void build() {
		newMaterial("bridgeVertMiddle", EntityStates.NONE, null, false);

	}

}
