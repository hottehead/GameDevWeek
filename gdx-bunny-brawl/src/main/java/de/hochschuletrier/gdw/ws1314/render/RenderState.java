package de.hochschuletrier.gdw.ws1314.render;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;

public class RenderState {
	public static RenderState NONE = new RenderState(EntityStates.NONE);	
	public EntityStates state;
	public FacingDirection facing;
	
	boolean requiresFacing;
	
	protected RenderState() {
		state = EntityStates.NONE;
		facing = null;
		requiresFacing = false;
	}
	
	protected void setState(EntityStates state, FacingDirection facing) {
		this.state = state;
		this.facing = facing;
		requiresFacing = facing==null?false:true;
	}
	
	public RenderState(EntityStates state, FacingDirection facing) {
		setState(state, facing);
	}
	
	public RenderState(EntityStates state) {
		this(state, null);
	}
	
	@Override
	public int hashCode() {
		return state.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		RenderState otherState = (RenderState)obj;
		if(this.hashCode() != otherState.hashCode()) // nicht gleicher state
			return false;
		if(!otherState.requiresFacing && !this.requiresFacing) { //gleicher state, beide brauchen kein facing -> gleiches objekt 
			return true;
		}
		if(this.facing == otherState.facing)
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return state!=null?state.toString():"null" + " : " + facing!=null?facing.toString():"none";//(state.name() + ":"+facing!=null?facing.name():"none"); 
	}
}
