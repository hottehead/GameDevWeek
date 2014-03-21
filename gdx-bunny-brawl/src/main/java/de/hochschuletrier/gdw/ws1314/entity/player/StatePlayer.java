package de.hochschuletrier.gdw.ws1314.entity.player;

import de.hochschuletrier.gdw.ws1314.state.IStateListener;
import de.hochschuletrier.gdw.ws1314.state.State;

/**
 * Created by Jerry on 21.03.14.
 */
public abstract class StatePlayer extends State {

	protected StatePlayer(IStateListener owner) {
		super(owner);
	}

	public abstract PlayerStates getCurrentState();
}
