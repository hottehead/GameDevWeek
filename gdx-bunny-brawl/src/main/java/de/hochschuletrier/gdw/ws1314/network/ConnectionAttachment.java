package de.hochschuletrier.gdw.ws1314.network;

public class ConnectionAttachment{
	int playerId;
	String playername;
	long entityid;

	public ConnectionAttachment(int playerId, String playername){
		this.playerId = playerId;
		this.playername = playername;
	}

	public void setEntityId(long entityid){
		this.entityid = entityid;
	}

	public int getPlayerId(){
		return playerId;
	}

	public String getPlayername(){
		return playername;
	}

	public long getEntityId(){
		return entityid;
	}
}
