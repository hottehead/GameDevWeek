package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by albsi on 17.03.14.
 */
public class LobbyUpdateDatagram extends BaseDatagram {
	private static final Logger logger = LoggerFactory.getLogger(LobbyUpdateDatagram.class);
	
    public static final byte LOBBY_UPDATE_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x10;
    private String map;
    private int playercount;
    private PlayerData[] players;

    public LobbyUpdateDatagram (byte type, short id, short param1, short param2) {
        super (MessageType.NORMAL, type, id, param1, param2);
    }
    public LobbyUpdateDatagram(String map, PlayerData[] players){
    	super (MessageType.NORMAL, LOBBY_UPDATE_DATAGRAM, (short) 0, (short) 0, (short) 0);
    	this.map = map;
    	this.playercount = players.length;
    	this.players = players;
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
        	message.putInt(players[i].getId());
            message.putString (players[i].getPlayername());
            message.putEnum (players[i].getType());
            message.putEnum(players[i].getTeam());
            message.putBool (players[i].isAccept());
        }
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
        map = message.getString ();
        playercount = message.getInt ();
        players = new PlayerData[playercount];
        for (int i = 0; i < playercount; i++) {
        	int id = message.getInt();
        	String name = message.getString();
        	EntityType type = message.getEnum(EntityType.class);
        	TeamColor team = message.getEnum(TeamColor.class);
        	boolean accept = message.getBool();
        	players[i] = new PlayerData(id, name, type, team, accept);
        }
    }

    public String getMap () {
        return map;
    }

    public PlayerData[] getPlayers(){
    	return players;
    }
}
