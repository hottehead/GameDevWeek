package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import java.util.HashMap;
import java.util.Iterator;

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
	private final float DURATION_TIME_IN_WATER = 30.0f;
	private final float SCL_VELOCITY = 300.0f;
	private final float NORMAL_DAMPING = 100.0f;
	
	private float speed;
	private boolean acrossable;
	private float lifetime;
	
	private Fixture fixtureMain;
	private Fixture fixCollUpperLeft;
	private Fixture fixCollUpperRight;
	private Fixture fixCollLowerLeft;
	private Fixture fixCollLowerRight;
	
	private boolean collWaterUpperLeft, collWaterUpperRight, collWaterLowerLeft, collWaterLowerRight;
	private boolean collAbyssUpperLeft, collAbyssUpperRight, collAbyssLowerLeft, collAbyssLowerRight;
	private boolean collHayBaleUpperLeft, collHayBaleUpperRight, collHayBaleLowerLeft, collHayBaleLowerRight;
	
	private HashMap<Long, ServerPlayer> playersOnHayBale;
	private int isOnBridge;
	
	private static final Logger logger = LoggerFactory.getLogger(ServerHayBale.class);
		
	public ServerHayBale()
	{
		super();
		speed = 0;
		lifetime = 0;
		acrossable = false;
		
		playersOnHayBale = new HashMap<>();
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
		        case Noob:
		        case Tank:
		        case Hunter:
		        case Knight:
		            this.playersOnHayBale.put(otherEntity.getID(), (ServerPlayer) otherEntity);
		            break;
	            case Projectil:
	                if(!acrossable){
	                    ServerProjectile projectile = (ServerProjectile) otherEntity;
	                    this.physicsBody.applyImpulse(projectile.getFacingDirection().getDirectionVector().x*SCL_VELOCITY,
	                                                  projectile.getFacingDirection().getDirectionVector().y*SCL_VELOCITY);
	                    speed = 1;
						this.setEntityState(EntityStates.NONE);
	                }
	                break;
	            case SwordAttack:
	                if(!acrossable) {
	                    ServerSwordAttack sword = (ServerSwordAttack) otherEntity;
	                    ServerPlayer player = (ServerPlayer) ServerEntityManager.getInstance().getEntityById(sword.getSourceID());
	                    this.physicsBody.applyImpulse(  player.getFacingDirection().getDirectionVector().x*SCL_VELOCITY + sword.getDamage(),
	                                                    player.getFacingDirection().getDirectionVector().y*SCL_VELOCITY + sword.getDamage());
						this.setEntityState(EntityStates.NONE);
	                }
	                break;
	            case Bridge:
	            case BRIDGE_HORIZONTAL_LEFT:
	            case BRIDGE_HORIZONTAL_MIDDLE:
	            case BRIDGE_HORIZONTAL_RIGHT:
	            case BRIDGE_VERTICAL_BOTTOM:
	            case BRIDGE_VERTICAL_MIDDLE:
	            case BRIDGE_VERTICAL_TOP:
	                this.isOnBridge++;
	                break;
	            default: 
	                //this.acrossable = false;
	                break;
	        }
		} else if(fixture == fixCollUpperLeft) {
		    switch(otherEntity.getEntityType()) {
		        case WaterZone:
                    this.collWaterUpperLeft = true;
                    break;
		        case AbyssZone:
		            this.collAbyssUpperLeft = true;
		            break;
		        case HayBale:
		            this.collHayBaleUpperLeft = true;
		            break;
		        default:
                    break;
		    }
		} else if(fixture == fixCollUpperRight) {
            switch(otherEntity.getEntityType()) {
                case WaterZone:
                    this.collWaterUpperRight = true;
                    break;
                case AbyssZone:
                    this.collAbyssUpperRight = true;
                    break;
                case HayBale:
                    this.collHayBaleUpperRight = true;
                    break;
                default:
                    break;
            }
        } else if(fixture == fixCollLowerLeft) {
            switch(otherEntity.getEntityType()) {
                case WaterZone:
                    this.collWaterLowerLeft = true;
                    break;
                case AbyssZone:
                    this.collAbyssLowerLeft = true;
                    break;
                case HayBale:
                    this.collHayBaleLowerLeft = true;
                    break;
                default:
                    break;
            }
        } else if(fixture == fixCollLowerRight) {
            switch(otherEntity.getEntityType()) {
                case WaterZone:
                    this.collWaterLowerRight = true;
                    break;
                case AbyssZone:
                    this.collAbyssLowerRight = true;
                    break;
                case HayBale:
                    this.collHayBaleLowerRight = true;
                    break;
                default:
                    break;
            }
        }
		
	}

	@Override
	public void endContact(Contact contact)
	{
	    ServerEntity otherEntity = this.identifyContactFixtures(contact);
        Fixture fixture = this.getCollidingFixture(contact);
        
        if(otherEntity == null){
            return;
        }
	    
        if(fixture == fixtureMain) {
          switch(otherEntity.getEntityType()) {
              case Noob:
              case Knight:
              case Hunter:
              case Tank:
                  this.playersOnHayBale.remove(otherEntity.getID());
                  break;
              case Bridge:
              case BRIDGE_HORIZONTAL_LEFT:
              case BRIDGE_HORIZONTAL_MIDDLE:
              case BRIDGE_HORIZONTAL_RIGHT:
              case BRIDGE_VERTICAL_BOTTOM:
              case BRIDGE_VERTICAL_MIDDLE:
              case BRIDGE_VERTICAL_TOP:
                  this.isOnBridge--;
                  if(this.isOnBridge < 0) {
                      this.isOnBridge = 0;
                  }
                  break;
              default:
                  break;
          }
        } else if(fixture == fixCollUpperLeft) {
            switch(otherEntity.getEntityType()) {
                case WaterZone:
                    this.collWaterUpperLeft = false;
                    break;
                case AbyssZone:
                    this.collAbyssUpperLeft = false;
                    break;
                case HayBale:
                    this.collHayBaleUpperLeft = false;
                    break;
                default:
                    break;
            }
        } else if(fixture == fixCollUpperRight) {
            switch(otherEntity.getEntityType()) {
                case WaterZone:
                    this.collWaterUpperRight = false;
                    break;
                case AbyssZone:
                    this.collAbyssUpperRight = false;
                    break;
                case HayBale:
                    this.collHayBaleUpperRight = false;
                    break;
                default:
                    break;
            }
        } else if(fixture == fixCollLowerLeft) {
            switch(otherEntity.getEntityType()) {
                case WaterZone:
                    this.collWaterLowerLeft = false;
                    break;
                case AbyssZone:
                    this.collAbyssLowerLeft = false;
                    break;
                case HayBale:
                    this.collHayBaleLowerLeft = false;
                    break;
                default:
                    break;
            }
        } else if(fixture == fixCollLowerRight) {
            switch(otherEntity.getEntityType()) {
                case WaterZone:
                    this.collWaterLowerRight = false;
                    break;
                case AbyssZone:
                    this.collAbyssLowerRight = false;
                    break;
                case HayBale:
                    this.collHayBaleLowerRight = false;
                    break;
                default:
                    break;
            }
        }
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
	    float posX = properties.getFloat("x");
	    float posY = properties.getFloat("y");
	    
        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, manager)
            .position(new Vector2(posX, posY))
            .fixedRotation(true).create();

        //main fixture
    	body.createFixture(new PhysixFixtureDef(manager)
		    .density(0.5f)
		    .friction(0.0f)
		    .restitution(0.0f)
		    .shapeBox(50,50));
    	
    	//upper left coll check
    	body.createFixture(new PhysixFixtureDef(manager)
            .density(0.5f)
            .friction(0.0f)
            .restitution(0.0f)
            .shapeBox(1, 1, new Vector2(-23, -23), 0)
            .sensor(true));
    	
    	//upper right coll check
    	body.createFixture(new PhysixFixtureDef(manager)
            .density(0.5f)
            .friction(0.0f)
            .restitution(0.0f)
            .shapeBox(1, 1, new Vector2(23, -23), 0)
            .sensor(true));
    	
    	//lower left coll check
    	body.createFixture(new PhysixFixtureDef(manager)
            .density(0.5f)
            .friction(0.0f)
            .restitution(0.0f)
            .shapeBox(1, 1, new Vector2(-23, 23), 0)
            .sensor(true));
    	
    	//lower right coll check
    	body.createFixture(new PhysixFixtureDef(manager)
            .density(0.5f)
            .friction(0.0f)
            .restitution(0.0f)
            .shapeBox(1, 1, new Vector2(23, 23), 0)
            .sensor(true));
        
        body.setGravityScale(0);
        body.addContactListener(this);
        body.setLinearDamping(NORMAL_DAMPING);
        setPhysicsBody(body);
        
        Array<Fixture> fixtures = body.getBody().getFixtureList();
        fixtureMain = fixtures.get(0);
        fixCollUpperLeft = fixtures.get(1);
        fixCollUpperRight = fixtures.get(2);
        fixCollLowerLeft = fixtures.get(3);
        fixCollLowerRight = fixtures.get(4);
	}
	
	public boolean isCrossable() {
	    return this.acrossable;
	}

    @Override
    public void update(float deltaTime) {
        if(acrossable) {
            lifetime += deltaTime;
            if(lifetime > DURATION_TIME_IN_WATER) {
                this.setEntityState(EntityStates.NONE);
                Iterator<Long> keySetIterator = this.playersOnHayBale.keySet().iterator();
                
                while(keySetIterator.hasNext()) {
                    Long key = keySetIterator.next();
                    ServerPlayer player = this.playersOnHayBale.get(key);
                    player.setPlayerIsNotOnBridgeAnymore();
                }
                
                this.reset();
            }
        } else if(collWaterUpperLeft && collWaterUpperRight && collWaterLowerLeft && collWaterLowerRight) {
            if(!collHayBaleLowerLeft && !collHayBaleLowerRight && !collHayBaleUpperLeft && !collHayBaleUpperRight) {
                this.physicsBody.setLinearVelocity(new Vector2());
                this.fixtureMain.setSensor(true);
                this.acrossable = true;
                this.setEntityState(EntityStates.WET);
                NetworkManager.getInstance().sendEntityEvent(getID(), EventType.DRWONING);
                speed = 0;
                
                Iterator<Long> keySetIterator = this.playersOnHayBale.keySet().iterator();
                
                while(keySetIterator.hasNext()) {
                    Long key = keySetIterator.next();
                    ServerPlayer player = this.playersOnHayBale.get(key);
                    player.setPlayerIsOnBridge();
                    }
            }
        } else if(collAbyssUpperLeft && collAbyssUpperRight && collAbyssLowerLeft && collAbyssLowerRight) {
            if(this.isOnBridge == 0) {
                this.reset();
            }
        }
    }
    
    public void reset() {
        super.reset();
        this.fixtureMain.setSensor(false);
        collWaterUpperLeft = false;
        collWaterUpperRight = false;
        collWaterLowerLeft = false;
        collWaterLowerRight = false;
        collAbyssUpperLeft = false;
        collAbyssUpperRight = false;
        collAbyssLowerLeft = false;
        collAbyssLowerRight = false;
        collHayBaleUpperLeft = false;
        collHayBaleUpperRight = false;
        collHayBaleLowerLeft = false;
        collHayBaleLowerRight = false;
        acrossable = false;
        lifetime = 0;
    }

}
