package de.hochschuletrier.gdw.ws1314.entity.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.basic.PlayerInfo;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.input.PlayerIntention;
import de.hochschuletrier.gdw.ws1314.state.State;
import de.hochschuletrier.gdw.ws1314.state.IStateListener;

/**
 * 
 * @author ElFapo
 * ASK BEFORE MODIFYING, OR I'LL MOST CERTAINLY TAKE A SHIT ON YOUR HEAD!
 */

public class ServerPlayer extends ServerEntity implements IStateListener
{
    private static final Logger logger = LoggerFactory.getLogger(ServerPlayer.class);


    private PlayerInfo	playerInfo;
    private PlayerKit 	playerKit;
    private TeamColor	teamColor;
    
    private float 		firstAttackCooldown;
    private float 		secondAttackCooldown;
    
    private float		currentHealth;
    private float		currentArmor;
    
    private int 		currentEggCount;
    
    private StatePlayerWaiting 	 attackState;
    private StatePlayerIdle		 idleState;
    private StatePlayerWaiting 	 knockbackState;
    private StatePlayerWalking	 walkingState;
    
    private State				 currentState;
    
    FacingDirection 	facingDirection;
    FacingDirection		desiredDirection;
    boolean				movingUp;
    boolean				movingDown;
    boolean				movingLeft;
    boolean				movingRight;
    
    public ServerPlayer()
    {
    	super();
    	
    	setPlayerKit(PlayerKit.NOOB);
    	currentEggCount = 0;
    	
    	attackState = new StatePlayerWaiting(this);
    	idleState = new StatePlayerIdle(this);
    	knockbackState = new StatePlayerWaiting(this);
    	walkingState = new StatePlayerWalking(this);
    	switchToState(idleState);
    }
    
    public void enable() {}
    public void disable() {}
    public void dispose() {}
    public void initialize() {}
    
    @Override
    public void update(float deltaTime) 
    {
    	currentState.update(deltaTime);
    	
    	// TODO Handle physics body velocity etc. Physics body shall not be faster than direction * playerKit.getMaxVelocity()
    }

    //NOT FINAL! CHANGE AS NEEDED
    Vector2 dir = new Vector2(0,0);
    public void doAction(PlayerIntention intent)
    {
        logger.info("Hey I got a Intention: {}",intent.name());

        switch (intent){
            case MOVE_UP_ON:
                movingUp = true;
                break;
            case MOVE_DOWN_ON:
                movingDown = true;
                break;
            case MOVE_LEFT_ON:
                movingLeft = true;
                break;
            case MOVE_RIGHT_ON:
                movingRight = true;
                break;
            case MOVE_UP_OFF:
                movingUp = false;
                break;
            case MOVE_DOWN_OFF:
                movingDown = false;
                break;
            case MOVE_LEFT_OFF:
                movingLeft = false;
                break;
            case MOVE_RIGHT_OFF:
                movingRight = false;
                break;
            case ATTACK_1:
        		attackState.setWaitTime(firstAttackCooldown);
            	if (currentState == idleState || currentState == walkingState)
            	{
            		attackState.setWaitFinishedState(currentState);
            		switchToState(attackState);
            	}
                break;
            case ATTACK_2:
        		attackState.setWaitTime(secondAttackCooldown);
        		if (currentState == idleState || currentState == walkingState)
            	{
            		attackState.setWaitFinishedState(currentState);
            		switchToState(attackState);
            	}
                break;
            case DROP_EGG:
        		if (currentState == idleState || currentState == walkingState)
        			dropEgg();
        }
        
        desiredDirection = FacingDirection.NONE;
        if (movingUp)
        {
        	if (movingLeft)
        		desiredDirection = FacingDirection.UP_LEFT;
        	else if (movingRight)
        		desiredDirection = FacingDirection.UP_RIGHT;
        	else
        		desiredDirection = FacingDirection.UP;
        }
        else if (movingDown)
        {
        	if (movingLeft)
        		desiredDirection = FacingDirection.DOWN_LEFT;
        	else if (movingRight)
        		desiredDirection = FacingDirection.DOWN_RIGHT;
        	else
        		desiredDirection = FacingDirection.DOWN;
        }
        else if (movingLeft)
        	desiredDirection = FacingDirection.LEFT;
        else if (movingRight)
        	desiredDirection = FacingDirection.RIGHT;    	
        
        // Player intended movement
        if (desiredDirection != FacingDirection.NONE)
        {
        	walkingState.setMovingDirection(desiredDirection);
            if (currentState == idleState || currentState == walkingState)
            	switchToState(walkingState);

        	attackState.setWaitFinishedState(walkingState);
        	knockbackState.setWaitFinishedState(walkingState);
        }
        // Not intended movement
        else
        {
        	if (currentState == walkingState)
        		switchToState(idleState);

        	attackState.setWaitFinishedState(idleState);
        	knockbackState.setWaitFinishedState(idleState);
        }
    }

    protected void moveBegin(FacingDirection dir)
    {
    	facingDirection = desiredDirection;
    	
    	// TODO 
    	// Damp old impulse
    	// acceleration impulse to physics body
    	// Use direction vector and impulse constant to create the impulse vector
    	// Check PlayerKit for impulse constant
    	
    	/*physicsBody.applyImpulse(dir.getDirectionVector().x * playerKit.getMaxVelocity(),
				 				 dir.getDirectionVector().y * playerKit.getMaxVelocity());*/
    	
    }
    
    protected void moveEnd()
    {
    	// TODO brake impulse to physics body
    	// Use direction vector and impulse constant to create the impulse vector
    	// Check PlayerKit for impulse constant
    	physicsBody.setLinearDamping(1);
    }
    
    private void doFirstAttack()
    {
    	playerKit.doFirstAttack(this);
    }
    
    private void doSecondAttack()
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
    
    public FacingDirection  getFacingDirection()	{ return facingDirection; }
    public int				getCurrentEggCount()	{ return currentEggCount; }
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
		//FIXME: player position muss noch irgendwo hinterlegt sein
		PhysixBody body = new PhysixBodyDef(BodyType.DynamicBody, manager)
							  .position(new Vector2()).fixedRotation(false).create();
		body.createFixture(new PhysixFixtureDef(manager).density(0.5f)
				.friction(0.5f).restitution(0.4f).shapeBox(100,100));
		body.setGravityScale(0);
		body.addContactListener(this);
		setPhysicsBody(body);
	}

	@Override
	public void switchToState(State state)
	{
		currentState.exit();
		currentState = state;
		currentState.init();
	}
	
	protected void applyKnockback()
	{
		switchToState(knockbackState);
		
		// TODO Calculate KnockbackImpulse
	}
}
