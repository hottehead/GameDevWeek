package de.hochschuletrier.gdw.commons.gdx.state.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

/**
 * Split the screen vertically (renders the splitting state twice)
 *
 * @author Santo Pfingsten
 */
public class SplitVerticalTransition extends Transition<SplitVerticalTransition> {

    public SplitVerticalTransition(int fadeTime) {
        super(fadeTime);
    }

    @Override
    public void render(TextureRegion fromRegion, TextureRegion toRegion) {
        int fullWidth = Gdx.graphics.getWidth();
        int halfHeight = Gdx.graphics.getHeight() / 2;
        int yOffset = Math.round(getProgress() * 0.5f * Gdx.graphics.getHeight());

        DrawUtil.batch.draw(toRegion, 0, 0, toRegion.getRegionWidth(), toRegion.getRegionHeight());

        DrawUtil.setClip(0, 0, fullWidth, halfHeight - yOffset);
        DrawUtil.pushTransform();
        DrawUtil.translate(0, -yOffset);
        DrawUtil.batch.draw(fromRegion, 0, 0, fromRegion.getRegionWidth(), fromRegion.getRegionHeight());
        DrawUtil.popTransform();

        DrawUtil.setClip(0, halfHeight + yOffset, fullWidth, halfHeight);
        DrawUtil.pushTransform();
        DrawUtil.translate(0, yOffset);
        DrawUtil.batch.draw(fromRegion, 0, 0, fromRegion.getRegionWidth(), fromRegion.getRegionHeight());
        DrawUtil.popTransform();
        DrawUtil.clearClip();
    }
}
