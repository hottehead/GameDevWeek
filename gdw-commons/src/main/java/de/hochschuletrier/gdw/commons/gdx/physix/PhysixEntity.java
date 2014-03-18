package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Santo Pfingsten
 */
public class PhysixEntity {

    protected PhysixBody physicsBody;

    protected void setPhysicsBody(PhysixBody physicsObject) {
        physicsObject.setOwner(this);
        this.physicsBody = physicsObject;
    }

    public Vector2 getPosition() {
        return physicsBody.getPosition();
    }

    public Vector2 getVelocity() {
        return physicsBody.getLinearVelocity();
    }

    public void setVelocity(Vector2 velocity) {
        physicsBody.setLinearVelocity(velocity);
    }

    public void setVelocity(float x, float y) {
        physicsBody.setLinearVelocity(x, y);
    }

    public void setVelocityX(float x) {
        physicsBody.setLinearVelocityX(x);
    }

    public void setVelocityY(float y) {
        physicsBody.setLinearVelocityY(y);
    }

}
