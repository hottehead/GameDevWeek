package de.hochschuletrier.gdw.commons.devcon.completers;

import java.util.List;

/**
 * Console command/argument completion interface
 *
 * @author Santo Pfingsten
 */
public interface IConsoleCompleter {

    /**
     * Completes the command or argument
    
     * @param prefix The prefix to complete
     * @param results the list to add to
     */
    void complete(String prefix, List<String> results);
}
