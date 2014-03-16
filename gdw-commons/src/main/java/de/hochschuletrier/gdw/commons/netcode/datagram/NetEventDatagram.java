package de.hochschuletrier.gdw.commons.netcode.datagram;

import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;

/**
 * A datagram for simple events (just the header and 2 short parameters)
 *
 * @author Santo Pfingsten
 */
public final class NetEventDatagram extends NetDatagram {

    /**
     * Create a new event datagram
     *
     * @param type the datagram type
     * @param id a unique id, can be used for anything.
     * @param param1 the first parameter of the header.
     * @param param2 the second parameter of the header.
     */
    public NetEventDatagram(byte type, short id, short param1, short param2) {
        super(MessageType.NONE, type, id, param1, param2);
    }

    /**
     * Create a new event datagram
     *
     * @param type the datagram type
     * @param id a unique id, can be used for anything.
     * @param param1 the first parameter of the header.
     */
    public NetEventDatagram(byte type, short id, short param1) {
        super(MessageType.NONE, type, id, param1, (short) 0);
    }

    /**
     * Create a new event datagram
     *
     * @param type the datagram type
     * @param id a unique id, can be used for anything.
     */
    public NetEventDatagram(byte type, short id) {
        super(MessageType.NONE, type, id, (short) 0, (short) 0);
    }

    /**
     * Create a new event datagram
     *
     * @param type the datagram type
     */
    public NetEventDatagram(byte type) {
        super(MessageType.NONE, type, (short) 0, (short) 0, (short) 0);
    }

    @Override
    public void writeToMessage(INetMessageOut message) {
    }

    public @Override
    void readFromMessage(INetMessageIn message) {
    }
}
