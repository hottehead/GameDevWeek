package de.hochschuletrier.gdw.commons.gdx.assets;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author Santo Pfingsten
 */
public class AnimationX {
	private Animation animation;
	private float stateTime;
	private TextureRegion[] frames;
	private boolean looping;

	public AnimationX(AnimationX other) {
		animation = other.animation;
		frames = other.frames;
	}

	public AnimationX(Texture texture, int rows, int cols, float frameTime, boolean loop) {
		int tileWidth = texture.getWidth() / cols;
		int tileHeight = texture.getHeight() / rows;
		TextureRegion[][] tmp = TextureRegion.split(texture, tileWidth, tileHeight);
		frames = new TextureRegion[cols * rows];
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				frames[index] = tmp[i][j];
				frames[index].flip(false, true);
				index++;
			}
		}
		animation = new Animation(frameTime * 0.001f, frames);
		animation.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);
		stateTime = 0f;
		looping = loop;
	}

	public void drawFrame(float x, float y, float width, float height, int frameIndex) {
		DrawUtil.batch.draw(frames[frameIndex], x, y, width, height);
	}

	public void drawFrame(float x, float y, int frameIndex) {
		drawFrame(x, y, getWidth(), getHeight(), frameIndex);
	}

	public void drawFrame(int frameIndex) {
		drawFrame(0, 0, getWidth(), getHeight(), frameIndex);
	}

	public void draw(float x, float y, float width, float height) {
		TextureRegion currentFrame = animation.getKeyFrame(stateTime, looping);
		DrawUtil.batch.draw(currentFrame, x, y, width, height);
	}

	public void update(float delta) {
		stateTime += delta;
	}

	public void draw(float x, float y) {
		draw(x, y, getWidth(), getHeight());
	}

	public void draw() {
		draw(0, 0, getWidth(), getHeight());
	}

	public boolean isStopped() {
		return animation.isAnimationFinished(stateTime);
	}

	public void restart() {
		stateTime = 0;
	}

	public TextureRegion getFrame(int i) {
		return frames[i];
	}

	public TextureRegion getCurrentFrame() {
		return animation.getKeyFrame(stateTime);
	}

	public float getWidth() {
		return frames[0].getRegionWidth();
	}

	public float getHeight() {
		return frames[0].getRegionHeight();
	}
}

