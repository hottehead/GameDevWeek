package de.hochschuletrier.gdw.commons.devcon.completers;

import java.util.List;

/**
 * Console command argument completion for enumerations
 *
 * @author Santo Pfingsten
 */
public class EnumCompleter<T extends Enum> implements IConsoleCompleter {

    Class<T> clazz;

    public EnumCompleter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void complete(String prefix, List<String> results) {
        for (T o : clazz.getEnumConstants()) {
            if (o.name().startsWith(prefix)) {
                results.add(o.name());
            }
        }
    }
}
