package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import de.hochschuletrier.gdw.commons.utils.Point;
import java.util.List;

/**
 *
 * @author Santo Pfingsten
 */
public class PhysixFixtureDef extends FixtureDef {

    private static final Vector2 dummyVector = new Vector2();

    private final PhysixManager manager;

    public PhysixFixtureDef(PhysixManager manager) {
        this.manager = manager;
    }

    /** The friction coefficient, usually in the range [0,1]. **/
    public PhysixFixtureDef friction(float value) {
        friction = value;
        return this;
    }

    /** The restitution (elasticity) usually in the range [0,1]. **/
    public PhysixFixtureDef restitution(float value) {
        restitution = value;
        return this;
    }

    /** The density, usually in kg/m^2. **/
    public PhysixFixtureDef density(float value) {
        density = value;
        return this;
    }

    /** A sensor shape collects contact information but never generates a collision response. */
    public PhysixFixtureDef sensor(boolean value) {
        isSensor = value;
        return this;
    }

    /** The collision category bits. Normally you would just set one bit. */
    public PhysixFixtureDef category(short bits) {
        filter.categoryBits = bits;
        return this;
    }

    /** The collision mask bits. This states the categories that this shape would accept for collision. */
    public PhysixFixtureDef mask(short bits) {
        filter.maskBits = bits;
        return this;
    }

    /** Collision groups allow a certain group of objects to never collide (negative) or always collide (positive). Zero means no
     * collision group. Non-zero group filtering always wins against the mask bits. */
    public PhysixFixtureDef groupIndex(short bits) {
        filter.groupIndex = bits;
        return this;
    }

    public PhysixFixtureDef shapeBox(float width, float height) {
        return shapeBox(width, height, Vector2.Zero, 0);
    }

    public PhysixFixtureDef shapeBox(float width, float height, Vector2 center, float angle) {
        float halfWidth = manager.toBox2D(width) * 0.5f;
        float halfHeight = manager.toBox2D(height) * 0.5f;

        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(halfWidth, halfHeight, manager.toBox2D(center, dummyVector), angle);
        this.shape = polyShape;
        return this;
    }

    public PhysixFixtureDef shapeCircle(float radius) {
        return shapeCircle(radius, Vector2.Zero);
    }

    public PhysixFixtureDef shapeCircle(float radius, Vector2 center) {
        radius = manager.toBox2D(radius);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        circleShape.setPosition(manager.toBox2D(center, dummyVector));
        this.shape = circleShape;
        return this;
    }

    public PhysixFixtureDef shapePolygon(List<Point> points) {
        PolygonShape polyShape = new PolygonShape();
        polyShape.set(manager.toBox2D(points));
        this.shape = polyShape;
        return this;
    }

    public PhysixFixtureDef shapePolyline(List<Point> points) {
        ChainShape chainShape = new ChainShape();
        chainShape.createChain(manager.toBox2D(points));
        this.shape = chainShape;
        return this;
    }
}
