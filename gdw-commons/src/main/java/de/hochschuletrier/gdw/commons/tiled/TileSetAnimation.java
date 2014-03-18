package de.hochschuletrier.gdw.commons.tiled;

public class TileSetAnimation {
    public final int numFrames;
    public final float frameDuration;
    public final int tileOffset;
    
    public TileSetAnimation(int numFrames, float frameDuration, int tileOffset) {
        this.numFrames = numFrames;
        this.frameDuration = frameDuration;
        this.tileOffset = tileOffset;
    }
}
