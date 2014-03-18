package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;

/**
 * Created by albsi on 17.03.14.
 */
public class PlayerUpdateDatagram extends BaseDatagram{

    public PlayerUpdateDatagram(byte type, short id, short param1, short param2) {
        super(MessageType.DELTA, type, id, param1, param2);
    }
    public static final byte PLAYER_UPDATE_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x12;
    
    private String playername;
    private EntityType type;
    private byte team;
    private boolean accept;
    
    @Override
    public void handle (DatagramHandler handler, NetConnection connection) {
        handler.handle (this, connection);
    }

    @Override
    public void writeToMessage (INetMessageOut message) {
    	message.putString(playername);
    	message.putEnum(type);
    	message.put(team);
    	message.putBool(accept);
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
    	playername = message.getString();
    	type = message.getEnum(EntityType.class);
    	team = message.get();
    	accept = message.getBool();
    }

	public String getPlayername() {
		return playername;
	}

	public EntityType getEntityType() {
		return type;
	}

	public byte getTeam() {
		return team;
	}

	public boolean isAccept() {
		return accept;
	}
  
}
