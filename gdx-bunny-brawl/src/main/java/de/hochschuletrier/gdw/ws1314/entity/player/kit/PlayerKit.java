package de.hochschuletrier.gdw.ws1314.entity.player.kit;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;

/**
 * @author ElFapo
 *  Who deleted this?
 */
public enum PlayerKit 
{
	//		TYPE				VELO	EGG HEALTH		ARMOR	COOLDOWNS	ATTACKS
    NOOB(  EntityType.Noob, 	100.0f, 1, 	 10.0f, 	0.0f, 	0.5f, 0.5f, new BasicAttack(), new BasicAttack()),
    HUNTER(EntityType.Hunter, 	120.0f, 1, 	200.0f, 	0.0f, 	0.55f, 0.5f, new AttackShootArrow(), new BasicAttack()),
    KNIGHT(EntityType.Knight, 	100.0f, 1, 	300.0f, 	0.0f, 	0.2f, 0.5f, new AttackSwingSword(), new BasicAttack()),
    TANK(  EntityType.Tank, 	40.0f,  3,  1000.0f,	0.0f,	0.4f, 0.5f, new AttackBlow(), new BasicAttack());
    
    private final float 	maxVelocity;
    private final int 		maxEggCount;
    private final float 	baseHealth;
    private final float 	baseArmor;
    private final float 	firstAttackCooldown;
    private final float 	secondAttackCooldown;
    private final AttackType attack1;
    private final AttackType attack2;
    private final EntityType entityType;
    
    public final float		accelerationImpulse = 800.0f;
    public final float		brakeImpulse = 100.0f;
    
    private PlayerKit (	EntityType entityType,
    					float 	maxVelocity, 
    					int 	maxEggCount, 
    					float 	health, 
    					float 	armor, 
    					float 	firstAttackCooldown, 
    					float 	secondAttackCooldown,
    					AttackType attack1,
    					AttackType attack2)
    {
    	this.entityType = entityType;
    	
    	this.maxVelocity = maxVelocity;
    	this.maxEggCount = maxEggCount;
    
    	this.baseArmor = armor;
    	this.baseHealth = health;
    	
    	this.firstAttackCooldown = firstAttackCooldown;
    	this.secondAttackCooldown = secondAttackCooldown;
    	
    	this.attack1 = attack1;
    	this.attack2 = attack2;
    }
    
    public float getMaxVelocity()			{ return maxVelocity; }
    public float getMaxEggCount()			{ return maxEggCount; }
    public float getBaseHealth()			{ return baseHealth;  }
    public float getBaseArmor()				{ return baseArmor;   }
    
    public void doFirstAttack(ServerPlayer player)	{  attack1.fire(player); }
    public void doSecondAttack(ServerPlayer player)	{  attack2.fire(player); }
    
    public float getFirstAttackCooldown()		{ return firstAttackCooldown; }
    public float getSecondAttackCooldown()		{ return secondAttackCooldown; }
    
    public EntityType getEntityType()			{ return entityType; }

}
