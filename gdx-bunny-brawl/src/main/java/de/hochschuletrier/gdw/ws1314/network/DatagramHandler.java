package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.ws1314.network.datagrams.ChatDeliverDatagram;
import de.hochschuletrier.gdw.ws1314.network.datagrams.ChatSendDatagram;

public interface DatagramHandler {

	void handle(ChatSendDatagram chatDatagram, NetConnection connection);

	void handle(ChatDeliverDatagram chatDeliverDatagram, NetConnection connection);

}
