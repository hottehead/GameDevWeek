package de.hochschuletrier.gdw.ws1314.entity.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ServerEgg;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ServerProjectile;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.input.PlayerIntention;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ElFapo
 * ASK BEFORE MODIFYING, OR I'LL MOST CERTAINLY TAKE A SHIT ON YOUR HEAD!
 */

public class ServerPlayer extends ServerEntity 
{
    private static final Logger logger = LoggerFactory.getLogger(ServerPlayer.class);


	private final float FRICTION = 0;


	private final float RESTITUTION = 0;


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
    boolean				movingUp;
    boolean				movingDown;
    boolean				movingLeft;
    boolean				movingRight;
    
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
            	if (!firstAttackFired && !secondAttackFired)
            	{
            		doFirstAttack();
            		firstAttackTimer = 0.0f;
            	}
                break;
            case ATTACK_2:
            	if (!firstAttackFired && !secondAttackFired)
            	{
            		doSecondAttack();
            		secondAttackTimer = 0.0f;
            	}
                break;
            case DROP_EGG:
            	dropEgg();
        }
        
        if (movingUp)
        {
        	if (movingLeft)
        		moveBegin(FacingDirection.UP_LEFT);
        	else if (movingRight)
        		moveBegin(FacingDirection.UP_RIGHT);
        	else
        		moveBegin(FacingDirection.UP);
        }
        else if (movingDown)
        {
        	if (movingLeft)
        		moveBegin(FacingDirection.DOWN_LEFT);
        	else if (movingRight)
        		moveBegin(FacingDirection.DOWN_RIGHT);
        	else
        		moveBegin(FacingDirection.DOWN);
        }
        else if (movingLeft)
        	moveBegin(FacingDirection.LEFT);
        else if (movingRight)
        	moveBegin(FacingDirection.RIGHT);
        else
        	moveEnd();
    }

    private void moveBegin(FacingDirection dir)
    {
    	direction = dir;
    	
    	// TODO 
    	// Damp old impulse
    	// acceleration impulse to physics body
    	// Use direction vector and impulse constant to create the impulse vector
    	// Check PlayerKit for impulse constant

    	physicsBody.applyImpulse(dir.getDirectionVector().x * playerKit.getMaxVelocity(),
		  		 				 dir.getDirectionVector().y * playerKit.getMaxVelocity());
    	moveEnd();

    	
    }
    
    private void moveEnd()
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
    public void beginContact(Contact contact) 	{
    	 ServerEntity otherEntity = this.identifyContactFixtures(contact);
         
         switch(otherEntity.getEntityType()) {
             case Tank:
             case Hunter:
             case Knight:
             case Noob:
                 ServerPlayer player = (ServerPlayer)otherEntity;
                 if(this.firstAttackFired){
                	 
                 }else if(this.secondAttackFired){
                	 
                 }
                 break;
             case Ei:			
            	 ServerEgg egg = (ServerEgg) otherEntity;
            	 this.currentEggCount++;
            	 break;
             case Projectil: 
            	
            	 ServerProjectile projectile = (ServerProjectile) otherEntity;
            	 ServerPlayer hunter = (ServerPlayer) ServerEntityManager.getInstance().getEntityById(projectile.getID());
            	 /*FIXME: Ich brauche noch die Angriffspunkte des Bogenschützen 
            	  * this.currentHealth -= hunter.angriff;
            	  */
            	 if(this.currentHealth <= 0){
            	  	 ServerEntityManager.getInstance().removeEntity(this);
            	  }
            	 
            	 break;
             case Bridge: 		
            	 break;
             case BridgeSwitch:	
            	 break;
             case Bush:			
            	 break;
             default:
            	 break;
                 
         }
    }
    public void endContact(Contact contact) 	{}
    public void preSolve(Contact contact, Manifold oldManifold) {}
    public void postSolve(Contact contact, ContactImpulse impulse) {}
    
    public FacingDirection  getFacingDirection()	{ return direction; }
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
		body.createFixture(new PhysixFixtureDef(manager).density(0)
				.friction(FRICTION).restitution(RESTITUTION).shapeBox(100,100));
		body.setGravityScale(0);
		body.addContactListener(this);
		setPhysicsBody(body);
	}
}
