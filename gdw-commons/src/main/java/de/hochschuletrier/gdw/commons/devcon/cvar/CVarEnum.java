package de.hochschuletrier.gdw.commons.devcon.cvar;

import de.hochschuletrier.gdw.commons.devcon.completers.EnumCompleter;

/**
 *
 * @author Santo Pfingsten
 */
public class CVarEnum<T extends Enum> extends CVar {

    Class<T> clazz;
    T value;
    final T defaultValue;

    public CVarEnum(String name, T defaultValue, Class<T> clazz, int flags, String description) {
        super(name, flags, description, new EnumCompleter(clazz));
        this.value = this.defaultValue = defaultValue;
        this.clazz = clazz;
    }

    public void set(int value, boolean force) {
        if (prepareSet(force) && this.value.ordinal() != value) {
            T[] values = clazz.getEnumConstants();
            if (value < 0 || value > values.length - 1) {
                this.value = defaultValue;
            } else {
                this.value = values[value];
            }
            notifyListeners();
        }
    }

    public void set(T value, boolean force) {
        if (prepareSet(force) && this.value != value) {
            this.value = value;
            notifyListeners();
        }
    }

    @Override
    public void set(String value, boolean force) {
        if (prepareSet(force) && !this.value.name().equals(value)) {
            for (T o : clazz.getEnumConstants()) {
                if (value.equals(o.name())) {
                    this.value = o;
                    notifyListeners();
                    return;
                }
            }
            this.value = defaultValue;
            notifyListeners();
        }
    }

    public void toggle(boolean force) {
        if (value.ordinal() == (clazz.getEnumConstants().length - 1)) {
            set(0, force);
        } else {
            set(value.ordinal() + 1, force);
        }
    }

    @Override
    public void reset(boolean force) {
        set(defaultValue, force);
    }

    public T get() {
        return value;
    }

    @Override
    public String toString() {
        return value.name();
    }

    @Override
    public String getTypeDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("enum [");

        boolean first = true;
        for (T o : clazz.getEnumConstants()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(o.name());
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String getDefaultValue() {
        return defaultValue.name();
    }
}
