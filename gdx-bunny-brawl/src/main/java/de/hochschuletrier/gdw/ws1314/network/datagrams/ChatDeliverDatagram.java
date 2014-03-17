package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

public class ChatDeliverDatagram extends BaseDatagram {

	public ChatDeliverDatagram(byte type, short id, short param1, short param2) {
		super(INetDatagram.MessageType.NORMAL, type, id, param1, param2);
	}
	public static final byte CHAT_DELIVER_MESSAGE = INetDatagram.Type.FIRST_CUSTOM + 0x01;

	@Override
	public void writeToMessage(INetMessageOut message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readFromMessage(INetMessageIn message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handle(DatagramHandler handler, NetConnection connection) {
		handler.handle(this,connection);
	}

}
