package de.hochschuletrier.gdw.ws1314.entity.player.kit;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ServerSwordAttack;

/**
 * 
 * @author ElFapo
 *
 */
public class AttackSwingSword extends AttackType 
{
    public static final float 	DAMAGE = 50.0f;
    public static final float	ANGLE = (float) Math.PI / 2.0f;
    public static final float	RANGE = 32.0f;
    
    public void fire(ServerPlayer player)
    {
    	Vector2 pos = new Vector2(player.getPosition());
    	pos.x += player.getFacingDirection().getDirectionVector().x;
    	pos.y += player.getFacingDirection().getDirectionVector().y;
    	
    	ServerSwordAttack attack = (ServerSwordAttack) ServerEntityManager.getInstance().createEntity(ServerSwordAttack.class, pos);
    	attack.setDamage(DAMAGE);
    	attack.setSource(player.getID());
    	attack.setSize(RANGE,  ANGLE);
    	attack.setDespawnTime(ServerPlayer.ATTACK_TIME);
    }

}
