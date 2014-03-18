package de.hochschuletrier.gdw.examples.netcode.game;

import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagramFactory;

/**
 *
 * @author Santo Pfingsten
 */
public class DatagramFactory implements INetDatagramFactory {
	@Override
	public INetDatagram createDatagram(byte type, short id, short param1, short param2) {
		switch(type) {
			case PlayerDatagram.PLAYER_MESSAGE:
				return new PlayerDatagram(type, id, param1, param2);
			default:
				throw new IllegalArgumentException("type: " + type);
		}
	}
}

