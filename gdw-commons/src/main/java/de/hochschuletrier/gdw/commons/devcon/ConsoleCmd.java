package de.hochschuletrier.gdw.commons.devcon;

import de.hochschuletrier.gdw.commons.devcon.completers.IConsoleCompleter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for a console command
 *
 * @author Santo Pfingsten
 */
public abstract class ConsoleCmd implements IConsoleCompleter {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleCmd.class);

    /** The name of the command (as used in the console) */
    protected final String name;
    /** The flags of the command */
    protected int flags;
    /** The minimum number of arguments to call */
    protected final int minArguments;
    /** The description printed by the help command */
    protected final String description;

    /**
     * @param name The name use in the console
     * @param flags Flags (@see CCmdFlags)
     * @param description The description printed by the help command
     * @param minArguments The minimum number of arguments to call
    */
    public ConsoleCmd(String name, int flags, String description, int minArguments) {
        this.name = name;
        this.flags = flags;
        this.description = description;
        this.minArguments = minArguments;
    }

    /**
     * @param name The name use in the console
     * @param flags Flags (@see CCmdFlags)
     * @param description The description printed by the help command
    */
    public ConsoleCmd(String name, int flags, String description) {
        this(name, flags, description, 0);
    }

    public abstract void execute(List<String> args);

    public String getName() {
        return name;
    }

    public int getFlags() {
        return flags;
    }

    public int getMinArguments() {
        return minArguments;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Shows the usage string (minimum arguments by default).
     * 
     * Override this if you want to show usage text
     */
    public void showUsage() {
        if (minArguments != 0) {
            showUsage(minArguments + " arguments");
        }
    }

    protected void showUsage(String arguments) {
        logger.info("Usage: {} {}", name, arguments);
    }

    @Override
    public void complete(String arg, List<String> results) {
    }

    public boolean isCallable(boolean log) {
        if ((flags & CCmdFlags.CHEAT) != 0 && !DevConsole.com_allowCheats.get()) {
            if (log) {
                logger.warn("{} is cheat protected", name);
            }
            return false;
        }
        if ((flags & CCmdFlags.DEVELOPER) != 0 && !DevConsole.com_developer.get()) {
            if (log) {
                logger.warn("{} is for developers only", name);
            }
            return false;
        }
        return true;
    }
}
