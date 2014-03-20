package de.hochschuletrier.gdw.ws1314.network;

public class ConnectionAttachment{
	int id;
	String playername;
	long entityid;

	public ConnectionAttachment(int id, String playername){
		this.id = id;
		this.playername = playername;
	}

	public void setEntityId(long entityid){
		this.entityid = entityid;
	}

	public int getId(){
		return id;
	}

	public String getPlayername(){
		return playername;
	}

	public long getEntityId(){
		return entityid;
	}
}
