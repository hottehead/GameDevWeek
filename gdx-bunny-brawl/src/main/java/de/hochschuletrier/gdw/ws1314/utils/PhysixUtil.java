package de.hochschuletrier.gdw.ws1314.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;

/**
 *
 * @author Santo Pfingsten
 */
public class PhysixUtil {

    public static void createHollowCircle(PhysixManager manager, float x, float y, float radius, int sides, float sideStrength) {
        float sideLength = (float) (2 * radius * Math.tan(Math.PI / (float) sides));
        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.StaticBody, manager).position(x, y).create();

        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(manager).restitution(0).friction(1);
        for (int i = 0; i < sides; i++) {
            float angle = (float) (2 * Math.PI / sides * i);
            Vector2 center = new Vector2((float) (radius * Math.cos(angle)), (float) (radius * Math.sin(angle)));
            fixtureDef.shapeBox(sideStrength, sideLength, center, angle);
            body.createFixture(fixtureDef);
        }
    }

    public static void createCapsuleBody(PhysixBody body, PhysixFixtureDef fixtureDef, float length, float radius) {
        Vector2 center = new Vector2(0, -(length + radius) * 0.5f);
        fixtureDef.shapeBox(radius * 2, length, center, 0);
        body.createFixture(fixtureDef);

        Vector2 pos = new Vector2(center.x, center.y - length * 0.5f);
        body.createFixture(fixtureDef.shapeCircle(radius, pos));
        pos.y += length;
        body.createFixture(fixtureDef.shapeCircle(radius, pos));
    }
}
