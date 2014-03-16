package de.hochschuletrier.gdw.commons.netcode.datagram;

/**
 * A datagram factory creates a new datagram object based on a type
 *
 * @author Santo Pfingsten
 */
public interface INetDatagramFactory {

    /**
     * Create a datagram based on a type
     *
     * @param type the datagram type
     * @param id a unique id, used in combo with type for identifying delta message bases
     * @param param1 the first parameter of the header. If the message type is not none, this contains the message size.
     * @param param2 the second parameter of the header. If the message type is not none, this contains the delta message size.
     * @return a new datagram object ready to be processed for reading
     */
    INetDatagram createDatagram(byte type, short id, short param1, short param2);
}
