/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ws1314.entity.projectile;

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
import static de.hochschuletrier.gdw.ws1314.entity.EntityType.SwordAttack;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;

/**
 *
 * @author Patrick
 */
public class ServerSwordAttack extends ServerEntity {
    
    //========================================
    // CONSTANTS
    private static final float totalLifetime = 1500.0f;
    
    //========================================
    // VARIABLES
    private final long sourceID;
    private final Vector2 originPosition;
    private final Vector2 facingDirection;
    
    private float lifetime;
    
    //========================================
    public ServerSwordAttack(long sourceID) {
        super();
        
        this.sourceID = sourceID;
        
        ServerPlayer player = (ServerPlayer)ServerEntityManager.getInstance().getEntityById(sourceID);
        
        this.originPosition = player.getPosition();
        this.facingDirection = player.getFacingDirection().getDirectionVector();
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update(float deltaTime) {
        lifetime += deltaTime;
        
        if(lifetime > totalLifetime) {
            ServerEntityManager.getInstance().removeEntity(this);
        }
    }

    @Override
    public EntityType getEntityType() {
        return SwordAttack;
    }

    @Override
    public void initPhysics(PhysixManager manager) {
        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.StaticBody, manager).position(this.originPosition).fixedRotation(true).create();
            body.createFixture(new PhysixFixtureDef(manager).density(0.5f).friction(0.0f).restitution(0.0f).shapeCircle(30).sensor(true));
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
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
    
}
