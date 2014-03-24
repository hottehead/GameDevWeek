package de.hochschuletrier.gdw.ws1314.ai.pathfinding;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;

public class MapWalker{
	Logger logger = LoggerFactory.getLogger(MapWalker.class);
	ServerEntityManager sem; 
	public MapWalker(){
		sem = ServerEntityManager.getInstance();
	}
	List<Vector2> collidingPoints = new ArrayList<>();
	List<Vector2> nonCollidingPoints = new ArrayList<>();
	List<Vector2> eggs = new ArrayList<>();
	List<Vector2> home = new ArrayList<>();
	
	public void WalkMap(){
		int size = sem.getListSize();
		for(int i = 0; i < size; i++){
			ServerEntity current = sem.getListEntity(i);
			EntityType type = current.getEntityType();
			logger.info(current.getPosition().toString());
			switch(type){
			case ContactMine:
			case AbyssZone:
			case WaterZone:
				collidingPoints.add(current.getPosition());
				break;
			case Ei:
				eggs.add(current.getPosition());
				break;
			case StartZone:
				home.add(current.getPosition());
				break;
			default:
				nonCollidingPoints.add(current.getPosition());
				break;
			}
		}
	}
	PathList openList = new PathList();
	
	public void ShortestPath(Vector2 start, Vector2 target){
		PathNode root = new PathNode(target, null);
		openList.add(root);
	}
	public void expandNode(PathNode node){
		Vector2 position = node.getPosition();
		for(int i = 0; i < nonCollidingPoints.size(); i++){
			Vector2 current = nonCollidingPoints.get(i);
			if(current.dst(position) <= 58){
				
			}
				
		}
	}

}