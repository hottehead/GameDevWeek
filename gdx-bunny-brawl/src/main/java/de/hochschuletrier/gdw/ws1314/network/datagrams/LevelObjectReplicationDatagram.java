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
public class LevelObjectReplicationDatagram extends BaseDatagram {

    public LevelObjectReplicationDatagram(byte type, short id, short param1, short param2) {
        super(MessageType.DELTA, type, id, param1, param2);
    }
    public static final byte LEVEL_OBJECT_REPLICATION_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x21;
    
    private long id;
    private EntityType type;
    private float xposition;
    private float yposition;
    private boolean status;



	@Override
    public void handle (DatagramHandler handler, NetConnection connection) {
        handler.handle (this, connection);
    }

    @Override
    public void writeToMessage (INetMessageOut message) {
    	message.putLong(id);
    	message.putEnum(type);
    	message.putFloat(xposition);
    	message.putFloat(yposition);
    	message.putBool(status);
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
    	id = message.getLong();
    	type = message.getEnum(EntityType.class);
    	xposition = message.getLong();
    	yposition = message.getLong();
    	status = message.getBool();
    }
    

    public long getId() {
		return id;
	}

	public EntityType getEntityType() {
		return type;
	}

	public float getXposition() {
		return xposition;
	}

	public float getYposition() {
		return yposition;
	}

	public boolean isStatus() {
		return status;
	}
}
