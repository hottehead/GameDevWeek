package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

public class EntityIDDatagram extends BaseDatagram{
	public static final byte ENTITY_ID_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x14;
	private long entityId;

	public EntityIDDatagram(byte type, short id, short param1, short param2){
		super(MessageType.NORMAL, type, id, param1, param2);
	}

	public EntityIDDatagram(long entityid){
		super(MessageType.NORMAL, ENTITY_ID_DATAGRAM, (short) 0, (short) 0, (short) 0);
		this.entityId = entityid;
	}

	@Override
	public void handle(DatagramHandler handler, NetConnection connection){
		handler.handle(this, connection);
	}

	@Override
	public void writeToMessage(INetMessageOut message){
		message.putLong(entityId);
	}

	@Override
	public void readFromMessage(INetMessageIn message){
		entityId = message.getLong();
	}

	public long getEntityId(){
		return entityId;
	}
}
