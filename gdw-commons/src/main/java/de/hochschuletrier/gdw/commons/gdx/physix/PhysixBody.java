package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;

import java.util.HashSet;

/**
 *
 * @author Santo Pfingsten
 */
public final class PhysixBody {

    private PhysixManager manager;
    private PhysixEntity owner;
    private Body body;
    private final HashSet<ContactListener> contactListeners = new HashSet<ContactListener>();
    private static final Vector2 dummyVector = new Vector2();
    private final Vector2 linearVelocity = new Vector2();
    private final Vector2 position = new Vector2();

    protected PhysixBody(BodyDef bodyDef, PhysixManager manager) {
        this.manager = manager;

        body = manager.getWorld().createBody(bodyDef);
        body.setUserData(this);
    }

    public void createFixture(PhysixFixtureDef fixtureDef) {
        body.createFixture(fixtureDef);
    }

	public Body getBody() {
        return body;
    }

    protected Array<Fixture> getFixtureList() {
        return body.getFixtureList();
    }

    public void setOwner(PhysixEntity owner) {
        this.owner = owner;
    }

    public PhysixEntity getOwner() {
        return owner;
    }

    public float getX() {
        return manager.toWorld(body.getPosition().x);
    }

    public float getY() {
        return manager.toWorld(body.getPosition().y);
    }

    public Vector2 getPosition() {
        return manager.toWorld(body.getPosition(), position);
    }

    public void setX(float x) {
        body.setTransform(dummyVector.set(manager.toBox2D(x), body.getPosition().y), body.getAngle());
    }

    public void setY(float y) {
        body.setTransform(dummyVector.set(body.getPosition().x, manager.toBox2D(y)), body.getAngle());
    }

    public void setPosition(Vector2 pos) {
        body.setTransform(manager.toBox2D(pos, dummyVector), body.getAngle());
    }

    public void setPosition(float x, float y) {
        body.setTransform(manager.toBox2D(x, y, dummyVector), body.getAngle());
    }

    public void setTransform(float x, float y, float omega) {
        body.setTransform(manager.toBox2D(x, y, dummyVector), omega);
    }

    public Vector2 getLocalCenter(Vector2 origin) {
        return manager.toBox2D(body.getLocalCenter(), origin);
    }

    public void simpleForceApply(Vector2 force) {
        body.applyForceToCenter(force, true);
    }

    public void applyImpulse(Vector2 speed) {
        body.applyLinearImpulse(manager.toBox2D(speed, dummyVector), body.getWorldCenter(), true);
    }

    public void applyImpulse(float x, float y) {
        body.applyLinearImpulse(manager.toBox2D(x, y, dummyVector), body.getWorldCenter(), true);
    }

    public float getAngle() {
        return body.getAngle();
    }

    public Vector2 getLinearVelocity() {
        return manager.toWorld(body.getLinearVelocity(), linearVelocity);
    }

    public void setLinearVelocity(Vector2 v) {
        body.setLinearVelocity(manager.toBox2D(v, dummyVector));
    }

    public void setLinearVelocity(float x, float y) {
        body.getLinearVelocity().x = manager.toBox2D(x);
        body.getLinearVelocity().y = manager.toBox2D(y);
        body.setAwake(true);
    }

    public void setLinearVelocityX(float x) {
        body.getLinearVelocity().x = manager.toBox2D(x);
        body.setAwake(true);
    }

    public void setLinearVelocityY(float y) {
        body.getLinearVelocity().y = manager.toBox2D(y);
        body.setAwake(true);
    }

    public void setAngularVelocity(float omega) {
        body.setAngularVelocity(omega);
        body.setAwake(true);
    }

    public boolean isAwake() {
        return body.isAwake();
    }

    public boolean isAsleep() {
        return !body.isAwake();
    }

    public void setGravityScale(float gravityScale) {
        body.setGravityScale(gravityScale);
    }

    public float getGravityScale() {
        return body.getGravityScale();
    }

    public void setMassData(MassData massData) {
        body.setMassData(massData);
    }

    public void setMassData(float mass) {
        MassData massData = new MassData();
        massData.mass = mass;
        body.setMassData(massData);
    }

    public float getMass() {
        return body.getMass();
    }

    public boolean addContactListener(ContactListener listener) {
        return contactListeners.add(listener);
    }

    public boolean removeContactListener(ContactListener listener) {
        return contactListeners.remove(listener);
    }

    public void beginContact(Contact contact) {
        for (ContactListener listener : contactListeners) {
            listener.beginContact(contact);
        }
    }

    public void endContact(Contact contact) {
        for (ContactListener listener : contactListeners) {
            listener.endContact(contact);
        }
    }

    public void preSolve(Contact contact, Manifold oldManifold) {
        for (ContactListener listener : contactListeners) {
            listener.preSolve(contact, oldManifold);
        }
    }

    public void postSolve(Contact contact, ContactImpulse impulse) {
        for (ContactListener listener : contactListeners) {
            listener.postSolve(contact, impulse);
        }
    }

    public void setActive(boolean value) {
        body.setActive(value);
    }

    public void setAwake(boolean value) {
        body.setAwake(value);
    }

    public BodyDef.BodyType getBodyType() {
        return body.getType();
    }
    
    public float getLinearDamping(){
    	return body.getLinearDamping();
    }
    
    public void setLinearDamping(float linearDamping){
    	body.setLinearDamping(linearDamping);
    }
    
    public void scale(float scale){
    	for (Fixture f : getFixtureList()) {
			Shape shape = f.getShape();
			float s = shape.getRadius();
			shape.setRadius(s * scale);
			
    	}
    }
}
