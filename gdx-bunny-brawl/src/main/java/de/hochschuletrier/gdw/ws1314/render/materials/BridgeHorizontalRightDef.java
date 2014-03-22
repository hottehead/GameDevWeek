package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class BridgeHorizontalRightDef extends BridgeHoriDef {

	@Override
	public void build() {
		newMaterial("bridgeHoriRight", EntityStates.NONE, null, false);
	}

}
