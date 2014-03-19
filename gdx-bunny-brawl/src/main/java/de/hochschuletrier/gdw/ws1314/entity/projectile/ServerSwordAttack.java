/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ws1314.entity.projectile;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import static de.hochschuletrier.gdw.ws1314.entity.EntityType.SwordAttack;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;

/**
 *
 * @author Patrick
 */
public class ServerSwordAttack extends ServerEntity {

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
    }

    @Override
    public EntityType getEntityType() {
        return SwordAttack;
    }

    @Override
    public void initPhysics(PhysixManager manager) {
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
