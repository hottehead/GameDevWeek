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
	private final float SCL_VELOCITY = 300.0f;
	private final float NORMAL_DAMPING = 1.0f;
	private final float WATER_DAMPING = 100.0f;
	private float speed;
	private boolean acrossable = false;
	
	
	public ServerHayBale()
	{
		super();
		speed = 0;
	}
	
	@Override
	public void initialize()
	{
		super.initialize();
	}
	
	@Override
	public void beginContact(Contact contact) {
		ServerEntity otherEntity = this.identifyContactFixtures(contact);
		
		if(otherEntity == null){
			return;
		}
		switch(otherEntity.getEntityType()) {
			case Projectil:
				if(!acrossable){
					this.physicsBody.setLinearDamping(NORMAL_DAMPING);
					ServerProjectile projectile = (ServerProjectile) otherEntity;
					this.physicsBody.applyImpulse(projectile.getFacingDirection().getDirectionVector().x*SCL_VELOCITY,
												  projectile.getFacingDirection().getDirectionVector().y*SCL_VELOCITY);
					speed = 1;
				}
				break;
			case SwordAttack:
				this.physicsBody.setLinearDamping(NORMAL_DAMPING);
				ServerSwordAttack sword = (ServerSwordAttack) otherEntity;
				ServerPlayer player = (ServerPlayer) ServerEntityManager.getInstance().getEntityById(sword.getSourceID());
				this.physicsBody.applyImpulse(	player.getFacingDirection().getDirectionVector().x*SCL_VELOCITY,
												player.getFacingDirection().getDirectionVector().y*SCL_VELOCITY);
				break;
			case WaterZone:
				this.physicsBody.setLinearDamping(WATER_DAMPING);
				this.acrossable = true;
				speed = 0;
				break;
			case Knight:
			case Hunter:
			case Noob:
			case Tank:
				
				if(!acrossable){
				ServerPlayer player2 = (ServerPlayer) otherEntity;
				this.physicsBody.setLinearDamping(1);
					if(speed > 0){
						player2.applyDamage(10);
					}
				}else{
					this.physicsBody.setLinearDamping(WATER_DAMPING);
				}
				speed = 0;
				break;
			default: 
				this.acrossable = false;
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
	
	
	public float getSpeed(){
		return speed;
	}

	@Override
	public void initPhysics(PhysixManager manager)
	{
            PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, manager)
                .position(new Vector2(properties.getFloat("x"),properties.getFloat("y")))
                .fixedRotation(true).create();
            body.setLinearDamping(NORMAL_DAMPING);
            if(!acrossable){
            	body.createFixture(new PhysixFixtureDef(manager)
				.density(0.5f)
				.friction(0.0f)
				.restitution(0.0f)
				.shapeBox(50,50));
            }else{
	            body.createFixture(new PhysixFixtureDef(manager)
	            	.sensor(true)
	            	.shapeBox(60,60));
            }
            body.setGravityScale(0);
            body.addContactListener(this);
            setPhysicsBody(body);
	}

}
