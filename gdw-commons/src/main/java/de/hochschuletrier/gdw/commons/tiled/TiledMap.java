package de.hochschuletrier.gdw.commons.tiled;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;

import de.hochschuletrier.gdw.commons.tiled.tmx.TmxDataConverter;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxLayer;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxLayerBase;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxObjectGroup;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxTileSet;

/**
 * A TilED map loader
 *
 * @author Santo Pfingsten
 */
public class TiledMap {

    /** The map width (in tiles) */
    protected final int width;
    /** The map height (in tiles) */
    protected final int height;
    /** The default tile width (TileSets may have varying values) */
    protected final int tileWidth;
    /** The default tile height (TileSets may have varying values) */
    protected final int tileHeight;
    /** The map properties */
    protected final SafeProperties properties;
    /** The list of TileSets */
    protected final ArrayList<TileSet> tileSets = new ArrayList<TileSet>();
    /** The list of Layers */
    protected final ArrayList<Layer> layers = new ArrayList<Layer>();
    /** The filename of this map */
    private final String filename;
    /** The map renderer */
    private ITiledMapRenderer renderer;

    /**
     * Load a map using a the default resource locator (file system) and the default PolyMode (RELATIVE_TO_FIRST)
     * 
     * @param filename the filename of the file to load
     * @throws Exception when the map could not be loaded
     */
    public TiledMap(String filename) throws Exception {
        this(filename, LayerObject.PolyMode.RELATIVE_TO_FIRST);
    }

    /**
     * Load a map
     * 
     * @param filename the filename of the file to load
     * @param polyMode The mode how polygon points should be converted
     * @throws Exception when the map could not be loaded
     */
    public TiledMap(String filename, LayerObject.PolyMode polyMode) throws Exception {
        this.filename = filename;

        try {
            TmxMap map = readMapFrom(filename);
            String orient = map.getOrientation();
            if (!orient.equals("orthogonal")) {
                throw new Exception("Only orthogonal maps supported, found: " + orient);
            }

            width = map.getWidth();
            height = map.getHeight();
            tileWidth = map.getTilewidth();
            tileHeight = map.getTileheight();
            // now read the map properties
            properties = map.getProperties();

            TileSet lastSet = null;

            int i = 0;
            for (TmxTileSet current : map.getTilesets()) {
                TileSet tileSet = new TileSet(this, current, i++);

                if (lastSet != null) {
                    lastSet.setLimit(tileSet.getFirstGID() - 1);
                }
                lastSet = tileSet;

                tileSets.add(tileSet);
            }

            i = 0;
            for (TmxLayerBase current : map.getLayerOrObjectgroup()) {
                Layer layer = new Layer(this, current, i++, polyMode);
                layers.add(layer);
            }
        } catch (Exception e) {
            throw new Exception("Failed to parse tilemap", e);
        }
    }

    /**
     * @return the file name of this map
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @return the list of TileSets
     */
    public ArrayList<TileSet> getTileSets() {
        return tileSets;
    }

    /**
     * @return The map width (in tiles)
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return The map height (in tiles)
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return The default tile height (TileSets may have varying values)
     */
    public int getTileHeight() {
        return tileHeight;
    }

    /**
     * @return The default tile width (TileSets may have varying values)
     */
    public int getTileWidth() {
        return tileWidth;
    }

    /**
     * @return The list of Layers
     */
    public ArrayList<Layer> getLayers() {
        return layers;
    }

    /**
     * Find a layer by its name
     * 
     * @param name The name of the layer
     * @return The layer or null if not found
     */
    public Layer findLayer(String name) {
        for (Layer layer : layers) {
            if (name.equals(layer.getName())) {
                return layer;
            }
        }

        return null;
    }

    /**
     * @return the properties for this map
     */
    public SafeProperties getProperties() {
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
     * Find a TileSet by a global tile id
     * 
     * @param gid the global tile id
     * @return the TileSet matching the tile id or null if not found
     */
    public TileSet findTileSet(int gid) {
        for (TileSet set : tileSets) {
            if (set.contains(gid)) {
                return set;
            }
        }

        return null;
    }

    /**
     * Find an object by its name
     * 
     * @param name The name of the object
     * @return The layer or null if not found
     */
    public LayerObject findObject(String name) {
        for (Layer layer : layers) {
            if (layer.getType() == Layer.Type.OBJECT) {
                for (LayerObject object : layer.getObjects()) {
                    if (name.equals(object.getName())) {
                        return object;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Read an xml file
     * 
     * @param filename the file to read from
     * @return a TmxTileSet object or a TmxMap object
     * @throws JAXBException
     * @throws FileNotFoundException 
     */
    final TmxMap readMapFrom(String filename) throws FileNotFoundException {
        InputStream in = CurrentResourceLocator.read(filename);

        XStream xStream = new XStream(new DomDriver()) {
           @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                     if (definedIn == Object.class) {
                     try {
                     return this.realClass(fieldName) != null;
                     } catch(Exception e) {
                     return false;
                     }
                     } else {
                            return super.shouldSerializeMember(definedIn, fieldName);
                        }
                    }
                };
            }
        };

        xStream.processAnnotations(TmxLayer.class);
        xStream.processAnnotations(TmxObjectGroup.class);
        xStream.processAnnotations(TmxMap.class);
        xStream.registerConverter(new TmxDataConverter());
        xStream.addImplicitCollection(TmxMap.class, "layerOrObjectgroup", TmxObjectGroup.class);
        xStream.addImplicitCollection(TmxMap.class, "layerOrObjectgroup", TmxLayer.class);

        return (TmxMap) xStream.fromXML(in);
    }

    final TmxTileSet readTileSetFrom(String filename) throws FileNotFoundException {
        InputStream in = CurrentResourceLocator.read(filename);

        XStream xStream = new XStream(new DomDriver());
        xStream.processAnnotations(TmxTileSet.class);
        return (TmxTileSet) xStream.fromXML(in);
    }

    /**
     * @return the map renderer (can be null if not set)
     */
    public ITiledMapRenderer getRenderer() {
        return renderer;
    }

    /**
     * Set the new map renderer
     * 
     * @param renderer the new map renderer
     */
    public void setRenderer(ITiledMapRenderer renderer) {
        this.renderer = renderer;
    }
}
