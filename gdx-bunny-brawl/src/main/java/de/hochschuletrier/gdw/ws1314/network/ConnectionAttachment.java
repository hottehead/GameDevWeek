package de.hochschuletrier.gdw.ws1314.network;

public class ConnectionAttachment {
	int id;
	String playername;
	
	public ConnectionAttachment(int id, String playername){
		this.id = id;
		this.playername = playername;
	}

	public int getId() {
		return id;
	}

	public String getPlayername() {
		return playername;
	}
}
