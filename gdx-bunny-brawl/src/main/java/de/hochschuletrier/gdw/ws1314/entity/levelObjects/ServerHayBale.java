package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import de.hochschuletrier.gdw.ws1314.entity.*;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ServerProjectile;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ServerSwordAttack;

/**
 * 
 * @author yannick
 *
 */

// Added Carrot Constants by ElFapo
public class ServerHayBale extends ServerLevelObject
{
	private final float DURATION_TIME_IN_WATER = 10.0f;
	private final float SCL_VELOCITY = 300.0f;
	private final float NORMAL_DAMPING = 1.0f;
	
	private float speed;
	private boolean acrossable;
	private float lifetime;
	
	private Fixture fixtureWaterCollCheck;
	private Fixture fixtureMain;
	
	private static final Logger logger = LoggerFactory.getLogger(ServerHayBale.class);
	
	public ServerHayBale()
	{
		super();
		speed = 0;
		lifetime = 0;
		acrossable = false;
	}
	
	@Override
	public void initialize()
	{
		super.initialize();
	}
	
	@Override
	public void beginContact(Contact contact) {
		ServerEntity otherEntity = this.identifyContactFixtures(contact);
		Fixture fixture = this.getCollidingFixture(contact);
		
		if(otherEntity == null){
			return;
		}
		
		if(fixture == fixtureMain) {
		    switch(otherEntity.getEntityType()) {
	            case Projectil:
	                if(!acrossable){
	                    ServerProjectile projectile = (ServerProjectile) otherEntity;
	                    this.physicsBody.applyImpulse(projectile.getFacingDirection().getDirectionVector().x*SCL_VELOCITY,
	                                                  projectile.getFacingDirection().getDirectionVector().y*SCL_VELOCITY);
	                    speed = 1;
						this.setEntityState(EntityStates.WALKING);
	                }
	                break;
	            case SwordAttack:
	                if(!acrossable) {
	                    ServerSwordAttack sword = (ServerSwordAttack) otherEntity;
	                    ServerPlayer player = (ServerPlayer) ServerEntityManager.getInstance().getEntityById(sword.getSourceID());
	                    this.physicsBody.applyImpulse(  player.getFacingDirection().getDirectionVector().x*SCL_VELOCITY + sword.getDamage(),
	                                                    player.getFacingDirection().getDirectionVector().y*SCL_VELOCITY + sword.getDamage());
						this.setEntityState(EntityStates.WALKING);
	                }
	                break;
	            default: 
	                this.acrossable = false;
	                break;
	        }
		} else if(fixture == fixtureWaterCollCheck) {
		    switch(otherEntity.getEntityType()) {
		        case WaterZone:
                    if(fixture == fixtureWaterCollCheck) {
                        this.physicsBody.setLinearVelocity(new Vector2());
                        this.fixtureMain.setSensor(true);
                        this.acrossable = true;
						this.setEntityState(EntityStates.WET);
						NetworkManager.getInstance().sendEntityEvent(getID(), EventType.DRWONING);
                        speed = 0;
                    }
                    break;
		        default:
					this.setEntityState(EntityStates.NONE);
                    this.acrossable = false;
                    break;
		    }
		}
		
	}

	@Override
	public void endContact(Contact contact)
	{
	}

	@Override
	public EntityType getEntityType()
	{
		return EntityType.HayBale;
	}
	
	
	public float getSpeed(){
		return speed;
	}

	@Override
	public void initPhysics(PhysixManager manager)
	{
        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, manager)
            .position(new Vector2(properties.getFloat("x"),properties.getFloat("y")))
            .fixedRotation(true).create();

        
    	body.createFixture(new PhysixFixtureDef(manager)
		    .density(0.5f)
		    .friction(0.0f)
		    .restitution(0.0f)
		    .shapeBox(50,50));
    	
    	body.createFixture(new PhysixFixtureDef(manager)
            .density(0.5f)
            .friction(0.0f)
            .restitution(0.0f)
            .shapeCircle(5)
            .sensor(true));
        
        body.setGravityScale(0);
        body.addContactListener(this);
        body.setLinearDamping(NORMAL_DAMPING);
        setPhysicsBody(body);
        
        Array<Fixture> fixtures = body.getBody().getFixtureList();
        fixtureMain = fixtures.get(0);
        fixtureWaterCollCheck = fixtures.get(1);
	}
	
	public boolean isCrossable() {
	    return this.acrossable;
	}

    @Override
    public void update(float deltaTime) {
        if(acrossable) {
            lifetime += deltaTime;
            if(lifetime > DURATION_TIME_IN_WATER) {
                ServerEntityManager.getInstance().removeEntity(this);
            }
        }
    }

}
