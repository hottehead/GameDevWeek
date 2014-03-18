package de.hochschuletrier.gdw.commons.tiled.tmx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author Santo Pfingsten
 */
public class TmxImage {

    @XStreamAlias("source")
    @XStreamAsAttribute
    protected String source;
    @XStreamAlias("trans")
    @XStreamAsAttribute
    protected String trans;
    @XStreamAlias("width")
    @XStreamAsAttribute
    protected Integer width;
    @XStreamAlias("height")
    @XStreamAsAttribute
    protected Integer height;

    public String getSource() {
        return source;
    }

    public void setSource(String value) {
        this.source = value;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String value) {
        this.trans = value;
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
}
