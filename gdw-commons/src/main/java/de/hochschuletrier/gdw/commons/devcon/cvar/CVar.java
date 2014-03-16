package de.hochschuletrier.gdw.commons.devcon.cvar;

import de.hochschuletrier.gdw.commons.devcon.CVarFlags;
import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.devcon.completers.IConsoleCompleter;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract console variable class
 *
 * @author Santo Pfingsten
 */
public abstract class CVar {

    private static final Logger logger = LoggerFactory.getLogger(CVar.class);

    protected final String name;
    protected final String description;
    protected int flags;
    protected boolean initialized;

    private final ArrayList<ICVarListener> listeners = new ArrayList<ICVarListener>();
    private final IConsoleCompleter completer;

    public CVar(String name, int flags, String description, IConsoleCompleter completer) {
        this.name = name;
        this.flags = flags;
        this.description = description;
        this.completer = completer;
    }

    public void addListener(ICVarListener l) {
        listeners.add(l);
    }

    public void removeListener(ICVarListener l) {
        listeners.remove(l);
    }

    protected void notifyListeners() {
        for (ICVarListener l : listeners) {
            l.modified(this);
        }
    }

    public boolean isWritable(boolean log) {
        if ((flags & (CVarFlags.ROM | CVarFlags.INIT)) != 0) {
            if (log) {
                logger.warn("{} is read only", name);
            }
            return false;
        }
        return isVisible(log);
    }

    public boolean isVisible(boolean log) {
        if ((flags & CVarFlags.CHEAT) != 0 && !DevConsole.com_allowCheats.get()) {
            if (log) {
                logger.warn("{} is cheat protected", name);
            }
            return false;
        }
        if ((flags & CVarFlags.DEVELOPER) != 0 && !DevConsole.com_developer.get()) {
            if (log) {
                logger.warn("{} is for developers only", name);
            }
            return false;
        }
        return true;
    }

    /**
     * @return the CVar flags
     */
    public int getFlags() {
        return flags;
    }

    /**
     * Set the CVar flags
     *
     * @param flags The new flags
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public IConsoleCompleter getCompleter() {
        return completer;
    }

    protected boolean prepareSet(boolean force) {
        if (!initialized) {
            initialized = true;
        } else if (!force && !isWritable(true)) {
            return false;
        }
        return true;
    }

    public abstract void reset(boolean force);

    public abstract void set(String value, boolean force);

    public abstract String getTypeDescription();

    public abstract String getDefaultValue();
}
