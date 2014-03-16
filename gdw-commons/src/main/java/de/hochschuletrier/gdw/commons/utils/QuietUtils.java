package de.hochschuletrier.gdw.commons.utils;

import java.io.Closeable;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for quietly doing things (ignoring exceptions)
 *
 * @author Santo Pfingsten
 */
public class QuietUtils {
    private static final Logger logger = LoggerFactory.getLogger(QuietUtils.class);

    public static void sleep(int ms) {
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            logger.warn("Sleep interrupted", e);
        }
    }
    
    public static void close(Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                logger.warn("Failed to close closeable", e);
            }
        }
    }
    
    public static float parseFloat(String value, float defaultValue) {
        try {
            return Float.parseFloat(value);
        } catch(NumberFormatException e) {
            logger.warn("Failed to parse float", e);
            return defaultValue;
        }
    }
    
    public static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            logger.warn("Failed to parse integer", e);
            return defaultValue;
        }
    }
}
