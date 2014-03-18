package de.hochschuletrier.gdw.commons.devcon.cvar;

import de.hochschuletrier.gdw.commons.utils.QuietUtils;

/**
 *
 * @author Santo Pfingsten
 */
public class CVarInt extends CVar {

    int value;
    final int defaultValue;
    final int min;
    final int max;

    public CVarInt(String name, int defaultValue, int min, int max, int flags, String description) {
        super(name, flags, description, null);
        this.value = this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
    }

    public void set(int value, boolean force) {
        if (prepareSet(force) && this.value != value) {
            if (value < min) {
                value = min;
            } else if (value > max) {
                value = max;
            }
            this.value = value;
            notifyListeners();
        }
    }

    @Override
    public void set(String s, boolean force) {
        set(QuietUtils.parseInt(s, defaultValue), force);
    }

    public void add(int amount, boolean force) {
        set(get() + amount, force);
    }

    @Override
    public void reset(boolean force) {
        set(defaultValue, force);
    }

    public int get() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public String getTypeDescription() {
        return String.format("int [%d - %d]", min, max);
    }

    @Override
    public String getDefaultValue() {
        return Integer.toString(defaultValue);
    }
}
