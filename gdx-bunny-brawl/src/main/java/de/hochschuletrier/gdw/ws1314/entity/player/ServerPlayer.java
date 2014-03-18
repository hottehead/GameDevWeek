package de.hochschuletrier.gdw.ws1314.entity.player;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.basic.PlayerInfo;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;

/**
 * 
 * @author ElFapo
 *
 */

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
    
    public void enable() {}
    public void disable() {}
    public void dispose() {}
    public void initialize() {}
    
    @Override
    public void update(float deltaTime) 
    {
    	if (firstAttackFired)
    	{
    		firstAttackTimer += deltaTime;
    		if (firstAttackTimer >= firstAttackCooldown)
    			firstAttackFired = false;
    	}
    	else if (secondAttackFired)
    	{
    		secondAttackTimer += deltaTime;
    		if (secondAttackTimer >= secondAttackCooldown)
    			secondAttackFired = false;
    	}
    	
    	// TODO Handle physics body velocity etc. Physics body shall not be faster than direction * playerKit.getMaxVelocity()
    }
    
    public void moveBegin(FacingDirection dir)
    {
    	direction = dir;
    	
    	// TODO acceleration impulse to physics body
    	// Use direction vector and impulse constant to create the impulse vector
    	// Check PlayerKit for impulse constant
    }
    
    public void moveEnd()
    {
    	// TODO brake impulse to physics body
    	// Use direction vector and impulse constant to create the impulse vector
    	// Check PlayerKit for impulse constant
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
    
    // TODO Handle all possible collision types: damage, death, physical, egg collected...
    public void beginContact(Contact contact) 	{}
    public void endContact(Contact contact) 	{}
    public void preSolve(Contact contact, Manifold oldManifold) {}
    public void postSolve(Contact contact, ContactImpulse impulse) {}
    
    public FacingDirection  getFacingDirection()	{ return direction; }
    public float			getCurrentEggCount()	{ return currentEggCount; }
    public float			getCurrentHealth()		{ return currentHealth; }
    public float			getCurrentArmor()		{ return currentArmor; }
    public PlayerInfo		getPlayerInfo()			{ return playerInfo; }
    public PlayerKit		getPlayerKit()			{ return playerKit; }
    public TeamColor		getTeamColor()			{ return teamColor; }
    public EntityType 		getEntityType()			{ return playerKit.getEntityType(); }
    
    public void setPlayerKit(PlayerKit kit)
    {
    	playerKit = kit;
    	firstAttackCooldown = kit.getFirstAttackCooldown();
    	secondAttackCooldown = kit.getSecondAttackCooldown();
    	currentHealth = kit.getBaseHealth();
    	currentArmor = kit.getBaseArmor();
    }
    
    public void setPlayerInfo(PlayerInfo info)
    {
    	playerInfo = info;
    }
    
    public void setTeamColor(TeamColor color)
    {
    	teamColor = color;
    }

	@Override
	public void initPhysics(PhysixManager manager)
	{
		// TODO Auto-generated method stub
		
	}
}
