package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;

public class TankMaterialDef extends MaterialDefinition {
	private String colorStr = null; 
	
	public TankMaterialDef(TeamColor color) {
		super(75, 99, MaterialLayer.PLAYER_LAYER);
		switch(color) {
		case BLACK:
			colorStr = "Black";
			break;
		default:
			colorStr = "White";
			break;
		}
	}
	
	@Override
	public void build() {
		newMaterial("tank"+colorStr+"IdleDown", EntityStates.IDLE, FacingDirection.DOWN, true);
		newMaterial("tank"+colorStr+"IdleUp", EntityStates.IDLE, FacingDirection.UP, true);
		newMaterial("tank"+colorStr+"IdleLeft", EntityStates.IDLE, FacingDirection.LEFT, true);
		newMaterial("tank"+colorStr+"IdleLeft", EntityStates.IDLE, FacingDirection.UP_LEFT, true);
		newMaterial("tank"+colorStr+"IdleLeft", EntityStates.IDLE, FacingDirection.DOWN_LEFT, true);
		newMaterial("tank"+colorStr+"IdleRight", EntityStates.IDLE, FacingDirection.RIGHT, true);
		newMaterial("tank"+colorStr+"IdleRight", EntityStates.IDLE, FacingDirection.UP_RIGHT, true);
		newMaterial("tank"+colorStr+"IdleRight", EntityStates.IDLE, FacingDirection.DOWN_RIGHT, true);
		
		newMaterial("tank"+colorStr+"WalkDown", EntityStates.WALKING, FacingDirection.DOWN, true);
		newMaterial("tank"+colorStr+"WalkUp", EntityStates.WALKING, FacingDirection.UP, true);
		newMaterial("tank"+colorStr+"WalkLeft", EntityStates.WALKING, FacingDirection.LEFT, true);
		newMaterial("tank"+colorStr+"WalkLeft", EntityStates.WALKING, FacingDirection.DOWN_LEFT, true);
		newMaterial("tank"+colorStr+"WalkLeft", EntityStates.WALKING, FacingDirection.UP_LEFT, true);
		newMaterial("tank"+colorStr+"WalkRight", EntityStates.WALKING, FacingDirection.RIGHT, true);
		newMaterial("tank"+colorStr+"WalkRight", EntityStates.WALKING, FacingDirection.DOWN_RIGHT, true);
		newMaterial("tank"+colorStr+"WalkRight", EntityStates.WALKING, FacingDirection.UP_RIGHT, true);
		
		newMaterial("tank"+colorStr+"AttackDown", EntityStates.ATTACK, FacingDirection.DOWN, true);
		newMaterial("tank"+colorStr+"AttackUp", EntityStates.ATTACK, FacingDirection.UP, true);
		newMaterial("tank"+colorStr+"AttackLeft", EntityStates.ATTACK, FacingDirection.LEFT, true);
		newMaterial("tank"+colorStr+"AttackLeft", EntityStates.ATTACK, FacingDirection.DOWN_LEFT, true);
		newMaterial("tank"+colorStr+"AttackLeft", EntityStates.ATTACK, FacingDirection.UP_LEFT, true);
		newMaterial("tank"+colorStr+"AttackRight", EntityStates.ATTACK, FacingDirection.RIGHT, true);
		newMaterial("tank"+colorStr+"AttackRight", EntityStates.ATTACK, FacingDirection.DOWN_RIGHT, true);
		newMaterial("tank"+colorStr+"AttackRight", EntityStates.ATTACK, FacingDirection.UP_RIGHT, true);
	}

}
