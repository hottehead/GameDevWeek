package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author yannick
 * 
 */
public class ServerBridge extends ServerLevelObject
{
	private boolean isVisible = false;
	private Fixture fixtureBody;
	/* FIXME:
	 * Comment: von Fabio Gimmillaro (Der komische Typ ganz hinten rechts)
	 * Bridge braucht ID, damit man einer Brücke bestimmte Schalter hinzufügen kann
	 * Ich muss auch in ServerBridgeSwitch darauf zugreifen können
	 * also bitte noch Getter einfügen oder public setzen mir egal ^^
	 * 
	 * private final long ID;
	 * public ServerBridge(long ID)
	 * {
	 * 		this.ID = ID;
	 * }
	 * 
	*/



    private EntityType type = EntityType.Bridge;
	
	public ServerBridge()
	{

	}
	
	@Override
	public void initialize(){
		super.initialize();
	}

	@Override
	public void beginContact(Contact contact)
	{
	}

	@Override
	public void endContact(Contact contact)
	{
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
		// TODO Auto-generated method stub
		PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.KinematicBody, manager)
									.position(new Vector2(properties.getFloat("x"),properties.getFloat("y")))
									.fixedRotation(false).create();
		body.createFixture(new PhysixFixtureDef(manager)
									.density(0.5f).friction(0.0f)
									.restitution(0.0f).sensor(true).shapeBox(100,200));
		
		body.setGravityScale(0);
		body.addContactListener(this);
		setPhysicsBody(body);
		
		Array<Fixture> fixtures = body.getBody().getFixtureList();
		fixtureBody = fixtures.get(0);
	}
	@Override
	public boolean getVisibility(){
		return isVisible;
	}
	public void setVisiblity(boolean b){
		if(isVisible)	{
			NetworkManager.getInstance().sendEntityEvent(getID(), EventType.BRIDGE_IN);
		} else{
			NetworkManager.getInstance().sendEntityEvent(getID(), EventType.BRIDGE_OUT);
		}
		isVisible = b;
		
		if(this.fixtureBody != null) {
    		if(isVisible) {
    		   this.fixtureBody.setSensor(true); 
    		} else if(isVisible) {
    		    this.fixtureBody.setSensor(false);
    		}
		}
	}

    @Override
    public void update(float deltaTime) {
        // TODO Auto-generated method stub
        
    }
}
