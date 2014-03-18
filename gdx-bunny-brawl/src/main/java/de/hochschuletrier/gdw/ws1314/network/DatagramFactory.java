package de.hochschuletrier.gdw.ws1314.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagramFactory;
import de.hochschuletrier.gdw.ws1314.network.datagrams.ChatDeliverDatagram;
import de.hochschuletrier.gdw.ws1314.network.datagrams.ChatSendDatagram;

public class DatagramFactory implements INetDatagramFactory {
    private static final Logger logger = LoggerFactory.getLogger(DatagramFactory.class);

	@Override
	public INetDatagram createDatagram(byte type, short id, short param1, short param2) {
		switch (type) {
		case ChatSendDatagram.CHAT_SEND_DATAGRAM:
			return new ChatSendDatagram(type, id, param1, param2);
		case ChatDeliverDatagram.CHAT_DELIVER_DATAGRAM:
			return new ChatDeliverDatagram(type, id, param1, param2);
		//TODO: Handle other types.
		default:
			logger.warn("Received datagram with unknown type {}", type);
			throw new IllegalArgumentException("Received datagram with unknown type: " + type);
		}
	}

}
