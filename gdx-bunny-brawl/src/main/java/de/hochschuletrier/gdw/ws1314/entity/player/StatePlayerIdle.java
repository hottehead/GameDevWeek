package de.hochschuletrier.gdw.ws1314.entity.player;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.ws1314.state.IStateListener;
import de.hochschuletrier.gdw.ws1314.state.State;

/**
 * 
 * @author ElFapo
 *
 */
public class StatePlayerIdle extends StatePlayer
{
	public StatePlayerIdle(IStateListener owner)
	{
		super(owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PlayerStates getCurrentState(){return PlayerStates.IDLE;}

	@Override
	public void update(float dt)
	{
	}

	@Override
	public void init()
	{
	}

	@Override
	public void exit()
	{
	}

}
