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
    private final TextureRegion regionClipped = new TextureRegion();

    public SplitHorizontalTransition(int duration) {
        super(duration);
    }

    @Override
    public void render(TextureRegion fromRegion, TextureRegion toRegion) {
        int halfWidth = Gdx.graphics.getWidth() / 2;
        int fullHeight = Gdx.graphics.getHeight();
        int xOffset = Math.round(getProgress() * halfWidth);
        int drawWidth = halfWidth - xOffset;
        
        DrawUtil.batch.draw(toRegion, 0, 0, toRegion.getRegionWidth(), toRegion.getRegionHeight());
        
        regionClipped.setTexture(fromRegion.getTexture());
        
        regionClipped.setRegion(xOffset, 0, drawWidth, fullHeight);
        DrawUtil.batch.draw(regionClipped, 0, 0, drawWidth, fullHeight);
        
        regionClipped.setRegion(halfWidth, 0, drawWidth, fullHeight);
        DrawUtil.batch.draw(regionClipped, halfWidth + xOffset, 0, drawWidth, fullHeight);
    }
}
