package de.hochschuletrier.gdw.commons.devcon;

/**
 * To add your own ones, create a class with static integers, initialized with CmdFlags.getBit()
 *
 * @author Santo Pfingsten
 */
public class CCmdFlags {

    private static int bitCounter;

    public static int getBit() {
        assert(bitCounter <= 32);
        return 1 << (bitCounter++);
    }

    public static final int ALL         = -1;       // Covers all CVar flags

    // Commands that can only be called under Certain conditions
    public static final int INIT        = getBit(); // Must be called from the commandline
    public static final int CHEAT       = getBit(); // Cheats must be enabled to call this
    public static final int DEVELOPER   = getBit(); // Must be in developer mode to see and call this command

    public static final int SYSTEM      = getBit(); // A System Command
}
