package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.NetDatagram;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

public abstract class BaseDatagram extends NetDatagram {
	public BaseDatagram(MessageType messageType, byte type, short id,
			short param1, short param2) {
		super(messageType, type, id, param1, param2);
		// TODO Auto-generated constructor stub
	}
	public abstract void handle(DatagramHandler handler, NetConnection connection);
}
