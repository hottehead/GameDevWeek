package de.hochschuletrier.gdw.ws1314.entity.player.kit;

import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;

public abstract class AttackType 
{
    public abstract void fire(ServerPlayer player);
    public float getBaseDamage()	{ return 0.0f; }
    public abstract float getAttackTime();
}
