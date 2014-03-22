package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

public class GameInfoReplicationDatagram extends BaseDatagram{
	public static final byte GAME_INFO_REPLICATION_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x23;

	private int eggsRemaining;
	private int eggsBlack;
	private int eggsWhite;

	public GameInfoReplicationDatagram(byte type, short id, short param1, short param2){
		super(MessageType.NORMAL, type, id, param1, param2);
	}

	public GameInfoReplicationDatagram(int eggsBlack, int eggsWhite, int eggsRemaining){
		super(MessageType.NORMAL, GAME_INFO_REPLICATION_DATAGRAM, (short) 0, (short) 0, (short) 0);
		this.eggsWhite = eggsWhite;
		this.eggsBlack = eggsBlack;
		this.eggsRemaining = eggsRemaining;
	}

	@Override
	public void writeToMessage(INetMessageOut message){
		message.putInt(eggsRemaining);
		message.putInt(eggsBlack);
		message.putInt(eggsWhite);
	}

	@Override
	public void readFromMessage(INetMessageIn message){
		eggsRemaining = message.getInt();
		eggsBlack = message.getInt();
		eggsWhite = message.getInt();
	}

	@Override
	public void handle(DatagramHandler handler, NetConnection connection){
		handler.handle(this, connection);
	}

	public int getEggsWhite(){
		return eggsWhite;
	}

	public int getEggsBlack(){
		return eggsBlack;
	}

	public int getEggsRemaining(){
		return eggsRemaining;
	}
}
