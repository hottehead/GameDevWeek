package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class BridgeHorizontalLeftDef extends BridgeHoriDef {

	@Override
	public void build() {
		newMaterial("bridgeHoriLeft", EntityStates.NONE, null, false);
	}

}
