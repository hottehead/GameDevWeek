package de.hochschuletrier.gdw.commons.utils;

/**
 * Helps calculating a smooth Frames Per Second display
 *
 * @author Santo Pfingsten
 */
public class FpsCalculator {

    private int index = 0;
    private int sum = 0;
    private final long[] fpsFrames;
    private long lastTime = -1;
    private final int waitTime;
    private int nextUpdate;
    private float fpsCount;

    /**
     * @param waitTime time to wait before updating the time, to avoid flickering
     * @param cacheSize number of frames to cache
     * @param startValue the start value of all cache items (in milliseconds)
     */
    public FpsCalculator(int waitTime, int cacheSize, int startValue) {
        this.waitTime = waitTime;
        this.nextUpdate = 0;
        fpsFrames = new long[cacheSize];
        for(int i=0; i<cacheSize; i++) {
            fpsFrames[i] = startValue;
        }
        sum = cacheSize * startValue;
    }

    public float getFps() {
        return fpsCount;
    }

    public void addFrame() {
        long time = System.currentTimeMillis();
        if (lastTime == -1) {
            lastTime = time;
        }

        long delta = time - lastTime;
        lastTime = time;

        sum -= fpsFrames[index];
        sum += delta;
        fpsFrames[index] = delta;
        if (++index == fpsFrames.length) {
            index = 0;
        }

        nextUpdate -= delta;
        if (nextUpdate <= 0) {
            nextUpdate = waitTime;
            fpsCount = 1000 / Math.max(1, (sum / (float) fpsFrames.length));
        }
    }
}
