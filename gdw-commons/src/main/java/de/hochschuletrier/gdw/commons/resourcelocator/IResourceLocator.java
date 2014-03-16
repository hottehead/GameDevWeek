package de.hochschuletrier.gdw.commons.resourcelocator;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Santo Pfingsten
 */
public interface IResourceLocator {

    InputStream readResource(String filename) throws FileNotFoundException;
    OutputStream writeResource(String filename) throws FileNotFoundException;

    String combinePaths(String base, String filename);
}
