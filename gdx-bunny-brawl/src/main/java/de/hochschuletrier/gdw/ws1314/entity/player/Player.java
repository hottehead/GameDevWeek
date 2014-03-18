package de.hochschuletrier.gdw.ws1314.entity.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;

public class Player extends ServerEntity 
{
	private PlayerKit 	playerKit;
	private TeamColor	teamColor;
	
	private float 		firstAttackCooldown;
	private float 		secondAttackCooldown;
	
	private float		firstAttackTimer;
	private float		secondAttackTimer;
	
	private float		currentVelocity;
	private float		currentHealth;
	private float		currentArmor;
	
	private int 		currentEggCount;
	
	Vector2 			direction;
	
	public Player()
	{
		super();
		
		playerKit = PlayerKit.NOOB;
	}
	
	@Override
	public void enable() 
	{
	}

	@Override
	public void disable() 
	{
	}

	@Override
	public void dispose() 
	{
	}

	@Override
	public void initialize() 
	{
	}
	
	public void setPlayerKit(PlayerKit kit)
	{
		playerKit = kit;
	}
	
	public PlayerKit getPlayerKit()
	{
		return playerKit;
	}

	@Override
	public void update(float deltaTime) 
	{
	}

	public void moveBegin(FacingDirection dir)
	{
		
	}
	
	public void moveEnd(FacingDirection dir)
	{
		
	}
	
	public void doFirstAttack()
	{
		playerKit.doFirstAttack(this);
	}
	
	public void doSecondAttack()
	{
		playerKit.doSecondAttack(this);
	}
	
	public void dropEgg()
	{
		
	}

	@Override
	public void beginContact(Contact contact) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void endContact(Contact contact) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{
		// TODO Auto-generated method stub
		
	}
}
