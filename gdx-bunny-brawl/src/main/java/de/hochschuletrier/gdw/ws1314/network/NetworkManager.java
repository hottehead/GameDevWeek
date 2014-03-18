package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.ws1314.input.PlayerIntention;

public class NetworkManager {
	private static NetworkManager instance = new NetworkManager();
	private NetworkManager(){}
	public static NetworkManager getInstance(){
		return instance;
	}
	
	public void sendEntityEvent(long id, int eventPlayerIntention){
		//TODO: Implement
	}
	
	public void sendAction(PlayerIntention eventPlayerIntention){
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
}
