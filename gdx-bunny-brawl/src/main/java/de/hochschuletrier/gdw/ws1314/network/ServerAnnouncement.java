package de.hochschuletrier.gdw.ws1314.network;

public class ServerAnnouncement{
	private String address;
	private int port;
	private int clients;
	private boolean busy;
	public ServerAnnouncement(String address, int port, int clients, boolean busy){
		super();
		this.address = address;
		this.port = port;
		this.clients = clients;
		this.busy = busy;
	}
	public String getAddress(){
		return address;
	}
	public int getPort(){
		return port;
	}
	public int getClients(){
		return clients;
	}
	public boolean isBusy(){
		return busy;
	}
}
