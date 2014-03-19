package de.hochschuletrier.gdw.commons.tiled.tmx;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author Santo Pfingsten
 */
@XStreamAlias("layer")
public class TmxLayer extends TmxLayerBase {

    @XStreamAlias("data")
    protected TmxData data;

    public TmxData getData() {
        return data;
    }

    public void setData(TmxData value) {
        this.data = value;
    }
}
