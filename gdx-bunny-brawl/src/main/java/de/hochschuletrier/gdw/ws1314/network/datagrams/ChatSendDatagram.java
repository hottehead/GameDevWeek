package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

public class ChatSendDatagram extends BaseDatagram {
    public static final byte CHAT_SEND_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x00;
    private String text;

    public ChatSendDatagram (byte type, short id,
                             short param1, short param2) {
        super (MessageType.NORMAL, type, id, param1, param2);
    }

    public ChatSendDatagram (String text) {
        super (MessageType.NORMAL, CHAT_SEND_DATAGRAM, (short) 0, (short) 0, (short) 0);
        this.text = text;
    }

    @Override
    public void handle (DatagramHandler handler, NetConnection connection) {
        handler.handle (this, connection);
    }

    @Override
    public void writeToMessage (INetMessageOut message) {
        message.putString (text);
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
        text = message.getString ();
    }

    public String getText () {
        return text;
    }
}
