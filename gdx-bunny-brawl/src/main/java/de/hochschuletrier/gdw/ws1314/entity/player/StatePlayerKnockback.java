package de.hochschuletrier.gdw.ws1314.entity.player;

import de.hochschuletrier.gdw.ws1314.state.IStateListener;

/**
 * Created by Jerry on 21.03.14.
 */
public class StatePlayerKnockback extends StatePlayerWaiting {
	public StatePlayerKnockback(IStateListener owner) {
		super(owner);
	}

	@Override
	public PlayerStates getCurrentState(){return PlayerStates.KNOCKBACK;}
}
