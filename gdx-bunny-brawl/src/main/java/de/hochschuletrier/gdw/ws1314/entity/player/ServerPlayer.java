package de.hochschuletrier.gdw.ws1314.entity.player;



import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.state.State;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ServerBridge;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ServerBridgeSwitch;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ServerCarrot;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ServerClover;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ServerEgg;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ServerHayBale;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ServerSpinach;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ServerProjectile;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ServerSwordAttack;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.input.PlayerIntention;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
import de.hochschuletrier.gdw.ws1314.state.IStateListener;
import de.hochschuletrier.gdw.ws1314.state.State;

/**
 * 
 * @author ElFapo
 * ASK BEFORE MODIFYING, OR I'LL MOST CERTAINLY TAKE A SHIT ON YOUR HEAD!
 * -I'D REALLY LIKE TO SEE THIS xD
 */

public class ServerPlayer extends ServerEntity implements IStateListener {
    private static final Logger logger = LoggerFactory.getLogger(ServerPlayer.class);


	public static final float	BRAKING = 5.0f;
	public static final float COLLISION_DAMPING = 10.0f;

	public static final float KNOCKBACK_IMPULSE = 100.0f;

	public static final float DENSITY = 0.0f;
    public static final float FRICTION = 0.0f;
	public static final float RESTITUTION = 0.0f;
	public static final float KNOCKBACK_TIME = 0.8f;
	public static final float ATTACK_TIME = 1.5f;

	public static final float WIDTH = 32.0f;
	public static final float HEIGHT = 32.0f;

	public static final float EGG_CARRY_SPEED_PENALTY = 0.15f;

        private boolean isOnBridge = false;


    private PlayerData	playerData;
    private PlayerKit 	playerKit;
    private TeamColor	teamColor;
    
    private float		currentHealth;
    private float		currentArmor;
    
    private int 		currentEggCount;
    
    private StatePlayerAttack 	 attackState;
    private StatePlayerIdle		 idleState;
    private StatePlayerKnockback knockbackState;
    private StatePlayerWalking	 walkingState;
    
    private StatePlayer			 currentState;
    
    private float				 attackCooldown;
    private float				 attackCooldownTimer;
    private boolean				 attackAvailable;
    
    private float attackBuffTimer;
    private float attackBuffDuration;
    private boolean attackBuffActive;
    private float	attackBuffFactor;

    private FacingDirection		desiredDirection;
    private boolean				movingUp;
    private boolean				movingDown;
    private boolean				movingLeft;
    private boolean				movingRight;
    
    private float				speedBuffTimer;
    private float				speedBuffDuration;
    private boolean				speedBuffActive;
    
    
    private long				droppedEggID;
    
    private Fixture				fixtureLowerBody;
    private Fixture				fixtureFullBody;
    private Fixture fixtureDeathCheck;
    
    private boolean isDead;
    private int collidingBridgePartsCount;
    
    
    public ServerPlayer()
    {
    	super();
    	
    	setPlayerKit(PlayerKit.HUNTER);
    	currentEggCount = 0;
    	
    	attackState = new StatePlayerAttack(this);
    	idleState = new StatePlayerIdle(this);
    	knockbackState = new StatePlayerKnockback(this);
    	walkingState = new StatePlayerWalking(this);
    	currentState = idleState;


    	desiredDirection = FacingDirection.NONE;
    	setFacingDirection(FacingDirection.DOWN);
    	speedBuffTimer = 0.0f;
    	speedBuffDuration = 0.0f;
    	speedBuffActive = false;
    	attackBuffTimer = 0.f;
    	attackBuffDuration = 0.f;
    	attackBuffActive = false;
    	attackBuffFactor = 1.0f;
    	droppedEggID = -1l;
    	isDead = false;
    	collidingBridgePartsCount = 0;
    }
    
    public void enable() {}
    public void disable() {}
    public void dispose() {}
    public void initialize() {}
    
