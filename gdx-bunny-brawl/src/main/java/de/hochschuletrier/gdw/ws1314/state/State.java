package de.hochschuletrier.gdw.ws1314.state;

/**
 * 
 * @author ElFapo
 *
 */
public abstract class State
{
	IStateListener owner;
	public State(IStateListener owner)
	{
		this.owner = owner;
	}
	
	public abstract void update(float dt);
	public abstract void init();
	public abstract void exit();
	
	public IStateListener getOwner()	{ return owner; }
}
