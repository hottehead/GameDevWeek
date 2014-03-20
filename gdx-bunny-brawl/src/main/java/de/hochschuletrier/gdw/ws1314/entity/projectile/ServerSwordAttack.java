/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ws1314.entity.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import static de.hochschuletrier.gdw.ws1314.entity.EntityType.SwordAttack;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;

/**
 *
 * @author Patrick
 */
public class ServerSwordAttack extends ServerEntity {
    
    //========================================
    // VARIABLES
    private long sourceID;
    private FacingDirection facingDirection;
    private TeamColor teamColor;
    private Vector2 originPosition;
    private float damage;
    private float despawnTime;
    
    //========================================
    public ServerSwordAttack() {
        super();
        
    }
    
    public void setSource(long sourceID) {
        this.sourceID = sourceID;

        ServerPlayer player = (ServerPlayer) ServerEntityManager.getInstance().getEntityById(sourceID);

        this.teamColor = player.getTeamColor();
        this.facingDirection = player.getFacingDirection();
        this.originPosition = player.getPosition();
    }
    
    public void setDamage(float dmg) {
    	damage = dmg;
    }
    
    public float getDamage() {
    	return damage;
    }
    
    public void setDespawnTime(float t) {
        despawnTime = t;
    }
    
    public float getDespawnTime() {
        return despawnTime;
    }
    
    public TeamColor getTeamColor() {
	return teamColor;
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update(float deltaTime) {
        despawnTime -= deltaTime;
        if(despawnTime < 0) {
            ServerEntityManager.getInstance().removeEntity(this);
        }
    }

    @Override
    public EntityType getEntityType() {
        return SwordAttack;
    }

    @Override
    public void initPhysics(PhysixManager manager) {
        float angle = this.facingDirection.getDirectionVector().getAngleRad();
        
        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.StaticBody, manager)
                .position(this.originPosition)
                .fixedRotation(true)
                .angle(angle)
                .create();
        
        body.createFixture(new PhysixFixtureDef(manager)
                .density(0.5f)
                .friction(0.0f)
                .restitution(0.0f)
                .shapeCircle(2)
                .sensor(true));
        body.setGravityScale(0);
        body.addContactListener(this);
            
        setPhysicsBody(body);
    }

    @Override
    public void beginContact(Contact contact) {
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void reset(){

    }
}
