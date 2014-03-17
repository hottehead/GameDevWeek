package de.hochschuletrier.gdw.ws1314.network;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.NetReception;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagramFactory;

public class NetworkManager {
    private static final Logger logger = LoggerFactory.getLogger(NetworkManager.class);
	private NetworkManager instance = new NetworkManager();

	private NetConnection clientConnection=null;
	private ArrayList<NetConnection> serverConnections=null;
	private NetReception serverReception=null;
	private INetDatagramFactory datagramFactory;//TODO Implement
	
	private NetworkManager(){}
	public NetworkManager getInstance(){
		return instance;
	}
	
	public void connect(String ip, int port){
		if(isClient()){
			logger.warn("Ignoring new connect command because we are already connected.");
		}
		try {
			clientConnection=new NetConnection(ip, port, datagramFactory);
		} catch (IOException e) {
			logger.error("Can't connect.",e);
		}
	}
	
	public void listen(String ip, int port, int maxConnections){
		if(isServer()){
			logger.warn("Ignoring new listen command because we are already a server.");
		}
		serverConnections=new ArrayList<NetConnection>();
		try {
			serverReception = new NetReception(ip, port, maxConnections, datagramFactory);
		} catch (IOException e) {
			logger.error("Can't listen for connections.", e);
			serverConnections=null;
			serverReception=null;
		}
	}
	
	public boolean isServer() {
		return serverConnections!=null && serverReception!=null;
	}

	public boolean isClient() {
		return clientConnection!=null && clientConnection.isConnected();
	}

	public void sendEntityEvent(long id, int eventPlayerIntention){
		//TODO: Implement
	}
	
	public void sendAction(int eventPlayerIntention){
		//TODO: Implement
	}
	
	public void despawnEntity(long id){
		//TODO: Implement
	}
	
	public void sendChat(String text){
		//TODO: Implement		
	}
	
	public void addChatListener(ChatListener listener){
		//TODO: Implement
	}
	
	public void removeChatListener(ChatListener listener){
		//TODO: Implement
	}
	
	public void receiveChat(String sender,String text){
		//TODO: Implement
	}
}
