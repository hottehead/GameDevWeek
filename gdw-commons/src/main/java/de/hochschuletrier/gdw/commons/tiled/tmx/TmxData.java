package de.hochschuletrier.gdw.commons.tiled.tmx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Santo Pfingsten
 */
public class TmxData {

    @XStreamAlias("encoding")
    @XStreamAsAttribute
    protected String encoding;
    @XStreamAlias("compression")
    @XStreamAsAttribute
    protected String compression;
    protected List<Integer> ids;

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String value) {
        this.encoding = value;
    }

    public String getCompression() {
        return compression;
    }

    public void setCompression(String value) {
        this.compression = value;
    }

    public List<Integer> getIds() {
        if (ids == null) {
            ids = new ArrayList<Integer>();
        }
        return this.ids;
    }
}
