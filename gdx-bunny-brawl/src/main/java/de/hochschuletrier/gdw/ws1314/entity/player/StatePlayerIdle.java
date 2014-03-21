package de.hochschuletrier.gdw.ws1314.entity.player;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.state.IStateListener;

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
	public EntityStates getCurrentState(){return EntityStates.IDLE;}

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
