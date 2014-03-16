package de.hochschuletrier.gdw.examples.netcode.pingpong;

import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.datagram.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;

/**
 *
 * @author Santo Pfingsten
 */
public class ChatDatagram extends NetDatagram {
	public static final byte CHAT_MESSAGE = INetDatagram.Type.FIRST_CUSTOM + 0;
	private String text;
	
	public ChatDatagram(byte type, short id, short param1, short param2) {
		super(INetDatagram.MessageType.NORMAL, type, id, param1, param2);
	}
	
	public ChatDatagram(String message) {
		super(INetDatagram.MessageType.NORMAL, CHAT_MESSAGE, (short)0, (short)0, (short)0);
		
		this.text = message;
	}

	@Override
	public void writeToMessage(INetMessageOut message) {
		message.putString(text);
	}
	
	@Override
	public void readFromMessage(INetMessageIn message) {
		text = message.getString();
	}
	
	public String getText() {
		return text;
	}
}
