package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 *
 * @author Santo Pfingsten
 */
public class PhysixBodyDef extends BodyDef {

    private final PhysixManager manager;

    public PhysixBodyDef(BodyType type, PhysixManager manager) {
        this.type = type;
        this.manager = manager;
    }

    /** The body type: static, kinematic, or dynamic. Note: if a dynamic body would have zero mass, the mass is set to one. **/
    public PhysixBodyDef type(BodyType value) {
        type = value;
        return this;
    }

    /** The world position of the body. Avoid creating bodies at the origin since this can lead to many overlapping shapes. **/
    public PhysixBodyDef position(Vector2 p) {
        position.set(manager.toBox2D(p.x), manager.toBox2D(p.y));
        return this;
    }

    public PhysixBodyDef position(float x, float y) {
        position.set(manager.toBox2D(x), manager.toBox2D(y));
        return this;
    }

    /** The world angle of the body in radians. **/
    public PhysixBodyDef angle(float value) {
        angle = value;
        return this;
    }

    /** The linear velocity of the body's origin in world co-ordinates. **/
    public PhysixBodyDef angularVelocity(Vector2 value) {
        linearVelocity.set(value);
        return this;
    }

    /** The angular velocity of the body. **/
    public PhysixBodyDef angularVelocity(float value) {
        angularVelocity = value;
        return this;
    }

    /** Linear damping is use to reduce the linear velocity. The damping parameter can be larger than 1.0f but the damping effect
     * becomes sensitive to the time step when the damping parameter is large. **/
    public PhysixBodyDef linearDamping(float value) {
        linearDamping = value;
        return this;
    }

    /** Angular damping is use to reduce the angular velocity. The damping parameter can be larger than 1.0f but the damping effect
     * becomes sensitive to the time step when the damping parameter is large. **/
    public PhysixBodyDef angularDamping(float value) {
        angularDamping = value;
        return this;
    }

    /** Set this flag to false if this body should never fall asleep. Note that this increases CPU usage. **/
    public PhysixBodyDef allowSleep(boolean value) {
        allowSleep = value;
        return this;
    }

    /** Is this body initially awake or sleeping? **/
    public PhysixBodyDef awake(boolean value) {
        awake = value;
        return this;
    }

    /** Should this body be prevented from rotating? Useful for characters. **/
    public PhysixBodyDef fixedRotation(boolean value) {
        fixedRotation = value;
        return this;
    }

    /** Is this a fast moving body that should be prevented from tunneling through other moving bodies? Note that all bodies are
     * prevented from tunneling through kinematic and static bodies. This setting is only considered on dynamic bodies.
     * @warning You should use this flag sparingly since it increases processing time. **/
    public PhysixBodyDef bullet(boolean value) {
        bullet = value;
        return this;
    }

    /** Does this body start out active? **/
    public PhysixBodyDef active(boolean value) {
        active = value;
        return this;
    }

    /** Scale the gravity applied to this body. **/
    public PhysixBodyDef gravityScale(float value) {
        gravityScale = value;
        return this;
    }

    public PhysixBody create() {
        return new PhysixBody(this, manager);
    }
}
