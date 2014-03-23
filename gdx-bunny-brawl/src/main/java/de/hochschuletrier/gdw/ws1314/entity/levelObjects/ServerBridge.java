package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;

/**
 * 
 * @author yannick
 * 
 */
public class ServerBridge extends ServerLevelObject
{
	private boolean isVisible = false;

    private EntityType type = EntityType.Bridge;
    
    private HashMap<Long, ServerPlayer> collidingPlayers;
    private HashMap<Long, ServerEgg> collidingEggs;
    private HashMap<Long, ServerHayBale> collidingHayBales;
    
	
	public ServerBridge()
	{
	    collidingPlayers = new HashMap<>();
	    collidingEggs = new HashMap<>();
	    collidingHayBales = new HashMap<>();
	}
	
	@Override
	public void initialize(){
		super.initialize();
	}

	@Override
	public void beginContact(Contact contact)
	{
	    ServerEntity otherEntity = this.identifyContactFixtures(contact);
        
        if(otherEntity == null){
            return;
        }
        
        switch(otherEntity.getEntityType()) {
            case Ei:
                this.collidingEggs.put(otherEntity.getID(), (ServerEgg) otherEntity);
                break;
            case Noob:
            case Knight:
            case Tank:
            case Hunter:
                this.collidingPlayers.put(otherEntity.getID(), (ServerPlayer) otherEntity);
                break;
            case HayBale:
                this.collidingHayBales.put(otherEntity.getID(), (ServerHayBale) otherEntity);
                break;
            default:
                break;
        }
	}

	@Override
	public void endContact(Contact contact)
	{
	    ServerEntity otherEntity = this.identifyContactFixtures(contact);
        
        if(otherEntity == null){
            return;
        }
        
        switch(otherEntity.getEntityType()) {
            case Ei:
                this.collidingEggs.remove(otherEntity.getID());
                break;
            case Noob:
            case Knight:
            case Tank:
            case Hunter:
                this.collidingPlayers.remove(otherEntity.getID());
                break;
            case HayBale:
                this.collidingHayBales.remove(otherEntity.getID());
                break;
            default:
                break;
        }
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
	}

	@Override
	public EntityType getEntityType()
	{
		return type;
	}

    public void setHorizontalLeft()
    {
        type = EntityType.BRIDGE_HORIZONTAL_LEFT;
    }
    public void setHorizontalMiddle()
    {
        type = EntityType.BRIDGE_HORIZONTAL_MIDDLE;
    }
    public void setHorizontalRight()
    {
        type = EntityType.BRIDGE_HORIZONTAL_RIGHT;
    }
    public void setVerticalBottom()
    {
        type = EntityType.BRIDGE_VERTICAL_BOTTOM;
    }
    public void setVerticalMiddle()
    {
        type = EntityType.BRIDGE_VERTICAL_MIDDLE;
    }
    public void setVerticalTop()
    {
        type = EntityType.BRIDGE_VERTICAL_TOP;
    }

	@Override
	public void initPhysics(PhysixManager manager)
	{
	    int width, height;
	    switch(this.type) {
	        //the bridge parts with horizontal in their name are part of an HORIZONTAL BRIDGE
	        //the parts themselves are vertical
	        case BRIDGE_HORIZONTAL_LEFT:
	        case BRIDGE_HORIZONTAL_RIGHT:
	        case BRIDGE_HORIZONTAL_MIDDLE:
	            width = 32;
	            height = 96;
	            break;
	        //the same as above
	        case BRIDGE_VERTICAL_BOTTOM:
	        case BRIDGE_VERTICAL_MIDDLE:
	        case BRIDGE_VERTICAL_TOP:
	            width = 96;
	            height = 32;
	            break;
	        default:
	            width = 100;
	            height = 200;
	            break;
	    }
	    
		PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.KinematicBody, manager)
									.position(new Vector2(properties.getFloat("x"),properties.getFloat("y")))
									.fixedRotation(false).create();
		body.createFixture(new PhysixFixtureDef(manager)
									.density(0.5f).friction(0.0f)
									.restitution(0.0f).sensor(true).shapeBox(width, height));
		
		body.setGravityScale(0);
		body.addContactListener(this);
		setPhysicsBody(body);
	}
	
	@Override
	public boolean getVisibility(){
		return isVisible;
	}

	public void setVisibility(boolean b){
		if(isVisible)	{
			NetworkManager.getInstance().sendEntityEvent(getID(), EventType.BRIDGE_IN);
		} else{
			NetworkManager.getInstance().sendEntityEvent(getID(), EventType.BRIDGE_OUT);
		}
		isVisible = b;
		
		if(!isVisible) {
		    //eier
		    Iterator<Long> keySetIterator = this.collidingEggs.keySet().iterator();

		    while(keySetIterator.hasNext()){
		      Long key = keySetIterator.next();
		      ServerEgg egg = this.collidingEggs.get(key);
		      if(egg.getVisibility()) {
		          egg.reset();
		      }
		    }
		    
		    //spieler
		    Iterator<Long> keySetIterator2 = this.collidingPlayers.keySet().iterator();
		    
		    while(keySetIterator2.hasNext()) {
		        Long key = keySetIterator2.next();
		        ServerPlayer player = this.collidingPlayers.get(key);
		        player.setPlayerInDeathZone();
		    }
		    
		    //heuball
		    Iterator<Long> keySetIterator3 = this.collidingHayBales.keySet().iterator();

            while(keySetIterator3.hasNext()){
              Long key = keySetIterator.next();
              ServerHayBale egg = this.collidingHayBales.get(key);
              
            }
		}
	}

    @Override
    public void update(float deltaTime) {
        
    }
}
