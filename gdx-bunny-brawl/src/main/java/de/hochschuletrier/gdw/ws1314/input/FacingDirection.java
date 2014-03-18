package de.hochschuletrier.gdw.ws1314.input;

import com.badlogic.gdx.math.Vector2;

/**
 * 
 * @author ElFapo
 *
 */

public enum FacingDirection 
{
	LEFT(-1.0f, 0.0f),
	DOWN(0.0f, 1.0f),
	RIGHT(1.0f, 0.0f),
	UP(0.0f, -1.0f),
	
	DOWN_LEFT(-(float)Math.sqrt(2.0f), (float)Math.sqrt(2.0f)),
	DOWN_RIGHT((float)Math.sqrt(2.0f), (float)Math.sqrt(2.0f)),
	UP_LEFT(-(float)Math.sqrt(2.0f), -(float)Math.sqrt(2.0f)),
	UP_RIGHT((float)Math.sqrt(2.0f), -(float)Math.sqrt(2.0f));
	
	Vector2 direction;
	
	private FacingDirection(float x, float y)
	{
		direction = new Vector2(x, y);
	}
	
	public Vector2 getDirectionVector()
	{
		return direction;
	}
}