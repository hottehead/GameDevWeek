package de.hochschuletrier.gdw.commons.netcode.datagram;

import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;

/**
 * A datagram is used to transfer events or messages.
 *
 * @author Santo Pfingsten
 */
public interface INetDatagram {

    /**
     * A list of internal datagram types
     */
    public static class Type {

        /** Send to keep the connection alive */
        public static final byte KEEP_ALIVE = 0;
        /** Disconnect signal */
        public static final byte DISCONNECT = 1;
        /** The first custom message */
        public static final byte FIRST_CUSTOM = 5;
    }

    /** The available message types */
    public static enum MessageType {

        /** The datagram only has a header (and 2 short parameters), no message */
        NONE,
        /** The datagram has a normal message */
        NORMAL,
        /** The datagram has a delta compressed message */
        DELTA,
    }

    /**
     * @return The type of message this datagram contains
     */
    MessageType getMessageType();

    /**
     * @return The type used to identify the type of datagram
     */
    byte getType();

    /**
     * @return A unique id, used in combo with type for identifying delta message bases
     */
    short getID();

    /**
     * @return The first parameter of the header. If the message type is not none, this contains the message size.
     */
    short getParam1();

    /**
     * @return The second parameter of the header. If the message type is not none, this contains the delta message size.
     */
    short getParam2();

    /**
     * Write all data to the message
     *
     * @param message the message to be send
     */
    void writeToMessage(INetMessageOut message);

    /**
     * Read all data from the message
     *
     * @param message the message to read from
     */
    void readFromMessage(INetMessageIn message);
}
