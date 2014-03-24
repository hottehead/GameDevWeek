package de.hochschuletrier.gdw.ws1314.ai.pathfinding;

import com.badlogic.gdx.math.Vector2;

public class PathNode{
	Vector2 position;
	PathNode parent;
	int costs;
	
	public PathNode(Vector2 position, PathNode parent){
		this.position = position;
		this.parent = parent;
		this.costs = parent.getCosts() + 1;
	}
	public Vector2 getPosition(){
		return position;
	}
	public void setPosition(Vector2 position){
		this.position = position;
	}
	public PathNode getParent(){
		return parent;
	}
	public void setParent(PathNode parent){
		this.parent = parent;
	}
	public int getCosts() {
		return costs;
	}
	
}