    @Override
    public void update(float deltaTime) 
    {
        if(isDead) {
            isDead = false;
            this.reset();
        }
        
    	currentState.update(deltaTime);
    	
    	if (!attackAvailable)
    	{
        	attackCooldownTimer += deltaTime;
        	if (attackCooldownTimer > attackCooldown)
        	{
        		attackAvailable = true;
        	}
    	}
    	
    	if (speedBuffActive)
    	{
    		speedBuffTimer += deltaTime;
    		if (speedBuffTimer >= speedBuffDuration)
    		{
    			walkingState.setSpeedFactor(1.0f - EGG_CARRY_SPEED_PENALTY * currentEggCount);
    			speedBuffActive = false;
    		}
    	}
    	
    	if (attackBuffActive)
    	{
    		attackBuffTimer += deltaTime;
    		if (attackBuffTimer >= attackBuffDuration)
    		{
    			attackBuffFactor = 1.0f;
    			attackBuffActive = false;
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
            	if (currentState.equals(idleState) || currentState.equals(walkingState))
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
        		if (currentState.equals(idleState) || currentState.equals(walkingState))
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
        		if (currentState.equals(idleState) || currentState.equals(walkingState))
        			dropEgg();
        		break;
            default:
                break;
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
            if (currentState.equals(idleState) || currentState.equals(walkingState))
            	switchToState(walkingState);

        	attackState.setWaitFinishedState(walkingState);
        	knockbackState.setWaitFinishedState(walkingState);
        }
        // Not intended movement
        else
        {
        	if (currentState.equals(walkingState))
        		switchToState(idleState);

        	attackState.setWaitFinishedState(idleState);
        	knockbackState.setWaitFinishedState(idleState);
        }
    }

    protected void moveBegin(FacingDirection dir)
    {
    	setFacingDirection(desiredDirection);
    	physicsBody.applyImpulse(dir.getDirectionVector().x * playerKit.getMaxVelocity(),
		  		 				 dir.getDirectionVector().y * playerKit.getMaxVelocity());
    	
    }
    
    protected void moveEnd()
    {
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
    	if (currentEggCount > 0)
    	{
    	currentEggCount--;
    		ServerEgg egg = (ServerEgg) ServerEntityManager.getInstance().createEntity(ServerEgg.class, getPosition().cpy());
    		droppedEggID = egg.getID();
    	}
    }
    
    public void beginContact(Contact contact) 	
    {
    	 ServerEntity otherEntity = this.identifyContactFixtures(contact);
    	 Fixture fixture = this.getCollidingFixture(contact);
         
         if(otherEntity == null) {
             return;
         }
         
         if(fixture == fixtureLowerBody) {
        	 switch(otherEntity.getEntityType()) {
             case Tank:
             case Hunter:
             case Knight:
             case Noob:
            	 ServerPlayer player = (ServerPlayer) otherEntity;
            	 player.physicsBody.setLinearDamping(COLLISION_DAMPING);
                 break;
             case Ei:			
            	 ServerEgg egg = (ServerEgg) otherEntity;
            	 if(this.currentEggCount < this.playerKit.getMaxEggCount() && egg.getID() != droppedEggID)
            	 {
            		 ServerEntityManager.getInstance().removeEntity(otherEntity);
            	 this.currentEggCount++;
            	 }
            	 break;
             case ContactMine:
            	 
            	 break;
             case Carrot:
            	 applySpeedBuff(ServerCarrot.CARROT_SPEEDBUFF_FACTOR - EGG_CARRY_SPEED_PENALTY * currentEggCount, ServerCarrot.CARROT_SPEEDBUFF_DURATION);
                	 ServerEntityManager.getInstance().removeEntity(otherEntity);
            	 break;
             case Spinach:
                	 applyAttackBuff(ServerSpinach.SPINACH_ATTACKBUFF_FACTOR, ServerSpinach.SPINACH_ATTACKBUFF_DURATION);
                     ServerEntityManager.getInstance().removeEntity(otherEntity);
            	 break;
             case Clover:
                	 applyHealth(ServerClover.CLOVER_HEALTHBUFF_FACTOR);
                	 ServerClover clover = (ServerClover) otherEntity;
                	 ServerEntityManager.getInstance().removeEntity(clover);
            	 break;
             case HayBale:
                 ServerHayBale ball = (ServerHayBale)otherEntity;
                 if(ball.isCrossable()) {
                     this.isOnBridge = true;
                     collidingBridgePartsCount++;
                 } else {
                   this.physicsBody.setLinearDamping(1);
                       if(ball.getSpeed() > 0){
                           this.applyDamage(ball.getVelocity().len());
                       }
                 }
                 break;
             case Bridge:
                 this.isOnBridge = true;
                 collidingBridgePartsCount++;
                 break;
             default:
            	 break;
        	 }
         } else if(fixture == fixtureFullBody) {
        	 switch(otherEntity.getEntityType()) {
                 case Projectil:
                	 ServerProjectile projectile = (ServerProjectile) otherEntity;
                     if (getID() != projectile.getSourceID()) {
                     	applyDamage(projectile.getDamage());
                     	applyKnockback(projectile.getFacingDirection(), KNOCKBACK_IMPULSE);
                     	ServerEntityManager.getInstance().removeEntity(otherEntity);
                     }
                	 break;
                 case SwordAttack:
                     ServerSwordAttack attack = (ServerSwordAttack) otherEntity;
                     if (attack.getSourceID() != getID()) {
                         applyDamage(attack.getDamage());
                         applyKnockback(attack.getFacingDirection(), KNOCKBACK_IMPULSE);
                     }
                	 break;
                 default:
                	 break;
        	 }      
         } else if(fixture == fixtureDeathCheck) {
             switch(otherEntity.getEntityType()) {
                 case AbyssZone:
                 case WaterZone:
                     if(!isOnBridge) {
                         this.isDead = true;
                     }
                     break;
                 default:
                     break;
             }
         }
    }
    
    
public void endContact(Contact contact) {
	ServerEntity otherEntity = this.identifyContactFixtures(contact);
	Fixture fixture = getCollidingFixture(contact);
     
     if(otherEntity == null)
         return;
     
     if (fixture == fixtureLowerBody) {
         switch(otherEntity.getEntityType()) {
         	case Ei:
         		if (((ServerEgg)otherEntity).getID() == droppedEggID)
         			droppedEggID = -1;
         		break;
         	case HayBale:
         	   ServerHayBale ball = (ServerHayBale)otherEntity;
               if(ball.isCrossable()) {
                   collidingBridgePartsCount--;
                   if(collidingBridgePartsCount <= 0) {
                       this.isOnBridge = false;
                   }
               }
               break;
            case Bridge:
                collidingBridgePartsCount--;
                if(collidingBridgePartsCount <= 0) {
                    this.isOnBridge = false;
                }
                break;
            default:
            	break;
         }
	 }
}

    public void preSolve(Contact contact, Manifold oldManifold) {}
    public void postSolve(Contact contact, ContactImpulse impulse) {}
    
    public int				getCurrentEggCount()	{ return currentEggCount; }
    public float			getCurrentHealth()		{ return currentHealth; }
    public float			getCurrentArmor()		{ return currentArmor; }
    public PlayerData		getPlayerInfo()			{ return playerData; }
    public PlayerKit		getPlayerKit()			{ return playerKit; }
    public TeamColor		getTeamColor()			{ return playerData.getTeam(); }
    public EntityType 		getEntityType()			{ return playerKit.getEntityType(); }
    public float			getCurrentAttackMultiplier()	{ return attackBuffFactor; }
	public EntityStates getCurrentPlayerState() {return currentState.getCurrentState();}
    
    public void setPlayerKit(PlayerKit kit)
    {
    	playerKit = kit;
    	currentHealth = kit.getBaseHealth();
    	currentArmor = kit.getBaseArmor();
    }
    
    public void setPlayerData(PlayerData pd)
    {
    	playerData = pd;
    }

    public void applySpeedBuff(float factor, float duration)
    {
    	speedBuffActive = true;
    	speedBuffTimer = 0.0f;
    	speedBuffDuration = duration;
    	walkingState.setSpeedFactor(factor);
    }

    public void applyAttackBuff(float factor, float duration)
    {
    	attackBuffActive = true;
    	attackBuffTimer = 0.f;
    	attackBuffDuration = duration;
    	attackBuffFactor = factor;
    }
    
    public void applyHealth(float factor)
    {
    	this.currentHealth += factor * playerKit.getBaseHealth();
    	if (this.currentHealth > playerKit.getBaseHealth())
    		this.currentHealth = playerKit.getBaseHealth();
    }

	@Override
	public void initPhysics(PhysixManager manager)
	{
		PhysixBody body1 = new PhysixBodyDef(BodyType.DynamicBody, manager)
				.position(properties.getFloat("x"), properties.getFloat("y"))
				.fixedRotation(false)
				.gravityScale(0.0f)
				.create();
		body1.createFixture(new PhysixFixtureDef(manager)
				.density(DENSITY)
				.friction(FRICTION)
				.restitution(RESTITUTION)
				.shapeCircle(HEIGHT / 2.0f, new Vector2(0, HEIGHT / 2.0f))
				);
		body1.createFixture(new PhysixFixtureDef(manager)
				.density(DENSITY)
				.friction(FRICTION)
				.restitution(RESTITUTION)
				.shapeBox(WIDTH, HEIGHT * 2.0f - HEIGHT / 2.0f + HEIGHT / 4.0f, new Vector2(0.0f, 0.0f), 0.0f)
				.sensor(true)
				);
		body1.createFixture(new PhysixFixtureDef(manager)
            .density(DENSITY)
            .friction(FRICTION)
            .restitution(RESTITUTION)
            .shapeCircle(HEIGHT / 4.0f, new Vector2(0, HEIGHT / 4.0f))
            .sensor(true)
        );

		body1.setGravityScale(0);
		body1.addContactListener(this);
		body1.setLinearDamping(BRAKING);
		setPhysicsBody(body1);

		Array<Fixture> fixtures = body1.getBody().getFixtureList();
		fixtureLowerBody = fixtures.get(0);
		fixtureFullBody = fixtures.get(1);
		fixtureDeathCheck = fixtures.get(2);
    	walkingState.setPhysixBody(physicsBody);
	}

	public void switchToState(StatePlayer state)
	{

		currentState.exit();
		currentState = state;
		currentState.init();
	}
	

	@Override
	public void switchToState(State state)
	{
		switchToState((StatePlayer)state);
	}

    public void reset()
    {
        
        currentHealth = playerKit.getBaseHealth();
        currentArmor = playerKit.getBaseArmor();
        setFacingDirection(FacingDirection.DOWN);
        		
        switchToState(idleState);
        
        this.physicsBody.setPosition(properties.getFloat("x"), properties.getFloat("y"));
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
	
	protected void applyKnockback(FacingDirection direction, float impulse)
	{
		knockbackState.setWaitTime(KNOCKBACK_TIME);
		switchToState(knockbackState);
		physicsBody.setLinearDamping(BRAKING);
		physicsBody.applyImpulse(direction.getDirectionVector().x * impulse, direction.getDirectionVector().y * impulse);
	}
        
}
