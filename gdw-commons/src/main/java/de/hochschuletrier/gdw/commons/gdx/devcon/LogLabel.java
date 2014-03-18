package de.hochschuletrier.gdw.commons.gdx.devcon;

import ch.qos.logback.classic.Level;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

/**
 * Extended Label class containing a log-level
 *
 * @author Santo Pfingsten
 */
public class LogLabel extends Label {

    private final Level level;

    public LogLabel(String text, Skin skin, Level level) {
        super(text, skin, "console-" + level.toString().toLowerCase());

        this.level = level;

        setAlignment(Align.left | Align.bottom);
        setFillParent(true);
        setWrap(true);
    }

    public Level getLevel() {
        return level;
    }
}
