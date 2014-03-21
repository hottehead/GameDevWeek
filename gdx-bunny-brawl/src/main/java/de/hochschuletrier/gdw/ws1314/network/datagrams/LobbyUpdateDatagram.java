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
public class LobbyUpdateDatagram extends NetDatagram {
    public static final byte LOBBY_UPDATE_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x10;
    
    public class PlayerData {
    	private String playername;
    	private EntityType type;
    	private byte team;
    	private boolean accept;
    	
    	public PlayerData(String playername, EntityType type, byte team, boolean accept){
    		this.playername = playername;
    		this.type = type;
    		this.team = team;
    		this.accept = accept;
    	}
    	
		public String getPlayername() {
			return playername;
		}
		public EntityType getType() {
			return type;
		}
		public byte getTeam() {
			return team;
		}
		public boolean isAccept() {
			return accept;
		}   	
    }
    private String map;
    private int playercount;
    private PlayerData[] players;

    public LobbyUpdateDatagram (byte type, short id, short param1, short param2) {
        super (MessageType.DELTA, type, id, param1, param2);
    }
    public LobbyUpdateDatagram(String map, PlayerData[] players){
    	super (MessageType.DELTA, LOBBY_UPDATE_DATAGRAM, (short) 0, (short) 0, (short) 0);
    	this.map = map;
    	this.playercount = players.length;
    	this.players = players;
    }

    @Override
    public void writeToMessage (INetMessageOut message) {
        message.putString (map);
        message.putInt (playercount);
        for (int i = 0; i < playercount; i++) {
            message.putString (players[i].getPlayername());
            message.putEnum (players[i].getType());
            message.put (players[i].getTeam());
            message.putBool (players[i].isAccept());
        }
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
        map = message.getString ();
        playercount = message.getInt ();
        players = new PlayerData[playercount];
        for (int i = 0; i < playercount; i++) {
        	players[i] = new PlayerData(message.getString(),message.getEnum(EntityType.class),message.get(),message.getBool());
        }
    }

    public String getMap () {
        return map;
    }

    public PlayerData[] getPlayers(){
    	return players;
    }
}
