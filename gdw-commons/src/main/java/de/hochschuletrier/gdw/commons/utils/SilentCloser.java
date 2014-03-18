package de.hochschuletrier.gdw.commons.utils;

import java.io.Closeable;

/**
 * Helper object for quietly closing a Closeable (as a replacement for try-with-resources)
 * Example:
 *      SilentCloser closer = new SilentCloser();
 *      try {
 *          FileInputStream fileIn = new FileInputStream(filename);
 *          closer.set(fileIn);
 *          InputStreamReader inReader = new InputStreamReader(fileIn);
 *          closer.set(inReader);
 *          ...
 *      } finally {
 *          closer.close();
 *      }
 *
 * @author Santo Pfingsten
 */
public class SilentCloser {
    private Closeable closeable;
    
    public void close() {
        QuietUtils.close(closeable);
    }
    
    public void set(Closeable closeable) {
        this.closeable = closeable;
    }
}
