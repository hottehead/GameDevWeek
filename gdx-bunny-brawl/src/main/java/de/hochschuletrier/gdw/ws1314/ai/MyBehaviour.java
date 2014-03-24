package de.hochschuletrier.gdw.ws1314.ai;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseTask;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.RandomChoice;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.Sequence;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.AttackShootArrow;
import de.hochschuletrier.gdw.ws1314.input.PlayerIntention;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;

public class MyBehaviour extends Behaviour {

	public MyBehaviour() {
		Blackboard b = new Blackboard();
		b.setCm(ClientEntityManager.getInstance());
		b.setAggressive(true);
		b.setId(b.getCm().getPlayerEntityID());
		this.setGlobalBlackboard(b);
		setName("Stupid AI");
		BaseNode root = new Sequence(this);
		Writer w = new Writer(root, "D");
		new Writer(root, "E");
		new Writer(root, "M");
		new Writer(root, "O");
		RandomChoice random = new RandomChoice(root);
		new Writer(random, "!");
		new Writer(random, "?");
		setLooping(false);
	}


	class Blackboard {
		long id;
		//Object player;
		//Object[] enemies;
		ClientEntityManager cm;
		boolean isAggressive;
		public long getId(){
			return id;
		}
		public void setId(long id){
			this.id = id;
		}
		public ClientEntityManager getCm(){
			return cm;
		}
		public void setCm(ClientEntityManager cm){
			this.cm = cm;
		}
		public boolean isAggressive(){
			return isAggressive;
		}
		public void setAggressive(boolean isAggressive){
			this.isAggressive = isAggressive;
		}
		
	}
	class GetInRange extends BaseTask {
		
		float AttackRange = AttackShootArrow.ARROW_SPAWN_DISTANCE + AttackShootArrow.ARROW_FLIGHT_DISTANCE;
		ClientPlayer p;
		ClientEntity ce;
		
		public GetInRange(BaseNode parent, ClientPlayer p, ClientEntity ce){
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

	
	class Intention extends BaseTask {
		PlayerIntention intention;
		public Intention(BaseNode parent, PlayerIntention intention){
			super(parent);
			this.intention = intention;
		}

		@Override
		public State onRun(float delta){
			NetworkManager.getInstance().sendAction(intention);
			return State.SUCCESS;
		}

		@Override
		public void onActivate(){
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDeactivate(){
			// TODO Auto-generated method stub
			
		}
		
	}
	class Writer extends BaseTask {
		String string;
		public Writer(BaseNode parent, String string) {
			super(parent);
			this.string = string;
		}

		@Override
		public State onRun(float delta) {
			System.out.println(string);
			return State.SUCCESS;
		}

		@Override
		public void onActivate() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDeactivate() {
			// TODO Auto-generated method stub

		}

	}
}
