package de.hochschuletrier.gdw.commons.gdx.state.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

/**
 * An empty Transition and base class for other transitions
 *
 * @author Santo Pfingsten
 * @param <T> The final class
 */
public class Transition<T extends Transition> {

    private int duration;
    private boolean reverse;
    private float progress = 0;
    private FrameBuffer fromFbo;
    private TextureRegion fromFboRegion;
    private FrameBuffer toFbo;
    private TextureRegion toFboRegion;

    public Transition() {
        this(0);
    }

    protected Transition(int duration) {
        if (duration > 0) {
            this.duration = duration;
        } else {
            this.duration = 1;
            progress = 1;
        }
        createFbos();
    }
    
    private void createFbos() {
        fromFbo = new FrameBuffer(Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        fromFboRegion = new TextureRegion(fromFbo.getColorBufferTexture());
        toFbo = new FrameBuffer(Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        toFboRegion = new TextureRegion(toFbo.getColorBufferTexture());
    }

    public float getProgress() {
        return reverse == false ? progress : (1.0f - progress);
    }

    public boolean isDone() {
        return (progress >= 1);
    }

    public boolean isReverse() {
        return reverse;
    }

    public T reverse() {
        reverse = !reverse;
        return (T) this;
    }

    public final void render(GameState from, GameState to) {
        fromFbo.begin();
        from.render();
        DrawUtil.batch.flush();
        fromFbo.end();
        toFbo.begin();
        to.render();
        DrawUtil.batch.flush();
        toFbo.end();

        render(fromFboRegion, toFboRegion);
    }
    
    public void render(TextureRegion fromRegion, TextureRegion toRegion) {
        DrawUtil.batch.draw(fromRegion, 0, 0, fromRegion.getRegionWidth(), fromRegion.getRegionHeight());
    }

    public void update(float delta) {
        if (progress < 1) {
            if (delta > 0.016f) {
                delta = 0.016f;
            }

            progress += delta * (1000.0f / duration);
            if (progress > 1) {
                progress = 1;
            }
        }
    }
}
