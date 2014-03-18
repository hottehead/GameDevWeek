package de.hochschuletrier.gdw.ws1314.entity.player;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.ws1314.basic.PlayerInfo;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;

public class ServerPlayer extends ServerEntity 
{
	private PlayerInfo	playerInfo;
	private PlayerKit 	playerKit;
	private TeamColor	teamColor;
	
	private float 		firstAttackCooldown;
	private float 		secondAttackCooldown;
	
	private float		firstAttackTimer;
	private float		secondAttackTimer;
	
	private boolean		firstAttackFired;
	private boolean		secondAttackFired;
	
	private float		currentVelocity;
	private float		currentHealth;
	private float		currentArmor;
	
	private int 		currentEggCount;
	
	FacingDirection 	direction;
	
	public ServerPlayer()
	{
		super();
		
		setPlayerKit(PlayerKit.NOOB);
		firstAttackTimer = 0.0f;
		firstAttackFired = false;
		secondAttackTimer = 0.0f;
		secondAttackFired = false;
		currentEggCount = 0;
		
	}
	
	@Override
	public void enable() {}

	@Override
	public void disable() {}

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
		firstAttackCooldown = kit.getFirstAttackCooldown();
		secondAttackCooldown = kit.getSecondAttackCooldown();
		currentHealth = kit.getBaseHealth();
		currentArmor = kit.getBaseArmor();
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
		direction = dir;
		
		// TODO send
	}
	
	public void moveEnd(FacingDirection dir)
	{
		direction = dir;
		
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
		// TODO Place egg on map
		currentEggCount--;
		if (currentEggCount < 0)
			currentEggCount = 0;
	}

	@Override
	public void beginContact(Contact contact) 
	{
		// TODO Handle all possible collision types: damage, death, physical, egg collected...
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
