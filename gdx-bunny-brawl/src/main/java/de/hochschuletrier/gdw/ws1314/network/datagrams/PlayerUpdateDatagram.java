package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.datagram.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

/**
 * Created by albsi on 17.03.14.
 */
public class PlayerUpdateDatagram extends NetDatagram {
    public static final byte PLAYER_UPDATE_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x12;
    private String playerName;
    private EntityType type;
    private byte team;
    private boolean accept;

    public PlayerUpdateDatagram (byte type, short id, short param1, short param2) {
        super (MessageType.DELTA, type, id, param1, param2);
    }

    public PlayerUpdateDatagram (String playerName, EntityType type, byte team, boolean accept) {
        super (MessageType.DELTA, PLAYER_UPDATE_DATAGRAM, (short) 0, (short) 0, (short) 0);
        this.playerName = playerName;
        this.type = type;
        this.team = team;
        this.accept = accept;
    }

    @Override
    public void writeToMessage (INetMessageOut message) {
        message.putString (playerName);
        message.putEnum (type);
        message.put (team);
        message.putBool (accept);
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
        playerName = message.getString ();
        type = message.getEnum (EntityType.class);
        team = message.get ();
        accept = message.getBool ();
    }

    public String getPlayerName () {
        return playerName;
    }

    public EntityType getEntityType () {
        return type;
    }

    public byte getTeam () {
        return team;
    }

    public boolean isAccept () {
        return accept;
    }

}
