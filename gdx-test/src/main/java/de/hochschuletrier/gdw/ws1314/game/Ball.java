package de.hochschuletrier.gdw.ws1314.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;

/**
 *
 * @author Santo Pfingsten
 */
public class Ball extends PhysixEntity {

    private final Vector2 origin = new Vector2();
    private final float radius;

    public Ball(float x, float y, float radius) {
        origin.set(x, y);
        this.radius = radius;
    }

    public void initPhysics(PhysixManager manager) {
        PhysixBody body = new PhysixBodyDef(BodyType.DynamicBody, manager).position(origin)
                .fixedRotation(false).create();
        body.createFixture(new PhysixFixtureDef(manager).density(5).friction(0.2f).restitution(0.4f).shapeCircle(radius));
        setPhysicsBody(body);
    }
}
