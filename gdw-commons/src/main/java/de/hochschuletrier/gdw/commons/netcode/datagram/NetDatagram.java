package de.hochschuletrier.gdw.commons.netcode.datagram;

/**
 * The most basic datagram
 *
 * @author Santo Pfingsten
 */
public abstract class NetDatagram implements INetDatagram {

    protected byte type;
    protected short id;
    protected short param1;
    protected short param2;
    protected MessageType messageType;

    /**
     * Create a new datagram
     *
     * @param messageType the type of message this datagram contains
     * @param type the datagram type
     * @param id a unique id, used in combo with type for identifying delta message bases
     * @param param1 the first parameter of the header. If the message type is not none, this contains the message size.
     * @param param2 the second parameter of the header. If the message type is not none, this contains the delta message size.
     */
    public NetDatagram(MessageType messageType, byte type, short id, short param1, short param2) {
        this.type = type;
        this.id = id;
        this.param1 = param1;
        this.param2 = param2;
        this.messageType = messageType;
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public byte getType() {
        return type;
    }

    @Override
    public short getID() {
        return id;
    }

    @Override
    public short getParam1() {
        return param1;
    }

    @Override
    public short getParam2() {
        return param2;
    }
}
