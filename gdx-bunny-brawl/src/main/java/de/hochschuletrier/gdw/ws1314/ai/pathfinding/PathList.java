package de.hochschuletrier.gdw.ws1314.ai.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class PathList{
	List<PathNode> nodes = new ArrayList<>();
	
	public PathNode removeMin(){
		PathNode result = nodes.get(0);
		nodes.remove(0);
		return result;
	}
	
	public void add(PathNode node){
		int costs = node.getCosts();
		if(costs >= nodes.get(nodes.size()-1).getCosts()){
			nodes.add(node);
			return;
		}
			
		for(int i = 0; i < nodes.size(); i++){
			if(costs < nodes.get(i).getCosts()){
				nodes.add(i, node);
				break;
			}
		}
	}
}
