package de.hochschuletrier.gdw.commons.gdx.devcon;

import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import de.hochschuletrier.gdw.commons.devcon.CCmdFlags;
import de.hochschuletrier.gdw.commons.devcon.CVarFlags;
import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarString;
import de.hochschuletrier.gdw.commons.devcon.cvar.ICVarListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a view for the DevConsole on libgdx ui
 *
 * @author Santo Pfingsten
 */
public class DevConsoleView implements ScreenListener, EventListener, ICVarListener {

    private Stage stage;
    private final LinkedList<LogLabel> logLabels = new LinkedList<LogLabel>();
    private Table table;
    private LogList logList = new LogList();
    private Skin skin;
    private CommandField commandField;
    private ScrollPane scrollPane;
    private static int sheduleScrollToEnd;
    private final CVarString log_filter = new CVarString("log_filter", "DEBUG", CVarFlags.SYSTEM, "log levels to filter from the console");
    private final CVarFloat log_height = new CVarFloat("log_height", 0.33f, 0.1f, 1.0f, CVarFlags.SYSTEM, "log height in percent");
    private final HashSet<Level> visibleLevels = new HashSet<Level>(7);
    private final Level[] logLevels = {
        Level.OFF,
        Level.ERROR,
        Level.WARN,
        Level.INFO,
        Level.DEBUG,
        Level.TRACE,
        Level.ALL
    };

    private InputInterceptor inputProcessor;

    private final DevConsole console;

    public DevConsoleView(DevConsole console) {
        this.console = console;
        console.register(clear_f);
        console.register(log_filter);
        log_filter.addListener(this);
        modified(log_filter); // update visible filters
        console.register(log_height);
        log_height.addListener(this);
    }

    public void init(AssetManagerX assetManager, Skin skin) {
        this.skin = skin;

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        stage = new Stage(new StretchViewport(width, height), DrawUtil.batch);
        stage.addListener(this);

        inputProcessor = new InputInterceptor(stage) {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.F12) {
                    isActive = !isActive;
                    return true;
                }
                return isActive && mainProcessor.keyUp(keycode);
            }
        };

        // Create a table that fills the screen. Everything else will go inside this table.
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        addLogLabel(new LogLabel("Console Started\n", skin, Level.INFO));
        scrollPane = new ScrollPane(logList, skin);

        table.row().expand().fill();
        table.add(scrollPane).expand().fill();
        table.row().expandX().fillX();
        commandField = new CommandField("", skin);
        table.add(commandField).expandX().fillX();

        stage.setKeyboardFocus(commandField);
        adjustHeight();

        Logger logger = (Logger) LoggerFactory.getLogger("root");
        appender.start();
        logger.addAppender(appender);
    }

    private void scrollToStart() {
        scrollPane.setScrollY(0);
    }

    private void scrollToEnd() {
        scrollPane.scrollTo(0, scrollPane.getScrollBarHeight(), 0, 10);
    }

    private void adjustHeight() {
        float height = Gdx.graphics.getHeight();
        table.padBottom(height - (height * log_height.get()));
    }

    public void render() {
        stage.draw();

        Table.drawDebug(stage);
    }

    public void update(float delta) {
        stage.act(delta);
        if (sheduleScrollToEnd > 0) {
            sheduleScrollToEnd--;
            scrollToEnd();
        }
    }

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public void setVisible(boolean visible) {
        inputProcessor.setActive(visible);
    }

    public boolean isVisible() {
        return inputProcessor.isActive();
    }

    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    	adjustHeight();
        logList.invalidateHierarchy();
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof InputEvent) {
            InputEvent evt = (InputEvent) event;
            if (evt.getType() == InputEvent.Type.keyDown) {
                switch (evt.getKeyCode()) {
                    case Input.Keys.TAB:
                        String text = commandField.getText();
                        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
                            console.printUsage(text);
                        } else {
                            console.completeInput(text, commandField.getSelectionStart(), commandField.getCursorPosition(), commandField.getConsoleEditor());
                        }
                        return true;
                    case Input.Keys.UP:
                        console.historyBack(commandField.getText(), commandField.getSelectionStart(), commandField.getCursorPosition(), commandField.getConsoleEditor());
                        return true;
                    case Input.Keys.DOWN:
                        console.historyForward(commandField.getText(), commandField.getSelectionStart(), commandField.getCursorPosition(), commandField.getConsoleEditor());
                        return true;
                    case Input.Keys.ESCAPE:
                        setVisible(false);
                        return true;
                    case Input.Keys.PAGE_UP:
                        scrollToStart();
                        return true;
                    case Input.Keys.PAGE_DOWN:
                        scrollToEnd();
                        return true;
                    case Input.Keys.ENTER:
                        console.submitInput(commandField.getText(), commandField.getSelectionStart(), commandField.getCursorPosition(), commandField.getConsoleEditor());
                        sheduleScrollToEnd = 10;
                        return true;
                }
            }
        }
        return false;
    }

    private void addLogLabel(LogLabel log) {
        logList.add(log);
        log.setVisible(visibleLevels.contains(log.getLevel()));
        logLabels.addLast(log);
    }

    AppenderBase<ILoggingEvent> appender = new AppenderBase<ILoggingEvent>() {
        @Override
        protected void append(ILoggingEvent e) {
            LogLabel log = logLabels.getLast();
            if (log.getLevel() != e.getLevel()) {
                log = new LogLabel("", skin, e.getLevel());
                addLogLabel(log);
            }
            StringBuilder sb = (StringBuilder) log.getText();
            if (e.getLevel() == Level.INFO) {
                sb.append(e.getFormattedMessage());
            } else if (e.getLevel() == Level.ERROR) {
                sb.append("Error: ").append(e.getFormattedMessage());
            } else if (e.getLevel() == Level.WARN) {
                sb.append("Warning: ").append(e.getFormattedMessage());
            } else if (e.getLevel() == Level.DEBUG) {
                sb.append("Debug: ").append(e.getFormattedMessage());
            } else if (e.getLevel() == Level.TRACE) {
                sb.append("Trace: ").append(e.getFormattedMessage());
            }
            sb.append("\n");
            log.invalidateHierarchy();
            DevConsoleView.sheduleScrollToEnd = 10;
        }
    };

    @Override
    public void modified(CVar cvar) {
        if (cvar == log_filter) {
            visibleLevels.clear();
            String ucFilter = log_filter.get().toUpperCase();
            for (Level level : logLevels) {
                if (ucFilter.indexOf(level.toString()) == -1) {
                    visibleLevels.add(level);
                }
            }
            for (LogLabel log : logLabels) {
                log.setVisible(visibleLevels.contains(log.getLevel()));
            }
            logList.invalidateHierarchy();
        } else if(cvar == log_height) {
            adjustHeight();
        }
    }

    ConsoleCmd clear_f = new ConsoleCmd("clear", CCmdFlags.SYSTEM, "Clear Console.") {

        @Override
        public void execute(List<String> args) {
            logList.clear();
            logLabels.clear();
            logList.add(new Label("", skin));
            logList.invalidateHierarchy();
            addLogLabel(new LogLabel("Console Cleared\n", skin, Level.INFO));
        }
    };
}
