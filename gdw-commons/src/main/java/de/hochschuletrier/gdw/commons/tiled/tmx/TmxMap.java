package de.hochschuletrier.gdw.commons.tiled.tmx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import de.hochschuletrier.gdw.commons.tiled.SafeProperties;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Santo Pfingsten
 */
@XStreamAlias("map")
public class TmxMap {

    @XStreamAlias("properties")
    protected SafeProperties properties;
    @XStreamImplicit(itemFieldName = "tileset")
    protected List<TmxTileSet> tilesets;
    protected List<TmxLayerBase> layerOrObjectgroup;
    @XStreamAlias("name")
    @XStreamAsAttribute
    protected String version;
    @XStreamAlias("orientation")
    @XStreamAsAttribute
    protected String orientation;
    @XStreamAlias("width")
    @XStreamAsAttribute
    protected int width;
    @XStreamAlias("height")
    @XStreamAsAttribute
    protected int height;
    @XStreamAlias("tilewidth")
    @XStreamAsAttribute
    protected int tilewidth;
    @XStreamAlias("tileheight")
    @XStreamAsAttribute
    protected int tileheight;

    public SafeProperties getProperties() {
        return properties;
    }

    public void setProperties(SafeProperties value) {
        this.properties = value;
    }

    public List<TmxTileSet> getTilesets() {
        if (tilesets == null) {
            tilesets = new ArrayList<TmxTileSet>();
        }
        return this.tilesets;
    }

    public List<TmxLayerBase> getLayerOrObjectgroup() {
        if (layerOrObjectgroup == null) {
            layerOrObjectgroup = new ArrayList<TmxLayerBase>();
        }
        return this.layerOrObjectgroup;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String value) {
        this.version = value;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String value) {
        this.orientation = value;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int value) {
        this.width = value;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int value) {
        this.height = value;
    }

    public int getTilewidth() {
        return tilewidth;
    }

    public void setTilewidth(int value) {
        this.tilewidth = value;
    }

    public int getTileheight() {
        return tileheight;
    }

    public void setTileheight(int value) {
        this.tileheight = value;
    }
}
