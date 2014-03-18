package de.hochschuletrier.gdw.commons.tiled.tmx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.hochschuletrier.gdw.commons.tiled.SafeProperties;

/**
 *
 * @author Santo Pfingsten
 */
public class TmxLayerBase {

    @XStreamAlias("properties")
    protected SafeProperties properties;
    @XStreamAlias("name")
    @XStreamAsAttribute
    protected String name;
    @XStreamAlias("opacity")
    @XStreamAsAttribute
    protected Float opacity;
    @XStreamAlias("width")
    @XStreamAsAttribute
    protected int width;
    @XStreamAlias("height")
    @XStreamAsAttribute
    protected int height;
    @XStreamAlias("visible")
    @XStreamAsAttribute
    protected Integer visible;

    public SafeProperties getProperties() {
        return properties;
    }

    public void setProperties(SafeProperties value) {
        this.properties = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public Float getOpacity() {
        return opacity;
    }

    public void setOpacity(Float value) {
        this.opacity = value;
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

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer value) {
        this.visible = value;
    }
}
