package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.ws1314.network.datagrams.ChatDeliverDatagram;
import de.hochschuletrier.gdw.ws1314.network.datagrams.ChatSendDatagram;

public class ServerGameDatagramHandler implements DatagramHandler {

	@Override
	public void handle(ChatSendDatagram chatDatagram, NetConnection connection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handle(ChatDeliverDatagram chatDeliverDatagram,
			NetConnection connection) {
		// TODO Auto-generated method stub
		
	}

}
