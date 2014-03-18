package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

/**
 * Created by albsi on 17.03.14.
 */
public class LobbyUpdateDatagram extends BaseDatagram{

    public LobbyUpdateDatagram(byte type, short id, short param1, short param2) {
        super(MessageType.DELTA, type, id, param1, param2);
    }
    public static final byte LOBBY_UPDATE_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x10;
    
    private String map;
 
    private String playername;
    private Enum<?> playerclass;
    private byte team;
    private boolean accept;

    @Override
    public void handle (DatagramHandler handler, NetConnection connection) {
        handler.handle (this, connection);
    }

    @Override
    public void writeToMessage (INetMessageOut message) {
    	message.putString(playername);
    	message.putEnum(playerclass);
    	message.put(team);
    	message.putBool(accept);
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
    	playername = message.getString();
    	//playerclass = message.getEnum(Enum<?> e);
    	team = message.get();
    	accept = message.getBool();
    }

	public String getMap() {
		return map;
	}

	public String getPlayername() {
		return playername;
	}

	public Enum<?> getPlayerclass() {
		return playerclass;
	}

	public byte getTeam() {
		return team;
	}

	public boolean isAccept() {
		return accept;
	}
    

}
