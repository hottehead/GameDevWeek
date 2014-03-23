package de.hochschuletrier.gdw.ws1314.entity.player.kit;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ServerProjectile;

/**
 * 
 * @author ElFapo
 *
 */

// Modified by ElFapo
public class AttackShootArrow extends AttackType 
{
	public static final int DAMAGE = 50; 
	public static final float ARROW_SPAWN_DISTANCE = 20.0f;
	public static final float ARROW_VELOCITY = 250.0f;
	public static final float ARROW_FLIGHT_DISTANCE = 500.0f;
	public static final float ARROW_DESPAWN_TIME = 1.0f;
	
	public static final float ARROW_HITCIRCLE_RADIUS = 4.0f;
	
    public void fire(ServerPlayer player)  
    {
    	Vector2 playerPos = player.getPosition();
    	Vector2 playerDir = player.getFacingDirection().getDirectionVector();
    	
    	Vector2 arrowPos = new Vector2( playerPos.x + playerDir.x * ARROW_SPAWN_DISTANCE,
    									playerPos.y + playerDir.y * ARROW_SPAWN_DISTANCE);// - ARROW_SPAWN_DISTANCE / 2.0f);
    	
    	ServerProjectile projectile = (ServerProjectile) ServerEntityManager.getInstance().createEntity(ServerProjectile.class, arrowPos);
    	projectile.setSource(player.getID());
    	projectile.setHitCircleRadius(ARROW_HITCIRCLE_RADIUS);
    	projectile.setPhysicalParameters(ARROW_VELOCITY, ARROW_FLIGHT_DISTANCE, ARROW_DESPAWN_TIME);
    	projectile.setDamage(DAMAGE);
    }

}
