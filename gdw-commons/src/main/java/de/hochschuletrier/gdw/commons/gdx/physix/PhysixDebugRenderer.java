package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

/**
 *
 * @author Santo Pfingsten
 */
public class PhysixDebugRenderer extends Box2DDebugRenderer {

    private final Matrix4 projectionMatrix = new Matrix4();

    public PhysixDebugRenderer() {
        setDrawBodies(true);
        setDrawInactiveBodies(true);
        setDrawContacts(true);
        setDrawJoints(true);
    }

    public void render(World world, float scale) {
        if (world != null) {
            DrawUtil.batch.end();
            projectionMatrix.set(DrawUtil.batch.getProjectionMatrix());
            projectionMatrix.scale(scale, scale, 1f);
            render(world, projectionMatrix);
            DrawUtil.batch.begin();
        }
    }
}
