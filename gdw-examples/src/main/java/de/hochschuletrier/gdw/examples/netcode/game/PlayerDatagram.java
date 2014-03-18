package de.hochschuletrier.gdw.examples.netcode.game;

import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.datagram.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import java.awt.Point;

/**
 *
 * @author Santo Pfingsten
 */
public class PlayerDatagram extends NetDatagram {
	public static final byte PLAYER_MESSAGE = INetDatagram.Type.FIRST_CUSTOM + 0;
	private final Point position = new Point();
	
	public PlayerDatagram(byte type, short id, short param1, short param2) {
		super(INetDatagram.MessageType.DELTA, type, id, param1, param2);
	}
	
	public PlayerDatagram(Point position) {
		super(INetDatagram.MessageType.DELTA, PLAYER_MESSAGE, (short)0, (short)0, (short)0);
		
		this.position.setLocation(position);
	}

	@Override
	public void writeToMessage(INetMessageOut message) {
		message.putInt(position.x);
		message.putInt(position.y);
	}
	
	@Override
	public void readFromMessage(INetMessageIn message) {
		position.x = message.getInt();
		position.y = message.getInt();
	}
	
	public Point getPosition() {
		return position;
	}
}
