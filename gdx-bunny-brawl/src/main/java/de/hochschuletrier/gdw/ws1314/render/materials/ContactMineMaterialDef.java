package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

public class ContactMineMaterialDef extends MaterialDefinition {

	public ContactMineMaterialDef() {
		super(32, 32, MaterialLayer.MINE_LAYER);
	}
	
	@Override
	public void build() {
		newMaterial("contactMine", EntityStates.NONE, null, false);
		newMaterial("mineActive", EntityStates.ATTACK, null, true);
		
		this.width = 128;
		this.height= 128;
		newMaterial("explosion", EntityStates.EXPLODING, null, true);

	}

}
