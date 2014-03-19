package de.hochschuletrier.gdw.ws1314.entity.player.kit;

import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ServerProjectile;

public class AttackShootArrow extends AttackType 
{
    public void fire(ServerPlayer player) 
    {
        new ServerProjectile(player.getID());
    }

}
