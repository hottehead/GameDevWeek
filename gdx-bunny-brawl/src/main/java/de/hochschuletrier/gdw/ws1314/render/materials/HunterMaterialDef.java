package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;

public class HunterMaterialDef extends MaterialDefinition {

	private String colorStr = null; 
	
	public HunterMaterialDef(TeamColor color) {
		super(110, 74, MaterialLayer.PLAYER_LAYER);
		switch(color) {
		case BLACK:
			colorStr = "Black";
			break;
		default:
			colorStr = "White";
			break;
		}
	}

	public void build() {
		newMaterial("hunter"+colorStr+"IdleDown", EntityStates.IDLE, FacingDirection.DOWN, true);
		newMaterial("hunter"+colorStr+"IdleUp", EntityStates.IDLE, FacingDirection.UP, true);
		newMaterial("hunter"+colorStr+"IdleLeft", EntityStates.IDLE, FacingDirection.LEFT, true);
		newMaterial("hunter"+colorStr+"IdleLeft", EntityStates.IDLE, FacingDirection.UP_LEFT, true);
		newMaterial("hunter"+colorStr+"IdleLeft", EntityStates.IDLE, FacingDirection.DOWN_LEFT, true);
		newMaterial("hunter"+colorStr+"IdleRight", EntityStates.IDLE, FacingDirection.RIGHT, true);
		newMaterial("hunter"+colorStr+"IdleRight", EntityStates.IDLE, FacingDirection.UP_RIGHT, true);
		newMaterial("hunter"+colorStr+"IdleRight", EntityStates.IDLE, FacingDirection.DOWN_RIGHT, true);
		
		newMaterial("hunter"+colorStr+"WalkDown", EntityStates.WALKING, FacingDirection.DOWN, true);
		newMaterial("hunter"+colorStr+"WalkUp", EntityStates.WALKING, FacingDirection.UP, true);
		newMaterial("hunter"+colorStr+"WalkLeft", EntityStates.WALKING, FacingDirection.LEFT, true);
		newMaterial("hunter"+colorStr+"WalkLeft", EntityStates.WALKING, FacingDirection.DOWN_LEFT, true);
		newMaterial("hunter"+colorStr+"WalkLeft", EntityStates.WALKING, FacingDirection.UP_LEFT, true);
		newMaterial("hunter"+colorStr+"WalkRight", EntityStates.WALKING, FacingDirection.RIGHT, true);
		newMaterial("hunter"+colorStr+"WalkRight", EntityStates.WALKING, FacingDirection.DOWN_RIGHT, true);
		newMaterial("hunter"+colorStr+"WalkRight", EntityStates.WALKING, FacingDirection.UP_RIGHT, true);
		
		newMaterial("hunter"+colorStr+"AttackDown", EntityStates.ATTACK, FacingDirection.DOWN, true);
		newMaterial("hunter"+colorStr+"AttackUp", EntityStates.ATTACK, FacingDirection.UP, true);
		newMaterial("hunter"+colorStr+"AttackLeft", EntityStates.ATTACK, FacingDirection.LEFT, true);
		newMaterial("hunter"+colorStr+"AttackLeft", EntityStates.ATTACK, FacingDirection.DOWN_LEFT, true);
		newMaterial("hunter"+colorStr+"AttackLeft", EntityStates.ATTACK, FacingDirection.UP_LEFT, true);
		newMaterial("hunter"+colorStr+"AttackRight", EntityStates.ATTACK, FacingDirection.RIGHT, true);
		newMaterial("hunter"+colorStr+"AttackRight", EntityStates.ATTACK, FacingDirection.DOWN_RIGHT, true);
		newMaterial("hunter"+colorStr+"AttackRight", EntityStates.ATTACK, FacingDirection.UP_RIGHT, true);
	}

}
