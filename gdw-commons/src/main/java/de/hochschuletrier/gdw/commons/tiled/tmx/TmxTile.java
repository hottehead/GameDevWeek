package de.hochschuletrier.gdw.commons.tiled.tmx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.hochschuletrier.gdw.commons.tiled.SafeProperties;

/**
 *
 * @author Santo Pfingsten
 */
public class TmxTile {

    @XStreamAlias("properties")
    protected SafeProperties properties;
    @XStreamAlias("id")
    @XStreamAsAttribute
    protected Integer id;
    @XStreamAlias("gid")
    @XStreamAsAttribute
    protected Integer gid;

    public SafeProperties getProperties() {
        return properties;
    }

    public void setProperties(SafeProperties value) {
        this.properties = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer value) {
        this.id = value;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer value) {
        this.gid = value;
    }
}
