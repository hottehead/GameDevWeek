package de.hochschuletrier.gdw.commons.devcon.cvar;

/**
 *
 * @author Santo Pfingsten
 */
public class CVarString extends CVar {

    private String value;
    private final String defaultValue;

    public CVarString(String name, String defaultValue, int flags, String description) {
        super(name, flags, description, null);
        this.value = this.defaultValue = defaultValue;
    }

    public String get() {
        return value;
    }

    @Override
    public void reset(boolean force) {
        set(defaultValue, force);
    }

    @Override
    public void set(String value, boolean force) {
        if (prepareSet(force) && !this.value.equals(value)) {
            this.value = value;
            notifyListeners();
        }
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String getTypeDescription() {
        return "string";
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }
}
