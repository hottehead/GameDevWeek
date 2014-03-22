package de.hochschuletrier.gdw.ws1314.render.materials;

import java.util.ArrayList;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.render.MaterialInfo;

public class HunterMaterials extends MaterialDefinition {

	public ArrayList<MaterialInfo> materials = new ArrayList<MaterialInfo>();

	public HunterMaterials() {
		super(110, 74, 1);
	}

	public void build() {
		newMaterial("hunterWhiteIdleDown", EntityStates.IDLE, FacingDirection.DOWN, true);
		newMaterial("hunterWhiteIdleUp", EntityStates.IDLE, FacingDirection.UP, true);
		newMaterial("hunterWhiteIdleLeft", EntityStates.IDLE, FacingDirection.LEFT, true);
		newMaterial("hunterWhiteIdleRight", EntityStates.IDLE, FacingDirection.RIGHT, true);
		
		newMaterial("hunterWhiteWalkDown", EntityStates.WALKING, FacingDirection.DOWN, true);
		newMaterial("hunterWhiteWalkUp", EntityStates.WALKING, FacingDirection.UP, true);
		newMaterial("hunterWhiteWalkLeft", EntityStates.WALKING, FacingDirection.LEFT, true);
		newMaterial("hunterWhiteWalkLeft", EntityStates.WALKING, FacingDirection.DOWN_LEFT, true);
		newMaterial("hunterWhiteWalkLeft", EntityStates.WALKING, FacingDirection.UP_LEFT, true);
		newMaterial("hunterWhiteWalkRight", EntityStates.WALKING, FacingDirection.RIGHT, true);
		newMaterial("hunterWhiteWalkRight", EntityStates.WALKING, FacingDirection.DOWN_RIGHT, true);
		newMaterial("hunterWhiteWalkRight", EntityStates.WALKING, FacingDirection.UP_RIGHT, true);
		
		newMaterial("hunterWhiteAttackDown", EntityStates.ATTACK, FacingDirection.DOWN, true);
		newMaterial("hunterWhiteAttackUp", EntityStates.ATTACK, FacingDirection.UP, true);
		newMaterial("hunterWhiteAttackLeft", EntityStates.ATTACK, FacingDirection.LEFT, true);
		newMaterial("hunterWhiteAttackLeft", EntityStates.ATTACK, FacingDirection.DOWN_LEFT, true);
		newMaterial("hunterWhiteAttackLeft", EntityStates.ATTACK, FacingDirection.UP_LEFT, true);
		newMaterial("hunterWhiteAttackRight", EntityStates.ATTACK, FacingDirection.RIGHT, true);
		newMaterial("hunterWhiteAttackRight", EntityStates.ATTACK, FacingDirection.DOWN_RIGHT, true);
		newMaterial("hunterWhiteAttackRight", EntityStates.ATTACK, FacingDirection.UP_RIGHT, true);
	}

}
