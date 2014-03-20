package de.hochschuletrier.gdw.commons.gdx.assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class AnimationExtended {

	public enum PlayMode {
		NORMAL, REVERSED, LOOP, LOOP_REVERSED, LOOP_PINGPONG, LOOP_RANDOM
	}

	final TextureRegion[] keyFrames;
	float[] frameDurations;
	public float animationDuration;
	private PlayMode playMode;

	public AnimationExtended(PlayMode playMode, float[] frameDurations,
			TextureRegion... keyFrames) {
		this.keyFrames = keyFrames;
		this.frameDurations = frameDurations;
		this.playMode = playMode;
		for (float f : frameDurations) {
			animationDuration += f;
		}
	}

	public TextureRegion getKeyFrame(float stateTime) {
		int frameNumber = getKeyFrameIndex(stateTime);
		return keyFrames[frameNumber];
	}

	public int getKeyFrameIndex(float stateTime) {
		if (keyFrames.length == 1)
			return 0;

		stateTime %= animationDuration;
		int frameNumber = 0;
		float tmp=0f;
		for (int i=0; i<frameDurations.length-1 ;i++) {
			float upperBounds = tmp+frameDurations[i];
			if(stateTime>=tmp && stateTime<=upperBounds) {
				frameNumber = i;
				return frameNumber;
			}
			tmp += frameDurations[i];
		}

		// switch (playMode) {
		// case NORMAL:
		// frameNumber = Math.min(keyFrames.length - 1, frameNumber);
		// break;
		// case LOOP:
		// frameNumber = frameNumber % keyFrames.length;
		// break;
		// case LOOP_PINGPONG:
		// frameNumber = frameNumber % ((keyFrames.length * 2) - 2);
		// if (frameNumber >= keyFrames.length)
		// frameNumber = keyFrames.length - 2 - (frameNumber -
		// keyFrames.length);
		// break;
		// case LOOP_RANDOM:
		// frameNumber = MathUtils.random(keyFrames.length - 1);
		// break;
		// case REVERSED:
		// frameNumber = Math.max(keyFrames.length - frameNumber - 1, 0);
		// break;
		// case LOOP_REVERSED:
		// frameNumber = frameNumber % keyFrames.length;
		// frameNumber = keyFrames.length - frameNumber - 1;
		// break;
		//
		// default:
		// // play normal otherwise
		// frameNumber = Math.min(keyFrames.length - 1, frameNumber);
		// break;
		// }

		return frameNumber;
	}
}
