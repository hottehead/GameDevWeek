package de.hochschuletrier.gdw.commons.tiled;

/**
 * Tile information
 *
 * @author Santo Pfingsten
 */
public class TileInfo {

    public final int tileSetId;
    public final int localId;
    public final int globalId;
    private final SafeProperties properties;

    public TileInfo(int tileSetId, int localId, int globalId, SafeProperties properties) {
        this.tileSetId = tileSetId;
        this.localId = localId;
        this.globalId = globalId;
        this.properties = properties;
    }

    public SafeProperties getProperties() {
        return properties;
    }

    public String getProperty(String propertyName, String def) {
        if (properties == null) {
            return def;
        }
        return properties.getString(propertyName, def);
    }

    public int getIntProperty(String propertyName, int def) {
        if (properties == null) {
            return def;
        }
        return properties.getInt(propertyName, def);
    }

    public float getFloatProperty(String propertyName, float def) {
        if (properties == null) {
            return def;
        }
        return properties.getFloat(propertyName, def);
    }

    public double getDoubleProperty(String propertyName, double def) {
        if (properties == null) {
            return def;
        }
        return properties.getDouble(propertyName, def);
    }

    public boolean getBooleanProperty(String propertyName, boolean def) {
        if (properties == null) {
            return def;
        }
        return properties.getBoolean(propertyName, def);
    }
}
