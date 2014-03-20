package de.hochschuletrier.gdw.ws1314.entity.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Body;
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
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ServerBridge;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ServerEgg;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.AttackShootArrow;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ServerProjectile;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.input.PlayerIntention;
import de.hochschuletrier.gdw.ws1314.state.State;
import de.hochschuletrier.gdw.ws1314.state.IStateListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ElFapo
 * ASK BEFORE MODIFYING, OR I'LL MOST CERTAINLY TAKE A SHIT ON YOUR HEAD!
 * -I'D REALLY LIKE TO SEE THIS xD
 */

public class ServerPlayer extends ServerEntity implements IStateListener
{
    private static final Logger logger = LoggerFactory.getLogger(ServerPlayer.class);


	private final float FRICTION = 0;
	private final float	BRAKING = 5.0f;


	private final float RESTITUTION = 0;
	private final float	KNOCKBACK_TIME = 0.8f;
	private final float ATTACK_TIME = 0.18f;


    private PlayerInfo	playerInfo;
    private PlayerKit 	playerKit;
    private TeamColor	teamColor;
    
    private float		currentHealth;
    private float		currentArmor;
    
    private int 		currentEggCount;
    
    private StatePlayerWaiting 	 attackState;
    private StatePlayerIdle		 idleState;
    private StatePlayerWaiting 	 knockbackState;
    private StatePlayerWalking	 walkingState;
    
    private State				 currentState;
    
    private float				 attackCooldown;
    private float				 attackCooldownTimer;
    private boolean				 attackAvailable;
    
    FacingDirection 	facingDirection;
    FacingDirection		desiredDirection;
    boolean				movingUp;
    boolean				movingDown;
    boolean				movingLeft;
    boolean				movingRight;
    
    public ServerPlayer()
    {
    	super();
    	
    	setPlayerKit(PlayerKit.HUNTER);
    	currentEggCount = 0;
    	
    	attackState = new StatePlayerWaiting(this);
    	idleState = new StatePlayerIdle(this);
    	knockbackState = new StatePlayerWaiting(this);
    	walkingState = new StatePlayerWalking(this);
    	currentState = idleState;
    	facingDirection = FacingDirection.DOWN;
    }
    
    public void enable() {}
    public void disable() {}
    public void dispose() {}
    public void initialize() {}
    
    @Override
    public void update(float deltaTime) 
    {
    	currentState.update(deltaTime);
    	
    	if (!attackAvailable)
    	{
        	attackCooldownTimer += deltaTime;
        	if (attackCooldownTimer > attackCooldown)
        	{
        		attackAvailable = true;
        	}
    	}
    }

