package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

public class GameStateDatagram extends BaseDatagram{
	public static final byte GAME_STATE_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x32;
	private GameStates gameStates;

	public GameStateDatagram(byte type, short id, short param1, short param2){
		super(MessageType.NORMAL, type, id, param1, param2);
	}

	public GameStateDatagram(GameStates gameStates){
		super(MessageType.NORMAL, GAME_STATE_DATAGRAM, (short) 0, (short) 0, (short) 0);
		this.gameStates = gameStates;
	}

	@Override
	public void handle(DatagramHandler handler, NetConnection connection){
		handler.handle(this, connection);
	}

	@Override
	public void writeToMessage(INetMessageOut message){
		message.putEnum(gameStates);
	}

	@Override
	public void readFromMessage(INetMessageIn message){
		this.gameStates = message.getEnum(GameStates.class);
	}

	public GameStates getGameStates(){
		return gameStates;
	}
}
