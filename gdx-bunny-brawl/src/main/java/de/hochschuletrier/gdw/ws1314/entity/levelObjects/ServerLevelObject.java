package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;

/**
 * 
 * @author yannick
 * 
 */
public abstract class ServerLevelObject extends ServerEntity
{
	protected boolean isVisible;

	@Override
	public void enable()
	{
		this.isVisible = true;
	}

	@Override
	public void disable()
	{
		this.isVisible = false;
	}

	@Override
	public void initialize()
	{
		this.isVisible = true;
	}

	@Override
	public void update(float deltaTime)
	{
	}

    @Override
    public void reset()
    {
        physicsBody.setPosition(new Vector2(properties.getFloat("x"), properties.getFloat("y")));
    }



	public void setVisibility(boolean b)
	{
		this.isVisible = b;
	}

	public boolean getVisibility()
	{
		return this.isVisible;
	}
}
