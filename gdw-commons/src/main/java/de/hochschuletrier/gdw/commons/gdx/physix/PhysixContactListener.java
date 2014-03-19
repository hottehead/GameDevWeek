package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 *
 * @author Santo Pfingsten
 */
class PhysixContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        PhysixBody objectA = (PhysixBody) contact.getFixtureA().getBody().getUserData();
        if (objectA != null) {
            objectA.beginContact(contact);
        }
        PhysixBody objectB = (PhysixBody) contact.getFixtureB().getBody().getUserData();
        if (objectB != null) {
            objectB.beginContact(contact);
        }
    }

    @Override
    public void endContact(Contact contact) {
        PhysixBody objectA = (PhysixBody) contact.getFixtureA().getBody().getUserData();
        if (objectA != null) {
            objectA.endContact(contact);
        }
        PhysixBody objectB = (PhysixBody) contact.getFixtureB().getBody().getUserData();
        if (objectB != null) {
            objectB.endContact(contact);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        PhysixBody objectA = (PhysixBody) contact.getFixtureA().getBody().getUserData();
        if (objectA != null) {
            objectA.preSolve(contact, oldManifold);
        }
        PhysixBody objectB = (PhysixBody) contact.getFixtureB().getBody().getUserData();
        if (objectB != null) {
            objectB.preSolve(contact, oldManifold);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        PhysixBody objectA = (PhysixBody) contact.getFixtureA().getBody().getUserData();
        if (objectA != null) {
            objectA.postSolve(contact, impulse);
        }
        PhysixBody objectB = (PhysixBody) contact.getFixtureB().getBody().getUserData();
        if (objectB != null) {
            objectB.postSolve(contact, impulse);
        }
    }
}
