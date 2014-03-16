package de.hochschuletrier.gdw.commons.gdx.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to get LibGDX key names from their index and vice versa
 *
 * @author Santo Pfingsten
 */
public class KeyUtil {
    private static final Logger logger = LoggerFactory.getLogger(KeyUtil.class);

    /**
     * Key names
     */
    private static final String[] keyName = new String[256];
    private static final Map<String, Integer> keyMap = new HashMap<String, Integer>(253);

    public static void init() {
        // Use reflection to find out key names
        Field[] fields = Input.Keys.class.getFields();
        try {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())
                        && Modifier.isPublic(field.getModifiers())
                        && Modifier.isFinal(field.getModifiers())
                        && field.getType().equals(int.class)) {

                    int key = field.getInt(null);
                    if (key >= 0) {
                        String name = field.getName();
                        if (!name.startsWith("META_")) {
                            keyName[key] = name;
                            keyMap.put(name, key);
                        }
                    }
                }

            }
        } catch (Exception e) {
            logger.error("Failed to read input key names", e);
        }

    }

    public static String getName(int key) {
        return keyName[key];
    }

    public static int getIndex(String keyName) {
        Integer ret = keyMap.get(keyName);
        return ret != null ? ret : -1;
    }
}
