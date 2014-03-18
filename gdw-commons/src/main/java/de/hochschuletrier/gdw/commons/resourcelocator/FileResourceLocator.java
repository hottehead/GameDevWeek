package de.hochschuletrier.gdw.commons.resourcelocator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Santo Pfingsten
 */
public class FileResourceLocator implements IResourceLocator {

    @Override
    public InputStream readResource(String filename)
            throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(new File(filename)));
    }

    @Override
    public OutputStream writeResource(String filename)
            throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(new File(filename)));
    }

    @Override
    public String combinePaths(String base, String filename) {
        File file = new File(new File(base).getParentFile(), filename);
        return file.toString();
    }
}
