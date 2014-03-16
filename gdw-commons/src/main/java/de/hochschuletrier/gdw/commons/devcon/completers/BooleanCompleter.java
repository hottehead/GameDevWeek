package de.hochschuletrier.gdw.commons.devcon.completers;

import java.util.List;

/**
 * Console command argument completion for booleans
 *
 * @author Santo Pfingsten
 */
public class BooleanCompleter implements IConsoleCompleter {

    private static final BooleanCompleter instance = new BooleanCompleter();

    public static IConsoleCompleter getInstance() {
        return instance;
    }

    private BooleanCompleter() {
    }

    @Override
    public void complete(String prefix, List<String> results) {
        if ("true".startsWith(prefix)) {
            results.add("true");
        }
        if ("false".startsWith(prefix)) {
            results.add("false");
        }
    }
}
