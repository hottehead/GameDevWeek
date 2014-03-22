package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;

public class KnightMaterialDef extends MaterialDefinition {

	public KnightMaterialDef() {
		super(110, 110, MaterialLayer.PLAYER_LAYER);
	}
	
	@Override
	public void build() {
		newMaterial("knightWhiteIdleDown", EntityStates.IDLE, FacingDirection.DOWN, true);
		newMaterial("knightWhiteIdleUp", EntityStates.IDLE, FacingDirection.UP, true);
		newMaterial("knightWhiteIdleLeft", EntityStates.IDLE, FacingDirection.LEFT, true);
		newMaterial("knightWhiteIdleRight", EntityStates.IDLE, FacingDirection.RIGHT, true);
		
		newMaterial("knightWhiteWalkDown", EntityStates.WALKING, FacingDirection.DOWN, true);
		newMaterial("knightWhiteWalkUp", EntityStates.WALKING, FacingDirection.UP, true);
		newMaterial("knightWhiteWalkLeft", EntityStates.WALKING, FacingDirection.LEFT, true);
		newMaterial("knightWhiteWalkLeft", EntityStates.WALKING, FacingDirection.DOWN_LEFT, true);
		newMaterial("knightWhiteWalkLeft", EntityStates.WALKING, FacingDirection.UP_LEFT, true);
		newMaterial("knightWhiteWalkRight", EntityStates.WALKING, FacingDirection.RIGHT, true);
		newMaterial("knightWhiteWalkRight", EntityStates.WALKING, FacingDirection.DOWN_RIGHT, true);
		newMaterial("knightWhiteWalkRight", EntityStates.WALKING, FacingDirection.UP_RIGHT, true);
		
		newMaterial("knightWhiteAttackDown", EntityStates.ATTACK, FacingDirection.DOWN, true);
		newMaterial("knightWhiteAttackUp", EntityStates.ATTACK, FacingDirection.UP, true);
		newMaterial("knightWhiteAttackLeft", EntityStates.ATTACK, FacingDirection.LEFT, true);
		newMaterial("knightWhiteAttackLeft", EntityStates.ATTACK, FacingDirection.DOWN_LEFT, true);
		newMaterial("knightWhiteAttackLeft", EntityStates.ATTACK, FacingDirection.UP_LEFT, true);
		newMaterial("knightWhiteAttackRight", EntityStates.ATTACK, FacingDirection.RIGHT, true);
		newMaterial("knightWhiteAttackRight", EntityStates.ATTACK, FacingDirection.DOWN_RIGHT, true);
		newMaterial("knightWhiteAttackRight", EntityStates.ATTACK, FacingDirection.UP_RIGHT, true);
	}

}
