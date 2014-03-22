package de.hochschuletrier.gdw.ws1314.entity.player;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.state.IStateListener;

/**
 * Created by Jerry on 21.03.14.
 */
public class StatePlayerAttack extends StatePlayerWaiting {
	public StatePlayerAttack(IStateListener owner) {
		super(owner);
	}
	@Override
	public EntityStates getCurrentState(){return EntityStates.ATTACK;}
}
