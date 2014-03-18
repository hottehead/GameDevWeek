package de.hochschuletrier.gdw.commons.devcon.cvar;

import de.hochschuletrier.gdw.commons.devcon.completers.BooleanCompleter;

/**
 *
 * @author Santo Pfingsten
 */
public class CVarBool extends CVar {

    private boolean value;
    private final boolean defaultValue;

    public CVarBool(String name, boolean defaultValue, int flags, String description) {
        super(name, flags, description, BooleanCompleter.getInstance());
        this.value = this.defaultValue = defaultValue;
    }

    public void set(boolean value, boolean force) {
        if (prepareSet(force) && this.value != value) {
            this.value = value;
            notifyListeners();
        }
    }

    public void toggle(boolean force) {
        set(!get(), force);
    }

    @Override
    public void set(String value, boolean force) {
        set(Boolean.parseBoolean(value), force);
    }

    @Override
    public void reset(boolean force) {
        set(defaultValue, force);
    }

    public boolean get() {
        return value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public String getTypeDescription() {
        return "boolean [true, false]";
    }

    @Override
    public String getDefaultValue() {
        return Boolean.toString(defaultValue);
    }
}
