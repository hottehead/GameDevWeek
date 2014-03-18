package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

/**
 * Created by albsi on 17.03.14.
 */
public class ActionDatagram extends BaseDatagram {
    public static final byte ACTION_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x40;
    private int playerAction;

    public ActionDatagram (byte type, short id, short param1, short param2) {
        super (MessageType.NORMAL, type, id, param1, param2);
    }

    public ActionDatagram (int playerAction) {
        super (MessageType.NORMAL, ACTION_DATAGRAM, (short) 0, (short) 0, (short) 0);
        this.playerAction = playerAction;
    }

    @Override
    public void handle (DatagramHandler handler, NetConnection connection) {
        handler.handle (this, connection);
    }

    @Override
    public void writeToMessage (INetMessageOut message) {
        message.putInt (playerAction);
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
        playerAction = message.getInt ();
    }

    public int getPlayerAction () {
        return playerAction;
    }
}
