package de.hochschuletrier.gdw.commons.netcode.message;

import java.util.HashMap;

/**
 * A message cache stores last known states for a message based on the datagram type and the id
 *
 * @author Santo Pfingsten
 */
public class NetMessageCache {

    /** The actual cache */
    private final HashMap<Integer, NetMessage> map = new HashMap<Integer, NetMessage>();

    /**
     * Get a cached message using the datagram type and id as key
     *
     * @param type the datagram type
     * @param id the datagram id
     * @return the last known state or null if none was set yet
     */
    public NetMessage get(byte type, short id) {
        // Combine the type and id into one integer key
        int key = ((type << 16) & 0xff0000) | (id & 0xffff);
        return map.get(key);
    }

    /**
     * Store a message in the cache using the datagram type and id as key
     *
     * @param type the datagram type
     * @param id the datagram id
     * @param msg the current state of the message
     */
    public void set(byte type, short id, NetMessage msg) {
        // Combine the type and id into one integer key
        int key = ((type << 16) & 0xff0000) | (id & 0xffff);

        // Free the last message
        NetMessage old = map.put(key, msg);
        if (old != null) {
            old.free();
        }
    }

    /**
     * Free all messages so they can be reused
     */
    public void clear() {
        for (NetMessage msg : map.values()) {
            msg.free();
        }
        map.clear();
    }
}
