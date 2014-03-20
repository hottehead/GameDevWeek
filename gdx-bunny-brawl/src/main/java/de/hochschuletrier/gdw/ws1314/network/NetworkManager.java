package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.NetReception;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagramFactory;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.Zone;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ServerLevelObject;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ServerProjectile;
import de.hochschuletrier.gdw.ws1314.input.PlayerIntention;
import de.hochschuletrier.gdw.ws1314.network.datagrams.*;
import de.hochschuletrier.gdw.ws1314.states.GameStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NetworkManager{

	private static final Logger logger = LoggerFactory.getLogger(NetworkManager.class);

	private static NetworkManager instance = new NetworkManager();

	private final String DEFAULT_SERVER_IP = "0.0.0.0";
	private final int DEFAULT_PORT = 54293;

	private NetConnection clientConnection = null;
	private ArrayList<NetConnection> serverConnections = null;
	private NetReception serverReception = null;
	private INetDatagramFactory datagramFactory = new DatagramFactory();

	private ServerDatagramHandler serverDgramHandler = new ServerDatagramHandler();
	private ClientDatagramHandler clientDgramHandler = new ClientDatagramHandler();
	private ArrayList<ChatListener> chatListeners = new ArrayList<ChatListener>();

	private int nextPlayerNumber = 1;

	private PlayerDisconnectCallback playerdisconnectcallback;
	private ClientIdCallback clientidcallback;
	private LobbyUpdateCallback lobbyupdatecallback;
	private PlayerUpdateCallback playerupdatecallback;
	private MatchUpdateCallback matchupdatecallback;
	private GameStateCallback gameStateCallback;
	
	private long lastStatTime=System.currentTimeMillis();
	private long lastTotalBytesSent=0;
	private long lastTotalBytesReceived=0;
	
	public void checkStats(){
		long newStatTime=System.currentTimeMillis();
		long statDT=newStatTime-lastStatTime;
		if(statDT<10000) return;
		long newTotalBytesSent=0;
		long newTotalBytesReceived=0;
		if(serverConnections!=null){
			for(NetConnection con: serverConnections){
				newTotalBytesSent+=con.getBytesSent();
				newTotalBytesReceived+=con.getBytesReceived();
			}
		}
		if(clientConnection!=null){
			newTotalBytesSent+=clientConnection.getBytesSent();
			newTotalBytesReceived+=clientConnection.getBytesReceived();
		}
		long deltaSent=newTotalBytesSent-lastTotalBytesSent;
		long deltaReceive=newTotalBytesReceived-lastTotalBytesReceived;
		double bytesSentPerSecond = deltaSent /(statDT/1000.0);
		double bytesReceivedPerSecond = deltaReceive /(statDT/1000.0);
		double factor=1024.0;
		logger.info("Network Statistics: ");
		logger.info("Sent: {} KB, {} KB/s",newTotalBytesSent/factor,bytesSentPerSecond/factor);
		logger.info("Rec: {} KB, {} KB/s",newTotalBytesReceived/factor,bytesReceivedPerSecond/factor);
		lastStatTime=newStatTime;
		lastTotalBytesSent=newTotalBytesSent;
		lastTotalBytesReceived=newTotalBytesReceived;
	}

	private NetworkManager(){
	}

	public static NetworkManager getInstance(){
		return instance;
	}

	public void connect(String ip, int port){
		if(isClient()){
			logger.warn("[CLIENT] Ignoring new connect command because we are already connected.");
			return;
		}
		try{
			clientConnection = new NetConnection(ip, port, datagramFactory);
			if(clientConnection.isAccepted()) logger.info("[CLIENT] connected to server {}:{}", ip, port);
		}
		catch (IOException e){
			logger.error("[CLIENT] Can't connect.", e);
		}
	}

	public void listen(String ip, int port, int maxConnections){
		if(isServer()){
			logger.warn("[SERVER] Ignoring new listen command because we are already a server.");
			return;
		}
		serverConnections = new ArrayList<NetConnection>();
		try{
			serverReception = new NetReception(ip, port, maxConnections, datagramFactory);

			if(serverReception.isRunning()){
				logger.info("[SERVER] is running and listening at {}:{}", getMyIp(), port);
			}
		}
		catch (IOException e){
			logger.error("[SERVER] Can't listen for connections.", e);
			serverConnections = null;
			serverReception = null;
		}
	}

	public PlayerDisconnectCallback getPlayerDisconnectCallback(){
		return playerdisconnectcallback;
	}

	public ClientIdCallback getClientIdCallback(){
		return clientidcallback;
	}

	public LobbyUpdateCallback getLobbyUpdateCallback(){
		return lobbyupdatecallback;
	}

	public PlayerUpdateCallback getPlayerUpdateCallback(){
		return playerupdatecallback;
	}

	public MatchUpdateCallback getMatchUpdateCallback(){
		return matchupdatecallback;
	}

	public GameStateCallback getGameStateCallback(){
		return gameStateCallback;
	}

	/**
	 * ONLY FOR LISTEN OF A SERVER
	 */
	public String getDefaultServerIp(){
		return DEFAULT_SERVER_IP;
	}

	/**
	 * Default Port
	 */
	public int getDefaultPort(){
		return DEFAULT_PORT;
	}

	public String getMyIp(){
		try{
			return InetAddress.getLocalHost().getHostAddress().toString();
		}catch (Exception e){
			logger.error("NWM: error at reading local host IP, fallback to localhost\n{}", e);
			return "127.0.0.1";
		}
	}

	public void setPlayerDisconnectCallback(PlayerDisconnectCallback callback){
		this.playerdisconnectcallback = callback;
	}

	public void setClientIdCallback(ClientIdCallback callback){
		this.clientidcallback = callback;
	}

	public void setLobbyUpdateCallback(LobbyUpdateCallback callback){
		this.lobbyupdatecallback = callback;
	}

	public void setPlayerUpdateCallback(PlayerUpdateCallback callback){
		this.playerupdatecallback = callback;
	}

	public void setMatchUpdateCallback(MatchUpdateCallback callback){
		this.matchupdatecallback = callback;
	}

	public void setGameStateCallback(GameStateCallback gameStateCallback){
		this.gameStateCallback = gameStateCallback;
	}

	public boolean isServer(){
		return serverConnections != null && serverReception != null && serverReception.isRunning();
	}

	public boolean isClient(){
		return clientConnection != null && clientConnection.isConnected();
	}

	public void sendEntityEvent(long id, EventType event){
		if(!isServer()) return;
		broadcastToClients(new EventDatagram(id, event));
	}

	public void sendAction(PlayerIntention playerAction){
		if(!isClient()) return;
		clientConnection.send(new ActionDatagram(playerAction));
	}

	public void despawnEntity(long id){
		if(!isClient()) return;
		broadcastToClients(new DespawnDatagram(id));
	}

	public void sendGameState(GameStates gameStates){
		if(!isServer()) return;
		broadcastToClients(new GameStateDatagram(gameStates));
	}

	private void sendClientId(NetConnection con){
		if(!isServer()) return;
		con.send(new ClientIdDatagram(((ConnectionAttachment) con.getAttachment()).getId()));
	}

	public void sendMatchUpdate(String map){
		if(!isClient()) return;
		clientConnection.send(new MatchUpdateDatagram(map));
	}

	public void sendPlayerUpdate(String playerName, EntityType type, TeamColor team, boolean accept){
		if(!isClient()) return;
		clientConnection.send(new PlayerUpdateDatagram(playerName, type, team, accept));
	}

	public void sendLobbyUpdate(String map, PlayerData[] players){
		if(!isServer()) return;
		for(PlayerData pd:players){
			logger.info("[SERVER] Player: " + pd.toString());
		}
		broadcastToClients(new LobbyUpdateDatagram(map, players));
	}

	public void sendChat(String text){
		if(isClient()){
			clientConnection.send(new ChatSendDatagram(text));
		}
		else if(isServer()){
			broadcastToClients(new ChatDeliverDatagram("SERVER", text));
			receiveChat("SERVER", text);
		}
		else{
			logger.error("[CLIENT] Can't send chat message, when not connected.");
		}
	}

	public void addChatListener(ChatListener listener){
		chatListeners.add(listener);
	}

	public void removeChatListener(ChatListener listener){
		chatListeners.remove(listener);
	}

	/**
	 * Wird von der Verarbeitungslogik für Chat-Datagramme verwendet, um Chat-Nachrichten an den Listener zuzustellen. Aufruf von anderer Stelle ist eher nicht
	 * sinnvoll.
	 * 
	 * @param sender
	 * @param text
	 */
	void receiveChat(String sender, String text){
		for(ChatListener l:chatListeners){
			l.chatMessage(sender, text);
		}
	}

	/**
	 * Wird innerhalb der server-seitigen Netzwerklogik verwendet, um Pakete an alle Clients zu schicken.
	 * 
	 * @param dgram
	 */
	void broadcastToClients(BaseDatagram dgram){
		if(!isServer()){
			logger.warn("[SERVER] Request to broadast datagram to clients will be ignored because of non-server context.");
			return;
		}
		for(NetConnection con:serverConnections){
			con.send(dgram);
		}
	}

	public void disconnectFromServer(){
		if(isClient()){
			clientConnection.shutdown();
			clientConnection = null;
			logger.info("[CLIENT] disconnected from server.");
		}
	}

	public void init(){
		new NetworkCommands();
		addChatListener(new ConsoleChatListener());
	}

	public void update(){
		handleNewConnections();
		handleDisconnects();
		replicateServerEntities();
		handleDatagramsClient();
		handleDatagramsServer();
		checkStats();
	}

	private void replicateServerEntities(){
		if(!isServer()) return;
		for(int i = 0;i < ServerEntityManager.getInstance().getListSize();++i){
			ServerEntity entity = ServerEntityManager.getInstance().getListEntity(i);
			if(entity instanceof ServerProjectile){
				broadcastToClients(new ProjectileReplicationDatagram((ServerProjectile) entity));
			}
			else if(entity instanceof ServerLevelObject){
				broadcastToClients(new LevelObjectReplicationDatagram((ServerLevelObject) entity));
			}
			else if(entity instanceof ServerPlayer){
				broadcastToClients(new PlayerReplicationDatagram((ServerPlayer) entity));
			}
			else if(entity instanceof Zone){
				//Intentionally ignored.
			}
			else{
				logger.warn("[SERVER] Unknown entity type {} can't be replicated.", entity.getClass().getCanonicalName());
			}
		}
	}

	private void handleNewConnections(){
		if(isServer()){
			NetConnection connection = serverReception.getNextNewConnection();
			while(connection != null){
				connection.setAccepted(true);
				connection.setAttachment(new ConnectionAttachment(nextPlayerNumber, "Player " + (nextPlayerNumber++)));
				serverConnections.add(connection);
				this.sendClientId(connection);
				logger.info("[SERVER] Player {} connected.", (nextPlayerNumber - 1));
				connection = serverReception.getNextNewConnection();
			}
		}
	}

	private void handleDisconnects(){
		if(isServer()){
			List<NetConnection> toRemove = new ArrayList<NetConnection>();
			for(NetConnection c:serverConnections){
				if(!c.isConnected()){
					toRemove.add(c);
				}
			}
			if(toRemove.size() > 0){
				List<Integer> ids = new ArrayList<Integer>();
				for(NetConnection rc:toRemove){
					serverConnections.remove(rc);
					logger.info("[SERVER] {} disconnected", ((ConnectionAttachment)rc.getAttachment()).getPlayername());
					broadcastToClients(new ChatDeliverDatagram("[SERVER]", ((ConnectionAttachment)rc.getAttachment()).playername+" disconnected"));
					ids.add(((ConnectionAttachment) rc.getAttachment()).getId());
				}
				playerdisconnectcallback.callback(ids.toArray(new Integer[ids.size()]));
			}
		}
		if(isClient()){

		}
	}

	private void handleDatagramsClient(){
		if(!isClient()) return;

		DatagramHandler handler = clientDgramHandler;

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

		DatagramHandler handler = serverDgramHandler;

		Iterator<NetConnection> it = serverConnections.iterator();
		while(it.hasNext()){
			NetConnection connection = it.next();
			connection.sendPendingDatagrams();

			while(connection.hasIncoming()){
				INetDatagram dgram = connection.receive();
				if(dgram instanceof BaseDatagram){
					((BaseDatagram) dgram).handle(handler, connection);
				}
			}

			if(!connection.isConnected()){
				logger.info("[SERVER] {} disconnected.", ((ConnectionAttachment) connection.getAttachment()).getPlayername());
				it.remove();
			}
		}
	}

	public void setPlayerEntityId(int playerId, long entityId){
		for(NetConnection nc:serverConnections){
			ConnectionAttachment tmp = (ConnectionAttachment) nc.getAttachment();
			if(tmp.getId() == playerId){
				tmp.setEntityId(entityId);
				nc.send(new EntityIDDatagram(entityId));
				break;
			}
		}
	}

	public void stopServer(){
		try{
			if(isServer()){
				for (NetConnection nc:serverConnections){
					nc.shutdown();
				}
				serverReception.shutdown();
				logger.info("[SERVER] stopped");
			}
			else{
				logger.warn("[SERVER] Can't stop, i'm not a Server.");
			}
		}
		catch (Exception e){
			logger.error("[SERVER] Can't Stop Server:", e);
		}
	}
}
