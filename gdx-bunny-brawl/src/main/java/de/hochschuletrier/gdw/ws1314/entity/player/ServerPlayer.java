package de.hochschuletrier.gdw.ws1314.entity.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
    	moveBegin(direction);
    	
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

    Vector2 dir = new Vector2(0,0);
    public void doAction(PlayerIntention intent)
    {
        switch (intent){
            case MOVE_TOGGLE_UP:
                if(dir.y > 0){
                    dir.y = 0;
                }
                else {
                    dir.y = 1;
                }
                break;
            case MOVE_TOGGLE_DOWN:
                if(dir.y > 0){
                    dir.y = 0;
                }
                else{
                    dir.y = -1;
                }
                break;
            case MOVE_TOGGLE_RIGHT:
                if(dir.x > 0){
                    dir.x = 0;
                }
                else{
                    dir.x = 1;
                }
                break;
            case MOVE_TOGGLE_LEFT:
                if(dir.x > 0){
                    dir.x = 0;
                }
                else{
                    dir.x = -1;
                }
                break;
            case ATTACK_1:
                doFirstAttack();
                break;
            case  ATTACK_2:
                doSecondAttack();
                break;
        }
    }

    public void moveBegin(FacingDirection dir)
    {
    	direction = dir;
    	
    	// TODO acceleration impulse to physics body
    	// Use direction vector and impulse constant to create the impulse vector
    	// Check PlayerKit for impulse constant
    	moveEnd();
    	physicsBody.applyImpulse(dir.getDirectionVector().x * playerKit.getMaxVelocity(),
				 				 dir.getDirectionVector().y * playerKit.getMaxVelocity());
    	moveEnd();
    	
    }
    
    public void moveEnd()
    {
    	// TODO brake impulse to physics body
    	// Use direction vector and impulse constant to create the impulse vector
    	// Check PlayerKit for impulse constant
    	physicsBody.setLinearDamping(1);
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
		//FIXME: player position muss noch irgendwo hinterlegt sein
		PhysixBody body = new PhysixBodyDef(BodyType.DynamicBody, manager)
							  .position(new Vector2()).fixedRotation(false).create();
		body.createFixture(new PhysixFixtureDef(manager).density(0.5f)
				.friction(0.5f).restitution(0.4f).shapeBox(100,100));
		body.setGravityScale(0);
		body.addContactListener(this);
		setPhysicsBody(body);
	}
}
