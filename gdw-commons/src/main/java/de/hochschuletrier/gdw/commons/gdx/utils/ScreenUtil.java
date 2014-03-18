package de.hochschuletrier.gdw.commons.gdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;

/**
 * Helper class to toggle fullscreen mode
 *
 * @author Santo Pfingsten
 */
public class ScreenUtil {

    public static void toggleFullscreen() {

        if (Gdx.graphics.isFullscreen()) {
            // fixme: last window size ?
            Gdx.graphics.setDisplayMode(1024, 768, false);
        } else {
            Graphics.DisplayMode mode = Gdx.graphics.getDesktopDisplayMode();
            Gdx.graphics.setDisplayMode(mode.width, mode.height, true);
        }
    }

    public static void setFullscreen(boolean fullscreen) {
        if (fullscreen != Gdx.graphics.isFullscreen()) {
            toggleFullscreen();
        }
    }
}
