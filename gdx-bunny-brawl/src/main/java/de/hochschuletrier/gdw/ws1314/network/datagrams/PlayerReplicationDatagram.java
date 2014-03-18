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
public class PlayerReplicationDatagram extends BaseDatagram{

    public PlayerReplicationDatagram(byte type, short id, short param1, short param2) {
        super(MessageType.DELTA, type, id, param1, param2);
    }
    public static final byte PLAYER_REPLICATION_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x20;

    private long id;
    private EntityType type;
	private float xposition;
    private float yposition;
    private byte eggs;
    private byte buffs;
    private int health;

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
    	message.put(eggs);
    	message.put(buffs);
    	message.putInt(health);
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
    	id = message.getInt();
    	type = message.getEnum(EntityType.class);
    	xposition = message.getFloat();
    	yposition = message.getFloat();
    	eggs = message.get();
    	buffs = message.get();
    	health = message.getInt();
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

	public byte getEggs() {
		return eggs;
	}

	public byte getBuffs() {
		return buffs;
	}

	public int getHealth() {
		return health;
	}
}
