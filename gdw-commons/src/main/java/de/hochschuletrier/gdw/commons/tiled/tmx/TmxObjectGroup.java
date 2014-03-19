package de.hochschuletrier.gdw.commons.tiled.tmx;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Santo Pfingsten
 */
@XStreamAlias("objectgroup")
public class TmxObjectGroup extends TmxLayerBase {

    @XStreamImplicit(itemFieldName = "object")
    protected List<TmxObject> objects;

    public List<TmxObject> getObjects() {
        if (objects == null) {
            objects = new ArrayList<TmxObject>();
        }
        return this.objects;
    }
}
