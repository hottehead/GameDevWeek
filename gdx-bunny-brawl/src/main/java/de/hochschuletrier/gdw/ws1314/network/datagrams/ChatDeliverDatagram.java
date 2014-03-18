package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

public class ChatDeliverDatagram extends BaseDatagram {
	public static final byte CHAT_DELIVER_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x01;

	private String sender;
	private String text;
	
	public ChatDeliverDatagram(byte type, short id, short param1, short param2) {
		super(MessageType.NORMAL, type, id, param1, param2);
	}
	
	public ChatDeliverDatagram(String sender, String text){
		super(MessageType.NORMAL, CHAT_DELIVER_DATAGRAM, (short)0, (short)0, (short)0);
		this.sender=sender;
		this.text=text;
	}

	@Override
	public void writeToMessage(INetMessageOut message) {
		message.putString(sender);
		message.putString(text);
	}

	@Override
	public void readFromMessage(INetMessageIn message) {
		sender = message.getString();
		text = message.getString();
	}

	@Override
	public void handle(DatagramHandler handler, NetConnection connection) {
		handler.handle(this,connection);
	}

}
