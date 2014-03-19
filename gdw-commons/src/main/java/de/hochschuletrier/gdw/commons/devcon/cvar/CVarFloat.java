package de.hochschuletrier.gdw.commons.devcon.cvar;

import de.hochschuletrier.gdw.commons.utils.QuietUtils;
import java.util.Locale;

/**
 *
 * @author Santo Pfingsten
 */
public class CVarFloat extends CVar {

    float value;
    final float defaultValue;
    final float min;
    final float max;

    public CVarFloat(String name, float defaultValue, float min, float max, int flags, String description) {
        super(name, flags, description, null);
        this.value = this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
    }

    public void set(float value, boolean force) {
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
        set(QuietUtils.parseFloat(s, defaultValue), force);
    }

    public void add(float amount, boolean force) {
        set(get() + amount, force);
    }

    @Override
    public void reset(boolean force) {
        set(defaultValue, force);
    }

    public float get() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public String getTypeDescription() {
        return String.format(Locale.ENGLISH, "float [%.2f - %.2f]", min, max);
    }

    @Override
    public String getDefaultValue() {
        return Float.toString(defaultValue);
    }
}
