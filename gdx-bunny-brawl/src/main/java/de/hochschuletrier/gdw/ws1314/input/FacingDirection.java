package de.hochschuletrier.gdw.ws1314.input;

import com.badlogic.gdx.math.Vector2;

/**
 * 
 * @author ElFapo
 *
 */

public enum FacingDirection 
{
	NONE(0.0f, 0.0f, 0),
	
	LEFT(-1.0f, 0.0f, 1),
	DOWN(0.0f, 1.0f, 2),
	RIGHT(1.0f, 0.0f, 3),
	UP(0.0f, -1.0f, 4),
	
	DOWN_LEFT(1.0f / -(float)Math.sqrt(2.0f), 1.0f / (float)Math.sqrt(2.0f), 5),
	DOWN_RIGHT(1.0f / (float)Math.sqrt(2.0f), 1.0f / (float)Math.sqrt(2.0f), 6),
	UP_LEFT(1.0f / -(float)Math.sqrt(2.0f), 1.0f / -(float)Math.sqrt(2.0f), 7),
	UP_RIGHT(1.0f / (float)Math.sqrt(2.0f), 1.0f / -(float)Math.sqrt(2.0f), 8);
	
	private final Vector2 	direction;
	private final float	  	angle;
	private final int	 	index;
	
	private FacingDirection(float x, float y, int i)
	{
		direction = new Vector2(x, y);
		index = i;
		angle = direction.x == 0.0f && 
				direction.y == 0.0f ? 0 //180deg
						: direction.getAngleRad();
	}
	
	public Vector2 getDirectionVector()
	{
		return direction;
	}
	
	public float getAngle()
	{
		return angle;
	}
	
	public int getIndex()
	{
		return index;
	}
}
