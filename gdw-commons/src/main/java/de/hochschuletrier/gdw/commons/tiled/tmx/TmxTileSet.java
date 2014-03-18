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
@XStreamAlias("tileset")
public class TmxTileSet {

    @XStreamAlias("properties")
    protected SafeProperties properties;
    @XStreamAlias("image")
    protected TmxImage image;
    @XStreamImplicit(itemFieldName = "tile")
    protected List<TmxTile> tiles;
    @XStreamAlias("firstgid")
    @XStreamAsAttribute
    protected int firstgid;
    @XStreamAlias("source")
    @XStreamAsAttribute
    protected String source;
    @XStreamAlias("name")
    @XStreamAsAttribute
    protected String name;
    @XStreamAlias("width")
    @XStreamAsAttribute
    protected Integer width;
    @XStreamAlias("height")
    @XStreamAsAttribute
    protected Integer height;
    @XStreamAlias("tilewidth")
    @XStreamAsAttribute
    protected Integer tilewidth;
    @XStreamAlias("tileheight")
    @XStreamAsAttribute
    protected Integer tileheight;
    @XStreamAlias("spacing")
    @XStreamAsAttribute
    protected Integer spacing;
    @XStreamAlias("margin")
    @XStreamAsAttribute
    protected Integer margin;

    public SafeProperties getProperties() {
        return properties;
    }

    public void setProperties(SafeProperties value) {
        this.properties = value;
    }

    public TmxImage getImage() {
        return image;
    }

    public void setImage(TmxImage value) {
        this.image = value;
    }

    public List<TmxTile> getTiles() {
        if (tiles == null) {
            tiles = new ArrayList<TmxTile>();
        }
        return this.tiles;
    }

    public int getFirstgid() {
        return firstgid;
    }

    public void setFirstgid(int value) {
        this.firstgid = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String value) {
        this.source = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer value) {
        this.width = value;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer value) {
        this.height = value;
    }

    public Integer getTilewidth() {
        return tilewidth;
    }

    public void setTilewidth(Integer value) {
        this.tilewidth = value;
    }

    public Integer getTileheight() {
        return tileheight;
    }

    public void setTileheight(Integer value) {
        this.tileheight = value;
    }

    public Integer getSpacing() {
        return spacing;
    }

    public void setSpacing(Integer value) {
        this.spacing = value;
    }

    public Integer getMargin() {
        return margin;
    }

    public void setMargin(Integer value) {
        this.margin = value;
    }
}
