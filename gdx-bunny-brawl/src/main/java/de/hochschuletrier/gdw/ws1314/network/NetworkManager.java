package de.hochschuletrier.gdw.ws1314.network;

public class NetworkManager {
	private NetworkManager instance = new NetworkManager();
	private NetworkManager(){}
	public NetworkManager getInstance(){
		return instance;
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
}
