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
    private final TextureRegion regionClipped = new TextureRegion();

    public SplitVerticalTransition(int duration) {
        super(duration);
    }

    @Override
    public void render(TextureRegion fromRegion, TextureRegion toRegion) {
        int fullWidth = Gdx.graphics.getWidth();
        int halfHeight = Gdx.graphics.getHeight() / 2;
        int yOffset = Math.round(getProgress() * halfHeight);
        int drawHeight = halfHeight - yOffset;

        DrawUtil.batch.draw(toRegion, 0, 0, toRegion.getRegionWidth(), toRegion.getRegionHeight());
        
        regionClipped.setTexture(fromRegion.getTexture());
        
        regionClipped.setRegion(0, halfHeight, fullWidth, drawHeight);
        DrawUtil.batch.draw(regionClipped, 0, 0, fullWidth, drawHeight);
        
        regionClipped.setRegion(0, yOffset, fullWidth, drawHeight);
        DrawUtil.batch.draw(regionClipped, 0, halfHeight + yOffset, fullWidth, drawHeight);
    }
}
