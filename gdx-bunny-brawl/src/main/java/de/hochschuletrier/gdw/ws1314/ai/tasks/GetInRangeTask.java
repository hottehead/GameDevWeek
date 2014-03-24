package de.hochschuletrier.gdw.ws1314.ai.tasks;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseTask;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.AttackShootArrow;

class GetInRangeTask extends BaseTask {
	
	float AttackRange = AttackShootArrow.ARROW_SPAWN_DISTANCE + AttackShootArrow.ARROW_FLIGHT_DISTANCE;
	ClientPlayer p;
	ClientEntity ce;
	
	public GetInRangeTask(BaseNode parent, ClientPlayer p, ClientEntity ce){
		super(parent);
		this.p = p;
		this.ce = ce;
	}

	@Override
	public State onRun(float delta){
		// TODO Auto-generated method stub
		if(getDistance() <= AttackRange)
			return State.SUCCESS;
		else {
			//Move To Enemy
			return State.RUNNING;
		}
		//return State.FAILURE;
	}

	@Override
	public void onActivate(){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeactivate(){
		// TODO Auto-generated method stub
		
	}
	
	private float getDistance(){
		Vector2 p_pos = p.getPosition();
		Vector2 e_pos = ce.getPosition();
		return p_pos.dst(e_pos);
	}
	
}
