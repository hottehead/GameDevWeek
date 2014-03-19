package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.ws1314.input.PlayerIntention;

/**
 * Created by albsi on 18.03.14.
 */
public interface ActionCallback {
    public void callback(PlayerIntention playerAction);
}
