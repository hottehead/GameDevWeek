package de.hochschuletrier.gdw.ws1314.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.NetReception;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagramFactory;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.network.datagrams.BaseDatagram;
import de.hochschuletrier.gdw.ws1314.network.datagrams.ChatDeliverDatagram;
import de.hochschuletrier.gdw.ws1314.network.datagrams.ChatSendDatagram;

public class NetworkManager {

    private static final Logger logger = LoggerFactory.getLogger(NetworkManager.class);
	private static NetworkManager instance = new NetworkManager();

	private NetConnection clientConnection=null;
	private ArrayList<NetConnection> serverConnections=null;
	private NetReception serverReception=null;
	private INetDatagramFactory datagramFactory=new DatagramFactory();
	
	private ServerGameDatagramHandler serverGameDgramHandler = new ServerGameDatagramHandler();
	private ServerLobbyDatagramHandler serverLobbyDgramHandler = new ServerLobbyDatagramHandler();
	private ClientGameDatagramHandler clientGameDgramHandler = new ClientGameDatagramHandler();
	private ClientLobbyDatagramHandler clientLobbyDgramHandler = new ClientLobbyDatagramHandler();
	
	private ArrayList<ChatListener> chatListeners = new ArrayList<ChatListener>();
	
	private NetworkManager(){}
	public static NetworkManager getInstance(){
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
		return serverConnections!=null && serverReception!=null && serverReception.isRunning();
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
		if(isClient()){
			clientConnection.send(new ChatSendDatagram(text));
		}
		else if (isServer()){
			broadcastToClients(new ChatDeliverDatagram("SERVER",text));
		}
		else {
			logger.error("Can't send chat message, when not connected.");
		}
	}
	
	public void addChatListener(ChatListener listener){
		chatListeners.add(listener);
	}
	
	public void removeChatListener(ChatListener listener){
		chatListeners.remove(listener);
	}
	
	/**
	 * Wird von der Verarbeitungslogik für Chat-Datagramme verwendet, um Chat-Nachrichten an den Listener zuzustellen.
	 * Aufruf von anderer Stelle ist eher nicht sinnvoll. 
	 * @param sender
	 * @param text
	 */
	void receiveChat(String sender,String text){
		for(ChatListener l : chatListeners){
			l.chatMessage(sender,text);
		}
	}
	
	/**
	 * Wird innerhalb der server-seitigen NEtzwerklogik verwendet, um Pakete an alle Clients zu schicken.
	 * @param dgram
	 */
	void broadcastToClients(BaseDatagram dgram){
		if(!isServer()){
			logger.warn("Request to broadast datagram to clients will be ignored because of non-server context.");
			return;
		}
		for(NetConnection con : serverConnections){
			con.send(dgram);
		}
	}
	
	public void disconnectFromServer(){
		if(isClient()){
			clientConnection.shutdown();
			clientConnection=null;
		}
	}
	
	public void init(){
		Main.getInstance().console.register(connectCmd);
		Main.getInstance().console.register(listenCmd);
		Main.getInstance().console.register(sayCmd);
		addChatListener(new ConsoleChatListener());
	}
	
	public void update(){
		handleNewConnections();
		handleDatagramsClient();
		handleDatagramsServer();
	}
	
	private void handleNewConnections(){
		if(isServer()) {
			NetConnection connection = serverReception.getNextNewConnection();
			while (connection != null) {
				connection.setAccepted(true);
				serverConnections.add(connection);
				connection = serverReception.getNextNewConnection();
			}
		}
	}
	
	private void handleDatagramsClient(){
		if(!isClient()) return;
		
		DatagramHandler handler=clientGameDgramHandler;
		
		clientConnection.sendPendingDatagrams();
		while(clientConnection.hasIncoming()){
			INetDatagram dgram = clientConnection.receive();
			if(dgram instanceof BaseDatagram){
				((BaseDatagram) dgram).handle(handler, clientConnection);
			}
		}
	}
	
	private void handleDatagramsServer(){
		if(!isServer()) return;
		
		DatagramHandler handler=serverGameDgramHandler;
		
		Iterator<NetConnection> it = serverConnections.iterator();
		while (it.hasNext()) {
			NetConnection connection = it.next();
			connection.sendPendingDatagrams();
			
			while(connection.hasIncoming()){
				INetDatagram dgram = connection.receive();
				if(dgram instanceof BaseDatagram){
					((BaseDatagram) dgram).handle(handler, connection);
				}
			}
			
			if (!connection.isConnected()) {
				logger.info("Client disconnected.");
				it.remove();
				continue;
			}
		}
	}
	private ConsoleCmd connectCmd = new ConsoleCmd("connect",0,"Connect to a server.",2) {
		
		@Override
		public void showUsage() {
			showUsage("<ip> <port>");
		}

		@Override
		public void execute(List<String> args) {
			try {
				String ip = args.get(1);
				int port = Integer.parseInt(args.get(2));
				connect(ip, port);
			} catch (NumberFormatException e) {
				showUsage();
			}
		}
	};
	private ConsoleCmd listenCmd = new ConsoleCmd("listen",0,"Start listening for client connections. (Become a server.)",2) {
		
		@Override
		public void showUsage() {
			showUsage("<interface-ip> <port> [max-connections = 10]");
		}

		@Override
		public void execute(List<String> args) {
			try {
				String ip = args.get(1);
				int port = Integer.parseInt(args.get(2));
				int maxConnections = 10;
				if(args.size()>3){
					maxConnections=Integer.parseInt(args.get(3));
				}
				listen(ip, port, maxConnections);
			} catch (NumberFormatException e) {
				showUsage();
			}
		}
	};
	private ConsoleCmd sayCmd = new ConsoleCmd("say",0,"Post a message in chat.",1) {
		@Override
		public void showUsage() {
			showUsage("<message-text>");
		}

		@Override
		public void execute(List<String> args) {
			sendChat(args.get(1));
		}
	};
}
