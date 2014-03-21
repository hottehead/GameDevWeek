package de.hochschuletrier.gdw.commons.gdx.assets;

import java.util.Map.Entry;
import java.util.TreeMap;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class AnimationExtended {

	public enum PlayMode {
		NORMAL, REVERSED, LOOP, LOOP_REVERSED, LOOP_PINGPONG, LOOP_RANDOM
	}

	final TextureRegion[] keyFrames;
	float[] frameDurations;
	Frame[] frameTimes;
	public float animationDuration = 0f;
	private PlayMode playMode;
	TreeMap<Frame, Integer> frames = new TreeMap<AnimationExtended.Frame, Integer>();
	Frame current = new Frame(0, 0);
	

	public AnimationExtended(PlayMode playMode, float[] frameDurations,
			TextureRegion... keyFrames) {
		this.keyFrames = keyFrames;
		this.frameDurations = frameDurations;
		this.playMode = playMode;
		int index = 0;
		
		for(TextureRegion t : keyFrames) {
			t.flip(false, true);
		}
		
		for (float f : frameDurations) {
			frames.put(new Frame(animationDuration, f), index);
			animationDuration += f;
			++index;
		}
	}

	public TextureRegion getKeyFrame(float stateTime) {
		int frameNumber = getKeyFrameIndex(stateTime);
		if(keyFrames[frameNumber]==null) {
			System.out.println("walking.getKeyFrame(stateTime) in Method render throws NullPointer");
		}
		return keyFrames[frameNumber];
	}

	public int getKeyFrameIndex(float stateTime) {
		if (keyFrames.length == 1)
			return 0;

		Integer frameNumber = new Integer(0);
		current.startTime = stateTime;
		float lowerBound = 0f;
		
		Entry<Frame, Integer> fetchedEntry = frames.floorEntry(current);
		frameNumber = fetchedEntry.getValue();
		switch (playMode) {
		case NORMAL:
			frameNumber = Math.min(frames.keySet().size() - 1, frameNumber);
			break;
		case LOOP:
			current.startTime = stateTime % animationDuration;
			// sucht höchst kleinsten key zu current
			fetchedEntry = frames.floorEntry(current);
			frameNumber = fetchedEntry.getValue();
			// frameNumber = frames.floorEntry(current).getValue();
			break;
		case LOOP_PINGPONG:
			current.startTime = stateTime % (animationDuration * 2);
			fetchedEntry = frames.floorEntry(current);
			frameNumber = fetchedEntry.getValue();
			if (current.startTime >= animationDuration) {
				Frame backFrame = new Frame(current.startTime, 0);
				backFrame.startTime = current.startTime - animationDuration;
				fetchedEntry = frames.floorEntry(backFrame);
				frameNumber = fetchedEntry.getValue();
				
			}
			break;
		case LOOP_RANDOM:
			frameNumber = MathUtils.random(keyFrames.length - 1);
			break;
		case REVERSED:
			current.startTime = animationDuration - stateTime % animationDuration;
			fetchedEntry = frames.floorEntry(current);
			frameNumber = fetchedEntry.getValue();
			frameNumber = Math.max(keyFrames.length - frameNumber - 1, 0);
			break;
		case LOOP_REVERSED:
			current.startTime = animationDuration - (stateTime % animationDuration);
			fetchedEntry = frames.floorEntry(current);
			frameNumber = frames.floorEntry(current).getValue();
			// frameNumber = keyFrames.length - frameNumber - 1;
			break;

		default:
			// play normal otherwise
			frameNumber = Math.min(keyFrames.length - 1, frameNumber);
			break;
		}

		return frameNumber.intValue();
	}

	class Frame implements Comparable<Frame> {
		float startTime;
		float duration;
		int hash;

		public Frame(float startTime, float duration) {
			this.startTime = startTime;
			this.duration = duration;
			hash = Float.floatToIntBits(startTime);
		}

		@Override
		public boolean equals(Object obj) {
			Frame f = (Frame) obj;
			return f.startTime >= this.startTime
					&& f.startTime < this.duration + this.startTime;
		}

		protected void setEntry(float val) {
			startTime = val;
			hash = Float.floatToIntBits(val);
		}

		@Override
		public String toString() {
			return "[" + startTime + ", " + duration + "]";
		}

		/*
		 * Compares this object with the specified object for order. Returns a
		 * negative integer, zero, or a positive integer as this object is less
		 * than, equal to, or greater than the specified object.
		 */
		@Override
		public int compareTo(Frame f) { // this ist current selber
			if (f.equals(this)) {// current im bereich
			// System.out.println("equals " + this + " ~ " + f);
				return 0;
			}
			if (this.startTime + this.duration > f.startTime + f.duration) { // current
			// System.out.println("greater " + this + " ~ " + f);
				return +1;
			}
			if (this.startTime + this.duration < f.startTime + f.duration) {
				// System.out.println("less " + this + " ~ " + f);
				return -1;
			}
			// System.out.println("undefined for " + this + " and " + f);
			return 1;
		}
	}
}
