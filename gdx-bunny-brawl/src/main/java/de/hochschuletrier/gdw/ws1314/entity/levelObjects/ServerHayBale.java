package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
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
	private final float SCL_VELOCITY = 100.0f;
	
	
	public ServerHayBale()
	{
		super();
	}
	
	@Override
	public void initialize()
	{
		super.initialize();
	}
	
	@Override
	public void beginContact(Contact contact) {
		ServerEntity otherEntity = this.identifyContactFixtures(contact);

		switch(otherEntity.getEntityType()) {
			case Projectil:
				ServerProjectile projectile = (ServerProjectile) otherEntity;
				this.physicsBody.applyImpulse(projectile.getFacingDirection().getDirectionVector());
				break;
			case SwordAttack:
				ServerSwordAttack sword = (ServerSwordAttack) otherEntity;
				ServerPlayer player = (ServerPlayer) ServerEntityManager.getInstance().getEntityById(sword.getSourceID());
				this.physicsBody.applyImpulse(	player.getFacingDirection().getDirectionVector().x*SCL_VELOCITY,
												player.getFacingDirection().getDirectionVector().y*SCL_VELOCITY);
				break;
			case WaterZone:
				this.physicsBody.setLinearDamping(100);
			case Knight:
			case Hunter:
			case Noob:
			case Tank:
				this.physicsBody.setLinearDamping(1);
			default:
				break;
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

            body.setGravityScale(0);
            body.addContactListener(this);
            setPhysicsBody(body);
	}

}
