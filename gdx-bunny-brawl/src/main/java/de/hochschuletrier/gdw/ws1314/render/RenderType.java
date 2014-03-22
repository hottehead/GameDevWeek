package de.hochschuletrier.gdw.ws1314.render;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;

public class RenderType {
	public EntityType entityType;
	public TeamColor teamColor;
	boolean requiresColor;
	
	protected RenderType() {
		entityType = EntityType.None;
		this.teamColor = TeamColor.BOTH;
		requiresColor = false;
	}
	
	protected void setType(EntityType entityType, TeamColor teamColor) {
		this.entityType = entityType;
		this.teamColor = teamColor;
		requiresColor = teamColor!=null?true:false;
	}
	
	public RenderType(EntityType entityType, TeamColor facing) {
		setType(entityType, facing);
	}
	
	public RenderType(EntityType type) {
		this(type, null);
	}
	
	@Override
	public int hashCode() {
		return entityType.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		RenderType otherState = (RenderType)obj;
		if(this.hashCode() != otherState.hashCode()) // nicht gleicher state
			return false;
		if(!otherState.requiresColor && !this.requiresColor) { //gleicher state, beide brauchen kein facing -> gleiches objekt 
			return true;
		}
		if(this.teamColor == otherState.teamColor)
			return true;
		return false;
	}
	
}
