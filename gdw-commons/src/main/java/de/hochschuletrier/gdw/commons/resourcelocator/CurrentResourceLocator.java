package de.hochschuletrier.gdw.commons.resourcelocator;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Santo Pfingsten
 */
public class CurrentResourceLocator {

    /**
     * The resource locator
     */
    private static final IResourceLocator defaultLocator = new FileResourceLocator();
    private static IResourceLocator locator;

    public static void set(IResourceLocator locator) {
        CurrentResourceLocator.locator = locator;
    }

    /**
     * @return The current resource locator
     */
    public static IResourceLocator get() {
        return locator != null ? locator : defaultLocator;
    }

    public static InputStream read(String filename)
            throws FileNotFoundException {
        return get().readResource(filename);
    }

    public static OutputStream write(String filename)
            throws FileNotFoundException {
        return get().writeResource(filename);
    }

    public static String combinePaths(String base, String filename) {
        return get().combinePaths(base, filename);
    }
}
