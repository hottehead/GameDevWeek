package de.hochschuletrier.gdw.commons.devcon;

/**
 * To add your own ones, create a class with static integers, initialized with CVarFlags.getBit()
 *
 * @author Santo Pfingsten
 */
public class CVarFlags {

    private static int bitCounter;

    public static int getBit() {
        assert (bitCounter <= 32);
        return 1 << (bitCounter++);
    }

    public static final int ALL         = -1;       // Covers all CVar flags

    // CVars that can only be changed under Certain conditions
    public static final int ROM         = getBit(); // The end user can not change this CVar
    public static final int INIT        = getBit(); // Must be set via commandline arguments during startup
    public static final int CHEAT       = getBit(); // Cheats must be enabled to change this CVar
    public static final int DEVELOPER   = getBit(); // Must be in developer mode to see and change this CVar

    public static final int SYSTEM      = getBit(); // A System CVar
}
