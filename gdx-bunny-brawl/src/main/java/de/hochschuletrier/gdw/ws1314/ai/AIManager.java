package de.hochschuletrier.gdw.ws1314.ai;
import java.util.ArrayList;
import java.util.List;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.BehaviourManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;


public class AIManager{
	private static AIManager instance = null;
	private List<BehaviourManager> ai_players = new ArrayList<>();
	private Blackboard globalBlackboard = new Blackboard();
	
	public AIManager(){
		globalBlackboard.setSem(ServerEntityManager.getInstance());
	}
	public AIManager getInstance(){
		if(instance == null)
			instance = new AIManager();
		return instance;
	}
	
	public void CreateAI(long entityid, EntityType clazz){
		BehaviourManager tmp = new BehaviourManager(globalBlackboard);
		Behaviour b = null;
		switch(clazz){
			case Hunter:
				b = new MyBehaviour();
				break;
			case Knight:
				break;
			case Tank:
				break;
			default:
				break;
		}
		b.setLocalBlackboard(entityid);
		tmp.addBehaviour(b);
		tmp.setGlobalBlackboard(globalBlackboard);
		ai_players.add(tmp);
		tmp.activate();
	}
	public void deactivateAI(){
		for(BehaviourManager bm : ai_players){
			bm.deactivate();
		}
	}
	
	public void update(float delta){
		for(BehaviourManager bm : ai_players){
			bm.update(delta);
		}
	}
}
