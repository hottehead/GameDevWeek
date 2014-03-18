package de.hochschuletrier.gdw.commons.tiled;

import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import java.util.HashMap;

import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxTile;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxTileSet;

/**
 * A tile set
 *
 * @author Santo Pfingsten
 */
public class TileSet {

    /** The index of the tile set */
    private final int index;
    /** The name of the tile set */
    private final String name;
    /** The first global tile id in the set */
    private final int firstGID;
    /** The local global tile id in the set */
    private int lastGID;
    /** The width of the tiles */
    private final int tileWidth;
    /** The height of the tiles */
    private final int tileHeight;
    /** The image containing the tiles */
    private final TmxImage image;
    /** The number of tiles across the sprite sheet */
    private final int tilesAcross;
    /** The number of tiles down the sprite sheet */
    private final int tilesDown;
    /** The tileset properties */
    private final SafeProperties properties;
    /** The properties for each tile */
    private final HashMap<Integer, SafeProperties> tileProperties = new HashMap<Integer, SafeProperties>();
    /** The padding of the tiles */
    private final int tileSpacing;
    /** The margin of the tiles */
    private final int tileMargin;
    /** The filename this tileset was loaded from */
    private final String filename;
    /** An attachment for the game to store additional information */
    private Object attachment;
    /** Animation data */
    private TileSetAnimation animation;

    /**
     * Create a tile set based on an XML definition
     *
     * @param element The XML describing the tileset
     * @throws Exception Indicates a failure to parse the tileset
     */
    TileSet(TiledMap map, TmxTileSet element, int index) throws Exception {
        this.index = index;
        firstGID = element.getFirstgid();
        name = element.getName();
        properties = element.getProperties();

        String source = element.getSource();
        if ((source != null) && (!source.equals(""))) {
            // Load from external file.
            filename = CurrentResourceLocator.combinePaths(map.getFilename(), source);
            element = map.readTileSetFrom(filename);
        } else {
            filename = map.getFilename();
        }

        tileWidth = element.getTilewidth();
        tileHeight = element.getTileheight();

        Integer spacing = element.getSpacing();
        tileSpacing = spacing != null ? spacing.intValue() : 0;

        Integer margin = element.getMargin();
        tileMargin = margin != null ? margin.intValue() : 0;

        for (TmxTile tile : element.getTiles()) {
            tileProperties.put(firstGID + tile.getId(), tile.getProperties());
        }

        image = element.getImage();
        tilesAcross = Math.max(1, ((image.getWidth() - (tileMargin * 2) - tileWidth) / (tileWidth + tileSpacing)) + 1);
        tilesDown = Math.max(1, ((image.getHeight() - (tileMargin * 2) - tileHeight) / (tileHeight + tileSpacing)) + 1);

        lastGID = (tilesAcross * tilesDown) + firstGID - 1;
        initAnimations();
    }

    private void initAnimations() {
        int frames = getIntProperty("animationFrames", 0);
        if (frames > 1) {
            animation = new TileSetAnimation(
                    frames,
                    getFloatProperty("animationDuration", 0),
                    getIntProperty("animationTileOffset", 0)
            );
        }
    }
    
    public TileSetAnimation getAnimation() {
        return animation;
    }

    /**
     * Get the width of each tile in this set
     *
     * @return The width of each tile in this set
     */
    public int getTileWidth() {
        return tileWidth;
    }

    /**
     * Get the height of each tile in this set
     *
     * @return The height of each tile in this set
     */
    public int getTileHeight() {
        return tileHeight;
    }

    /**
     * Get the spacing between tiles in this set
     *
     * @return The spacing between tiles in this set
     */
    public int getTileSpacing() {
        return tileSpacing;
    }

    /**
     * Get the margin around tiles in this set
     *
     * @return The maring around tiles in this set
     */
    public int getTileMargin() {
        return tileMargin;
    }

    /**
     * @return the properties for this map
     */
    SafeProperties getProperties() {
        return properties;
    }

    /**
     * Get a map property
     *
     * @param key the key of the property
     * @param def the default value to return if the property has not been set.
     * @return the property value or def
     */
    public String getProperty(String key, String def) {
        if (properties == null) {
            return def;
        }
        return properties.getString(key, def);
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

    /**
     * @param globalID the global tile id
     * @return the properties for the specified tile
     */
    SafeProperties getTileProperties(int globalID) {
        return tileProperties.get(globalID);
    }

    String getTileProperty(int globalID, String propertyName, String def) {
        SafeProperties props = tileProperties.get(globalID);
        if (props == null) {
            return def;
        }
        return props.getString(propertyName, def);
    }

    public int getTileIntProperty(int globalID, String propertyName, int def) {
        SafeProperties props = tileProperties.get(globalID);
        if (props == null) {
            return def;
        }
        return props.getInt(propertyName, def);
    }

    public float getTileFloatProperty(int globalID, String propertyName, float def) {
        SafeProperties props = tileProperties.get(globalID);
        if (props == null) {
            return def;
        }
        return props.getFloat(propertyName, def);
    }

    public double getTileDoubleProperty(int globalID, String propertyName, double def) {
        SafeProperties props = tileProperties.get(globalID);
        if (props == null) {
            return def;
        }
        return props.getDouble(propertyName, def);
    }

    public boolean getTileBooleanProperty(int globalID, String propertyName, boolean def) {
        SafeProperties props = tileProperties.get(globalID);
        if (props == null) {
            return def;
        }
        return props.getBoolean(propertyName, def);
    }

    /**
     * Get the x position of a tile on this sheet
     *
     * @param id The tileset specific ID (i.e. not the global one)
     * @return The index of the tile on the x-axis
     */
    public int getTileX(int id) {
        return id % tilesAcross;
    }

    /**
     * Get the y position of a tile on this sheet
     *
     * @param id The tileset specific ID (i.e. not the global one)
     * @return The index of the tile on the y-axis
     */
    public int getTileY(int id) {
        return id / tilesAcross;
    }

    /**
     * Set the limit of the tiles in this set
     *
     * @param limit The limit of the tiles in this set
     */
    void setLimit(int limit) {
        lastGID = limit;
    }

    /**
     * Check if this tileset contains a particular tile
     *
     * @param gid The global id to seach for
     * @return True if the ID is contained in this tileset
     */
    public boolean contains(int gid) {
        return (gid >= firstGID) && (gid <= lastGID);
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the firstGID
     */
    public int getFirstGID() {
        return firstGID;
    }

    /**
     * @return the lastGID
     */
    public int getLastGID() {
        return lastGID;
    }

    /**
     * @return the image
     */
    public TmxImage getImage() {
        return image;
    }

    /**
     * @return the tilesAcross
     */
    public int getTilesAcross() {
        return tilesAcross;
    }

    /**
     * @return the tilesDown
     */
    public int getTilesDown() {
        return tilesDown;
    }

    /**
     * @return the filename this tileset was loaded from
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @return the attachment
     */
    public Object getAttachment() {
        return attachment;
    }

    /**
     * @param attachment the attachment to set
     */
    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }
}
