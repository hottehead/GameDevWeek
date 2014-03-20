package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author yannick
 * 
 */
public class ServerEgg extends ServerLevelObject
{
    public ServerEgg() {
        super();
    }

    public void update(float delta) {
    }
	
    @Override
    public void initialize() {
            super.initialize();
    }

    @Override
    public void beginContact(Contact contact) {
        ServerEntity otherEntity = this.identifyContactFixtures(contact);

        switch(otherEntity.getEntityType()) {
            case Tank:
            case Hunter:
            case Knight:
            case Noob:
                ServerEntityManager.getInstance().removeEntity(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public EntityType getEntityType() {
            return EntityType.Ei;
    }

    @Override
    public void initPhysics(PhysixManager manager) {

        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.StaticBody, manager)
                                            .position(new Vector2(properties.getFloat("x"),properties.getFloat("y")))
                                            .fixedRotation(false).create();

        body.createFixture(new PhysixFixtureDef(manager)
                                .sensor(true)
                                .density(0.5f)
                                .friction(0.0f)
                                .restitution(0.0f)
                                .shapeCircle(16));



        body.setGravityScale(0);
        body.addContactListener(this);
        setPhysicsBody(body);
    }

}
