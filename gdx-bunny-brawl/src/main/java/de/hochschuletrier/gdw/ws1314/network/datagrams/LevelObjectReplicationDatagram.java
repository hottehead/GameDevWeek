package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ServerLevelObject;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

public class LevelObjectReplicationDatagram extends BaseDatagram{
	public static final byte LEVEL_OBJECT_REPLICATION_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x21;
	private long entityId;
	private EntityType type;
	private float xposition;
	private float yposition;
	private boolean visibility;

	public LevelObjectReplicationDatagram(byte type, short id, short param1, short param2){
		super(MessageType.DELTA, type, id, param1, param2);
	}

	public LevelObjectReplicationDatagram(long entityId, EntityType type, float xposition, float yposition, boolean visibility){
		super(MessageType.DELTA, LEVEL_OBJECT_REPLICATION_DATAGRAM, (short) entityId, (short) 0, (short) 0);
		this.entityId = entityId;
		this.type = type;
		this.xposition = xposition;
		this.yposition = yposition;
		this.visibility = visibility;
	}

	public LevelObjectReplicationDatagram(ServerLevelObject entity){
		this(entity.getID(), entity.getEntityType(), entity.getPosition().x, entity.getPosition().y, entity.getVisibility());
	}

	@Override
	public void handle(DatagramHandler handler, NetConnection connection){
		handler.handle(this, connection);
	}

	@Override
	public void writeToMessage(INetMessageOut message){
		message.putLong(entityId);
		message.putEnum(type);
		message.putFloat(xposition);
		message.putFloat(yposition);
		message.putBool(visibility);
	}

	@Override
	public void readFromMessage(INetMessageIn message){
		entityId = message.getLong();
		type = message.getEnum(EntityType.class);
		xposition = message.getFloat();
		yposition = message.getFloat();
		visibility = message.getBool();
	}

	public long getEntityId(){
		return entityId;
	}

	public EntityType getEntityType(){
		return type;
	}

	public float getXposition(){
		return xposition;
	}

	public float getYposition(){
		return yposition;
	}

	public boolean getVisibility(){
		return visibility;
	}
}
