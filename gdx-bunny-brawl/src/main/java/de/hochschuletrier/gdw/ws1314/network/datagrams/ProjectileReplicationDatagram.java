package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

/**
 * Created by albsi on 17.03.14.
 */
public class ProjectileReplicationDatagram extends BaseDatagram {

    public ProjectileReplicationDatagram(byte type, short id, short param1, short param2) {
        super(MessageType.DELTA, type, id, param1, param2);
    }
    public static final byte PROJETILE_REPLICATION_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x22;

    private long id;
    private float xposition;
    private float yposition;
    private FacingDirection direction;


    
    @Override
    public void handle (DatagramHandler handler, NetConnection connection) {
        handler.handle (this, connection);
    }

    @Override
    public void writeToMessage (INetMessageOut message) {
    	message.putLong(id);
    	message.putFloat(xposition);
    	message.putFloat(yposition);
    	message.putEnum(direction);
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
    	id = message.getLong();
    	xposition = message.getFloat();
    	yposition = message.getFloat();
    	direction = message.getEnum(FacingDirection.class);
    }

	public long getId() {
		return id;
	}

	public float getXposition() {
		return xposition;
	}

	public float getYposition() {
		return yposition;
	}

	public FacingDirection getDirection() {
		return direction;
	}
}
