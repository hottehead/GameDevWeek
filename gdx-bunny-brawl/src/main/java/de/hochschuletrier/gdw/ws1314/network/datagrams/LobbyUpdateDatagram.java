package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

/**
 * Created by albsi on 17.03.14.
 */
public class LobbyUpdateDatagram extends BaseDatagram {
    public static final byte LOBBY_UPDATE_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x10;
    private String map;
    private int playercount;
    private String[] playername;
    private EntityType[] type;
    private byte[] team;
    private boolean[] accept;

    public LobbyUpdateDatagram (byte type, short id, short param1, short param2) {
        super (MessageType.DELTA, type, id, param1, param2);
    }

    @Override
    public void handle (DatagramHandler handler, NetConnection connection) {
        handler.handle (this, connection);
    }

    @Override
    public void writeToMessage (INetMessageOut message) {
        message.putString (map);
        message.putInt (playercount);
        for (int i = 0; i < playercount; i++) {
            message.putString (playername[i]);
            message.putEnum (type[i]);
            message.put (team[i]);
            message.putBool (accept[i]);
        }
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
        map = message.getString ();
        playercount = message.getInt ();
        playername = new String[playercount];
        type = new EntityType[playercount];
        team = new byte[playercount];
        accept = new boolean[playercount];
        for (int i = 0; i < playercount; i++) {
            playername[i] = message.getString ();
            type[i] = message.getEnum (EntityType.class);
            team[i] = message.get ();
            accept[i] = message.getBool ();
        }
    }

    public String getMap () {
        return map;
    }

    public String[] getPlayernames () {
        return playername;
    }

    public EntityType[] getEntityTypes () {
        return type;
    }

    public byte[] getTeams () {
        return team;
    }

    public boolean[] getAccepts () {
        return accept;
    }
}
