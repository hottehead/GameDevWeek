package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

public class PingDatagram extends BaseDatagram{
	public static final byte PING_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x50;
	private long timestamp;

	public PingDatagram(byte type, short id, short param1, short param2){
		super(MessageType.NORMAL, type, id, param1, param2);
	}

	public PingDatagram(long timestamp){
		super(MessageType.NORMAL, PING_DATAGRAM, (short) 0, (short) 0, (short) 0);
		this.timestamp = timestamp;
	}

	public long getTimestamp(){
		return timestamp;
	}

	@Override
	public void writeToMessage(INetMessageOut message){
		message.putLong(timestamp);
	}

	@Override
	public void readFromMessage(INetMessageIn message){
		timestamp = message.getLong();
	}

	@Override
	public void handle(DatagramHandler handler, NetConnection connection){
		handler.handle(this, connection);
	}
}