    //NOT FINAL! CHANGE AS NEEDED
    Vector2 dir = new Vector2(0,0);
    public void doAction(PlayerIntention intent)
    {
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
            	if (!attackAvailable)
            		break;
        		attackState.setWaitTime(ATTACK_TIME);
            	if (currentState == idleState || currentState == walkingState)
            	{
            		attackCooldown = playerKit.getFirstAttackCooldown();
            		attackCooldownTimer = 0.0f;
            		attackAvailable = false;
            		attackState.setWaitFinishedState(currentState);
            		switchToState(attackState);
            		doFirstAttack();
            	}
                break;
            case ATTACK_2:
            	if (!attackAvailable)
            		break;
        		attackState.setWaitTime(ATTACK_TIME);
        		if (currentState == idleState || currentState == walkingState)
            	{
            		attackCooldown = playerKit.getSecondAttackCooldown();
            		attackCooldownTimer = 0.0f;
            		attackAvailable = false;
            		attackState.setWaitFinishedState(currentState);
            		switchToState(attackState);
            		doSecondAttack();
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

    	moveEnd();
    	physicsBody.applyImpulse(dir.getDirectionVector().x * playerKit.getMaxVelocity(),
		  		 				 dir.getDirectionVector().y * playerKit.getMaxVelocity());
    	//System.out.println(dir.getDirectionVector().x + " " + dir.getDirectionVector().y);
    	moveEnd();

    }
    
    protected void moveEnd()
    {
    	// TODO brake impulse to physics body
    	// Use direction vector and impulse constant to create the impulse vector
    	// Check PlayerKit for impulse constant
    	//physicsBody.setLinearVelocity(FacingDirection.NONE.getDirectionVector());
    	physicsBody.setLinearDamping(BRAKING);
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
    public void beginContact(Contact contact) 	{
    	 ServerEntity otherEntity = this.identifyContactFixtures(contact);
         
         switch(otherEntity.getEntityType()) {
             case Tank:
             case Hunter:
             case Knight:
             case Noob:
                 // Comment by ElFapo:
            	 // Hier nur physikalische Kontakte berücksichtigen. Waffenkontakte werden wie bei Projektilen behandelt.
            	 //
                 break;
             case Ei:			
            	 ServerEgg egg = (ServerEgg) otherEntity;
            	 System.out.println(this.getCurrentEggCount());
            	 if(this.currentEggCount < this.playerKit.getMaxEggCount()){
            		 ServerEntityManager.getInstance().removeEntity(otherEntity);
            		 this.currentEggCount++;
            	 }
            	 break;
             case Projectil: 
            	 ServerProjectile projectile = (ServerProjectile) otherEntity;
//            	 ServerPlayer hunter = (ServerPlayer) ServerEntityManager.getInstance().getEntityById(projectile.getID());
//            	 this.currentHealth -= AttackShootArrow.DAMAGE;
//            	  
//            	 if(this.currentHealth <= 0){
//            	  	 ServerEntityManager.getInstance().removeEntity(this);
//            	  }
            	 break;
             case Bridge:
            	 ServerBridge bridge = (ServerBridge) otherEntity;
            	 //ServerPlayer hunter = (ServerPlayer) ServerEntityManager.getInstance().getEntityById(projectile.getID());
            	 
            	/* if(){
            	  	 ServerEntityManager.getInstance().removeEntity(this);
            	  }*/
            	 break;
             case BridgeSwitch:	
            	 break;
             case Bush:			
            	 break;
             case SwordAttack:
            	 break;
             case ContactMine:
            	 break;
             case Carrot:
            	 this.setSpeedBuff(1.5f,10);
            	 break;
             case Spinach:
            	 break;
             case Clover:
            	 break;
             case WaterZone:
            	 break;
             case AbyssZone:
            	 break;
             case GrassZone:
            	 break;
             case PathZone:
            	 break;
             case StartZone:
            	 break;
             default:
            	 break;
                 
         }
    }
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
    
    public void setSpeedBuff(float factor, float timer){
    	
    }

	@Override
	public void initPhysics(PhysixManager manager)
	{
		// TODO Auto-generated method stub
		PhysixBody body = new PhysixBodyDef(BodyType.DynamicBody, manager)
							  .position(properties.getFloat("x"), properties.getFloat("y")).fixedRotation(false).create();
		body.createFixture(new PhysixFixtureDef(manager).density(0)
				.friction(FRICTION).restitution(RESTITUTION).shapeBox(32,32));

		body.setGravityScale(0);
		body.addContactListener(this);
		setPhysicsBody(body);
    	walkingState.setPhysixBody(physicsBody);
	}

	@Override
	public void switchToState(State state)
	{
		currentState.exit();
		currentState = state;
		currentState.init();
	}

    public void reset(){
        physicsBody.setPosition(new Vector2(properties.getFloat("x"), properties.getFloat("y")));
        currentHealth = playerKit.getBaseHealth();
        currentArmor = playerKit.getBaseArmor();
        facingDirection = FacingDirection.DOWN;
        		
        switchToState(idleState);
    }
    
    public void applyDamage(float amount)
    {
    	amount -= currentArmor;
    	if (amount < 0)
    		amount = 0;
    	currentHealth -= amount;
    	currentArmor -= amount;
    	
    	if (currentArmor < 0)
    		currentArmor = 0;
    	
    	if (currentHealth <= 0)
    		reset();
    }
	
	protected void applyKnockback()
	{
		knockbackState.setWaitTime(KNOCKBACK_TIME);
		switchToState(knockbackState);
		
		// TODO Calculate KnockbackImpulse
	}
}
