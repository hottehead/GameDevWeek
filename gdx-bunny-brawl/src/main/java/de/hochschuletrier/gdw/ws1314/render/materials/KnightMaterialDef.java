package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;

public class KnightMaterialDef extends MaterialDefinition {

	String teamColorStr = null;
	
	public KnightMaterialDef(TeamColor color) {
		super(110, 110, MaterialLayer.PLAYER_LAYER);
		switch(color) {
		case BLACK:
			teamColorStr = "Black";
			break;
		default:
			teamColorStr = "White";
			break;
		}
	}
	
	@Override
	public void build() {
		newMaterial("knight"+teamColorStr+"IdleDown", EntityStates.IDLE, FacingDirection.DOWN, true);
		newMaterial("knight"+teamColorStr+"IdleUp", EntityStates.IDLE, FacingDirection.UP, true);
		newMaterial("knight"+teamColorStr+"IdleLeft", EntityStates.IDLE, FacingDirection.LEFT, true);
		newMaterial("knight"+teamColorStr+"IdleLeft", EntityStates.IDLE, FacingDirection.UP_LEFT, true);
		newMaterial("knight"+teamColorStr+"IdleLeft", EntityStates.IDLE, FacingDirection.DOWN_LEFT, true);
		newMaterial("knight"+teamColorStr+"IdleRight", EntityStates.IDLE, FacingDirection.RIGHT, true);
		newMaterial("knight"+teamColorStr+"IdleRight", EntityStates.IDLE, FacingDirection.UP_RIGHT, true);
		newMaterial("knight"+teamColorStr+"IdleRight", EntityStates.IDLE, FacingDirection.DOWN_RIGHT, true);
		
		newMaterial("knight"+teamColorStr+"WalkDown", EntityStates.WALKING, FacingDirection.DOWN, true);
		newMaterial("knight"+teamColorStr+"WalkUp", EntityStates.WALKING, FacingDirection.UP, true);
		newMaterial("knight"+teamColorStr+"WalkLeft", EntityStates.WALKING, FacingDirection.LEFT, true);
		newMaterial("knight"+teamColorStr+"WalkLeft", EntityStates.WALKING, FacingDirection.DOWN_LEFT, true);
		newMaterial("knight"+teamColorStr+"WalkLeft", EntityStates.WALKING, FacingDirection.UP_LEFT, true);
		newMaterial("knight"+teamColorStr+"WalkRight", EntityStates.WALKING, FacingDirection.RIGHT, true);
		newMaterial("knight"+teamColorStr+"WalkRight", EntityStates.WALKING, FacingDirection.DOWN_RIGHT, true);
		newMaterial("knight"+teamColorStr+"WalkRight", EntityStates.WALKING, FacingDirection.UP_RIGHT, true);
		
		newMaterial("knight"+teamColorStr+"AttackDown", EntityStates.ATTACK, FacingDirection.DOWN, true);
		newMaterial("knight"+teamColorStr+"AttackUp", EntityStates.ATTACK, FacingDirection.UP, true);
		newMaterial("knight"+teamColorStr+"AttackLeft", EntityStates.ATTACK, FacingDirection.LEFT, true);
		newMaterial("knight"+teamColorStr+"AttackLeft", EntityStates.ATTACK, FacingDirection.DOWN_LEFT, true);
		newMaterial("knight"+teamColorStr+"AttackLeft", EntityStates.ATTACK, FacingDirection.UP_LEFT, true);
		newMaterial("knight"+teamColorStr+"AttackRight", EntityStates.ATTACK, FacingDirection.RIGHT, true);
		newMaterial("knight"+teamColorStr+"AttackRight", EntityStates.ATTACK, FacingDirection.DOWN_RIGHT, true);
		newMaterial("knight"+teamColorStr+"AttackRight", EntityStates.ATTACK, FacingDirection.UP_RIGHT, true);
	}

}
