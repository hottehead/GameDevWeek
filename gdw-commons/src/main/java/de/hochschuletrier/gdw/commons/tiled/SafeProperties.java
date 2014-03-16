package de.hochschuletrier.gdw.commons.tiled;

import java.util.HashMap;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Alternative Properties storage to get some additional types without the need to manually check for parse exceptions
 *
 * @author Santo Pfingsten
 */
@XStreamAlias("properties")
public class SafeProperties {
    private static final Logger logger = LoggerFactory.getLogger(SafeProperties.class);

    @XStreamAlias("name")
    @XStreamAsAttribute
    String name;
    @XStreamImplicit(itemFieldName = "property", keyFieldName = "name")
    private HashMap<String, SafeProperty> properties = new HashMap<String, SafeProperty>();
    private SafeProperties defaults;

    private static class SafeProperty {

        @XStreamAlias("name")
        @XStreamAsAttribute
        private String name;
        @XStreamAlias("value")
        @XStreamAsAttribute
        private String value;

        public SafeProperty() {
        }

        public SafeProperty(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        private void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * Creates an empty property list with no default values.
     */
    public SafeProperties() {
    }

    /**
     * Creates an empty property list with the specified defaults.
     *
     * @param defaults the defaults.
     */
    public SafeProperties(SafeProperties defaults) {
        this.defaults = defaults;
    }

    /**
     * Set the default properties
     *
     * @param defaults the defaults.
     */
    public void setDefaults(SafeProperties defaults) {
        this.defaults = defaults;
    }

    /**
     * Get a property as an integer. If the property does not exist or can't be parsed to an integer the default value
     * will be returned. If it is no integer, the NumberFormatException will be printed to stdout.
     *
     * @param key the hashtable key.
     * @param defaultValue a default value.
     * @return the property value or defaultValue
     */
    public int getInt(String key, int defaultValue) {
        String value = getString(key, null);
        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn(key + " is not an integer: " + e.toString());
            return defaultValue;
        }
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public void setInt(String key, int value) {
        setString(key, "" + value);
    }

    /**
     * Get a property as a float. If the property does not exist or can't be parsed to a float the default value will be
     * returned. If it is no float, the NumberFormatException will be printed to stdout.
     *
     * @param key the hashtable key.
     * @param defaultValue a default value.
     * @return the property value or defaultValue
     */
    public float getFloat(String key, float defaultValue) {
        String value = getString(key, null);
        if (value == null) {
            return defaultValue;
        }

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            logger.warn(key + " is not a float: " + e.toString());
            return defaultValue;
        }
    }

    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    public void setFloat(String key, float value) {
        setString(key, "" + value);
    }

    /**
     * Get a property as a double. If the property does not exist or can't be parsed to a double the default value will
     * be returned. If it is no double, the NumberFormatException will be printed to stdout.
     *
     * @param key the hashtable key.
     * @param defaultValue a default value.
     * @return the property value or defaultValue
     */
    public double getDouble(String key, double defaultValue) {
        String value = getString(key, null);
        if (value == null) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            logger.warn(key + " is not a double: " + e.toString());
            return defaultValue;
        }
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public void setDouble(String key, double value) {
        setString(key, "" + value);
    }

    /**
     * Get a property as a boolean. If the property does not exist the default value will be returned. The value will be
     * matched against "true" or "1".
     *
     * @param key the hashtable key.
     * @param defaultValue a default value.
     * @return the property value or defaultValue
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getString(key, null);
        if (value == null) {
            return defaultValue;
        }
        return value.equals("1") || value.equals("true");
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public void setBoolean(String key, boolean value) {
        setString(key, value ? "true" : "false");
    }

    /**
     * Get a property as a string. If the property does not exist the default value will be returned.
     *
     * @param key the hashtable key.
     * @param defaultValue a default value.
     * @return the property value or defaultValue
     */
    public String getString(String key, String defaultValue) {
        SafeProperty property = properties.get(key);
        if (property == null) {
            return defaultValue;
        }
        return property.getValue();
    }

    public String getString(String key) {
        SafeProperty property = properties.get(key);
        if (property == null) {
            if (defaults != null) {
                return defaults.getString(key);
            }
            return null;
        }
        return property.getValue();
    }

    public void setString(String key, String value) {
        SafeProperty property = properties.get(key);
        if (property != null) {
            property.setValue(value);
        } else {
            property = new SafeProperty(key, value);
            properties.put(key, property);
        }
    }

    public void clear() {
        properties.clear();
    }
}
