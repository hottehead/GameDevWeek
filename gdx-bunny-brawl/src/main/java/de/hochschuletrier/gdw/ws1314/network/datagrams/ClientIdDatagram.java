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
    private PlayerData playerData;

    public ClientIdDatagram (byte type, short id,
                             short param1, short param2) {
        super (MessageType.DELTA, type, id, param1, param2);
    }

    public ClientIdDatagram (PlayerData playerData) {
        super (MessageType.DELTA, CLIENT_ID_DATAGRAM, (short) 0, (short) 0, (short) 0);
        this.playerData = playerData;
    }

    @Override
    public void handle (DatagramHandler handler, NetConnection connection) {
        handler.handle (this, connection);
    }

    @Override
    public void writeToMessage (INetMessageOut message) {
        message.putInt (playerData.getId());
        message.putString(playerData.getPlayername());
        message.putEnum(playerData.getType());
        message.putEnum(playerData.getTeam());
        message.putBool(playerData.isAccept());
        message.putLong(playerData.getEntityId());
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
    	int id = message.getInt();
    	String name = message.getString();
    	EntityType type = message.getEnum(EntityType.class);
    	TeamColor team = message.getEnum(TeamColor.class);
    	boolean accept = message.getBool();
    	long entityid = message.getLong();
    	this.playerData = new PlayerData(id, name, type, team, accept, entityid);
    }

    public PlayerData getPlayerData () {
        return playerData;
    }
}
