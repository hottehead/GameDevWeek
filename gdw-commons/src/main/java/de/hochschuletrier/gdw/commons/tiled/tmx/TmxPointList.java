package de.hochschuletrier.gdw.commons.tiled.tmx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author Santo Pfingsten
 */
public class TmxPointList {

    @XStreamAlias("points")
    @XStreamAsAttribute
    protected String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String value) {
        this.points = value;
    }
}
