package de.hochschuletrier.gdw.commons.gdx.state.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

/**
 * Fade to/from a color (default black)
 *
 * @author Santo Pfingsten
 */
public class FadeTransition extends Transition<FadeTransition> {

    private Color color;
    private float threshold;

    public FadeTransition() {
        this(Color.BLACK, 500, 0.5f);
    }

    public FadeTransition(Color color) {
        this(color, 500, 0.5f);
    }

    public FadeTransition(Color color, int fadeTime) {
        this(color, fadeTime, 0.5f);
        
    }

    public FadeTransition(Color color, int fadeTime, float threshold) {
        super(fadeTime);

        this.color = new Color(color);
        this.threshold = threshold;
    }

    @Override
    public void render(TextureRegion fromRegion, TextureRegion toRegion) {
        float progress = getProgress();
        if(progress < threshold) {
            color.a = progress / threshold;
            DrawUtil.batch.draw(fromRegion, 0, 0, fromRegion.getRegionWidth(), fromRegion.getRegionHeight());
        } else {
            color.a = (1.0f - progress) / (1.0f-threshold);
            DrawUtil.batch.draw(toRegion, 0, 0, toRegion.getRegionWidth(), toRegion.getRegionHeight());
        }

        DrawUtil.setColor(color);
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}
