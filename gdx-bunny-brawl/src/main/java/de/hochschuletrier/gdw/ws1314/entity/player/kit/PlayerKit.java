package de.hochschuletrier.gdw.ws1314.entity.player.kit;

import de.hochschuletrier.gdw.ws1314.entity.player.Player;

public enum PlayerKit 
{
	NOOB(1.0f, 1, 100.0f, 0.0f, 1.0f, 1.0f, new BasicAttack(), new BasicAttack());
	
	private final float 	maxVelocity;
	private final int 		maxEggCount;
	private final float 	baseHealth;
	private final float 	baseArmor;
	private final float 	firstAttackCooldown;
	private final float 	secondAttackCooldown;
	private final AttackType attack1;
	private final AttackType attack2;
	
	private PlayerKit (	float 	maxVelocity, 
						int 	maxEggCount, 
						float 	armor, 
						float 	health, 
						float 	firstAttackCooldown, 
						float 	secondAttackCooldown,
						AttackType attack1,
						AttackType attack2)
	{
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
	
	public void doFirstAttack(Player player)	{  attack1.fire(player); }
	public void doSecondAttack(Player player)	{  attack2.fire(player); }
}
