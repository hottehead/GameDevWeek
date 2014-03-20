package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

public class ClientIdDatagram extends BaseDatagram {
    public static final byte CLIENT_ID_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x13;
    private int playerid;

    public ClientIdDatagram (byte type, short id,
                             short param1, short param2) {
        super (MessageType.DELTA, type, id, param1, param2);
    }

    public ClientIdDatagram (int playerid) {
        super (MessageType.DELTA, CLIENT_ID_DATAGRAM, (short) 0, (short) 0, (short) 0);
        this.playerid = playerid;
    }

    @Override
    public void handle (DatagramHandler handler, NetConnection connection) {
        handler.handle (this, connection);
    }

    @Override
    public void writeToMessage (INetMessageOut message) {
        message.putInt (playerid);
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
    	this.playerid = message.getInt();
    }

    public int getPlayerId () {
        return playerid;
    }
}
