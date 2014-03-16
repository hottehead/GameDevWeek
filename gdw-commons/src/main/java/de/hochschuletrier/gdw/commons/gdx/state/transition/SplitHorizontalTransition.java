package de.hochschuletrier.gdw.commons.gdx.state.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

/**
 * Split the screen horizontally (renders the splitting state twice)
 *
 * @author Santo Pfingsten
 */
public class SplitHorizontalTransition extends Transition<SplitHorizontalTransition> {

    public SplitHorizontalTransition(int fadeTime) {
        super(fadeTime);
    }

    @Override
    public void render(TextureRegion fromRegion, TextureRegion toRegion) {
        int halfWidth = Gdx.graphics.getWidth() / 2;
        int fullHeight = Gdx.graphics.getHeight();
        int xOffset = Math.round(getProgress() * halfWidth);
        
        DrawUtil.batch.draw(toRegion, 0, 0, toRegion.getRegionWidth(), toRegion.getRegionHeight());

        DrawUtil.setClip(0, 0, halfWidth - xOffset, fullHeight);
        DrawUtil.pushTransform();
        DrawUtil.translate(-xOffset, 0);
        DrawUtil.batch.draw(fromRegion, 0, 0, fromRegion.getRegionWidth(), fromRegion.getRegionHeight());
        DrawUtil.popTransform();

        DrawUtil.setClip(halfWidth + xOffset, 0, halfWidth, fullHeight);
        DrawUtil.pushTransform();
        DrawUtil.translate(xOffset, 0);
        DrawUtil.batch.draw(fromRegion, 0, 0, fromRegion.getRegionWidth(), fromRegion.getRegionHeight());
        DrawUtil.popTransform();
        DrawUtil.clearClip();
    }
}
