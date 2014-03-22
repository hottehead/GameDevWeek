package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.NetReception;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagramFactory;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.basic.GameInfoListener;
import de.hochschuletrier.gdw.ws1314.entity.*;
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
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NetworkManager{
	private static final Logger logger = LoggerFactory.getLogger(NetworkManager.class);

	private static NetworkManager instance = new NetworkManager();
	private NetConnection clientConnection = null;
	private ArrayList<NetConnection> serverConnections = null;
	private NetReception serverReception = null;
	private INetDatagramFactory datagramFactory = new DatagramFactory();

	private ServerDatagramHandler serverDgramHandler = new ServerDatagramHandler();
	private ClientDatagramHandler clientDgramHandler = new ClientDatagramHandler();
	private ArrayList<ChatListener> chatListeners = new ArrayList<>();

	private DisconnectCallback disconnectcallback;
	private PlayerDisconnectCallback playerdisconnectcallback;
	private ClientIdCallback clientidcallback;
	private LobbyUpdateCallback lobbyupdatecallback;
	private PlayerUpdateCallback playerupdatecallback;
	private MatchUpdateCallback matchupdatecallback;
	private GameStateCallback gameStateCallback;

	private long lastStatTime = System.currentTimeMillis();
	private long lastTotalBytesSent = 0;
	private long lastTotalBytesReceived = 0;
	private long lastTotalDgramsSent = 0;
	private long lastTotalDgramsReceived = 0;
	private float ping = 0;

	private int nextPlayerNumber = 1;
	
	protected Deque<DespawnDatagram> pendingDespawns = new LinkedList<>();

	public float getPing(){
		return ping;
	}

	void updatePing(float newPing){
		ping = 0.9f * ping + 0.1f * newPing;
	}

	public void checkStats(){
		long newStatTime = System.currentTimeMillis();
		long statDT = newStatTime - lastStatTime;
		if(statDT < 60000) return;
		long newTotalBytesSent = 0;
		long newTotalBytesReceived = 0;
		long newTotalDgramsSent = 0;
		long newTotalDgramsReceived = 0;
		if(serverConnections != null){
			for(NetConnection con : serverConnections){
				newTotalBytesSent += con.getBytesSent();
				newTotalBytesReceived += con.getBytesReceived();
				newTotalDgramsSent += con.getDatagramsSent();
				newTotalDgramsReceived += con.getDatagramsReceived();
			}
		}
		if(clientConnection != null){
			newTotalBytesSent += clientConnection.getBytesSent();
			newTotalBytesReceived += clientConnection.getBytesReceived();
			newTotalDgramsSent += clientConnection.getDatagramsSent();
			newTotalDgramsReceived += clientConnection.getDatagramsReceived();
		}
		long deltaBytesSent = newTotalBytesSent - lastTotalBytesSent;
		long deltaBytesReceived = newTotalBytesReceived - lastTotalBytesReceived;
		long deltaDgramsSent = newTotalDgramsSent - lastTotalDgramsSent;
		long deltaDgramsReceived = newTotalDgramsReceived - lastTotalDgramsReceived;
		double bytesSentPerSecond = deltaBytesSent / (statDT / 1000.0);
		double bytesReceivedPerSecond = deltaBytesReceived / (statDT / 1000.0);
		double dgramsSentPerSecond = deltaDgramsSent / (statDT / 1000.0);
		double dgramsReceivedPerSecond = deltaDgramsReceived / (statDT / 1000.0);
		double factor = 1024.0;
		double rF = 100.0;//Rounding Factor
		if(isClient()){
			logger.info("Network Statistics: Ping {} ms", ping);
		}
		else{
			logger.info("Network Statistics:");
		}

		logger.info("Sent: {} KiB, {} Byte/s, {} Dgrams, {} Dgrams/s",
				newTotalBytesSent / factor, Math.round(bytesSentPerSecond * rF) / rF,
				newTotalDgramsSent, Math.round(dgramsSentPerSecond * rF) / rF);
		logger.info("Rec: {} KiB, {} Byte/s, {} Dgrams, {} Dgrams/s",
				newTotalBytesReceived / factor, Math.round(bytesReceivedPerSecond * rF) / rF,
				newTotalDgramsReceived, Math.round(dgramsReceivedPerSecond * rF) / rF);
		lastStatTime = newStatTime;
		lastTotalBytesSent = newTotalBytesSent;
		lastTotalBytesReceived = newTotalBytesReceived;
		lastTotalDgramsSent = newTotalDgramsSent;
		lastTotalDgramsReceived = newTotalDgramsReceived;
	}

	private NetworkManager(){
	}

	public static NetworkManager getInstance(){
		return instance;
	}

	private boolean isPortOk(int port){
		if(port >=  1024 && port <= 65535){
			return true;
		}
		else {
			return false;
		}
	}

	public void connect(String ip, int port){
		if(isClient()){
			logger.warn("[CLIENT] Ignoring new connect command because we are already connected.");
			return;
		}
		if(!isPortOk(port)){
			throw new IllegalArgumentException("[CLIENT] Port out of allowed range 1024 - 65535");
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
		if(!isPortOk(port)){
			throw new IllegalArgumentException("[SERVER] Port out of allowed range 1024 - 65535");
		}
		serverConnections = new ArrayList<>();
		try{
			serverReception = new NetReception(ip, port, maxConnections, datagramFactory);
			if(serverReception.isRunning()){
				logger.info("[SERVER] for {} players is running and listening at {}:{}", maxConnections, getMyIp(), port);
			}
			ServerEntityManager.getInstance().getGameInfo().addListner(gameInfoListener);
		}
		catch (IOException e){
			logger.error("[SERVER] Can't listen for connections.", e);
			serverConnections = null;
			serverReception = null;
		}
	}

	/**
	 * ONLY FOR LISTEN OF A SERVER
	 */
	public String getDefaultServerIp(){
		return "0.0.0.0";
	}

	/**
	 * Default Port
	 */
	public int getDefaultPort(){
		return 54293;
	}

	/**
	 * DisconnectCallback: wird auf Server und Clientseite aufgerufen, sobald die eigene Verbindung verloren geht.
	 * z.B: Client disconnected daraufhin wir dieser Callback aufgerufen, damit der GameState geändert werden kann
	 */
	public DisconnectCallback getDisconnectCallback(){
		return disconnectcallback;
	}

	/**
	 * PlayerDisconnectCallback: Serverseitig. Wenn einer oder mehrere Clients disconnecten, wird deren ID
	 * in diesem Callback mtigegeben damit die Serverdaten angepasst werden können.
	 * Danach ist eine Funktion wie LobbyUpdate notwendig um diese änderung den Clients mitzutzeilen
	 */
	public PlayerDisconnectCallback getPlayerDisconnectCallback(){
		return playerdisconnectcallback;
	}

	/**
	 * ClientIdCallback: Clientseitig, dem Clienten wird seine ID mitgeteilt
	 */
	public ClientIdCallback getClientIdCallback(){
		return clientidcallback;
	}

	/**
	 * LobbyUpdateCallback: Clientseitig, dem Clienten wird die neue Spielerliste und Map zugeteilt
	 */
	public LobbyUpdateCallback getLobbyUpdateCallback(){
		return lobbyupdatecallback;
	}

	/**
	 * Serverseitig: der Client teilt seine Spielerdaten mit(Spielername, Klasse, Team und Accept)
	 */
	public PlayerUpdateCallback getPlayerUpdateCallback(){
		return playerupdatecallback;
	}

	/**
	 * Serverseitig: der Client teilt einen Mapvorschlag mit
	 */
	public MatchUpdateCallback getMatchUpdateCallback(){
		return matchupdatecallback;
	}

	/**
	 * Clientseitig: der Server teilt dem Clienten den GameState mit
	 */
	public GameStateCallback getGameStateCallback(){
		return gameStateCallback;
	}

	public String getMyIp(){
		try{
			return InetAddress.getLocalHost().getHostAddress();
		}
		catch (Exception e){
			logger.error("[NETWORK] error at reading local host IP, fallback to localhost\n{}", e);
			return "127.0.0.1";
		}
	}

	public void setDisconnectCallback(DisconnectCallback callback){
		this.disconnectcallback = callback;
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
		if(!isServer()) return;
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
		for(PlayerData pd : players){
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
	 * @param sender String
	 * @param text   String
	 */
	void receiveChat(String sender, String text){
		for(ChatListener l : chatListeners){
			l.chatMessage(sender, text);
		}
	}

	/**
	 * Wird innerhalb der server-seitigen Netzwerklogik verwendet, um Pakete an alle Clients zu schicken.
	 *
	 * @param dgram BaseDatagram
	 */
	void broadcastToClients(BaseDatagram dgram){
		if(!isServer()){
			logger.warn("[SERVER] Request to broadast datagram to clients will be ignored because of non-server context.");
			return;
		}
		for(NetConnection con : serverConnections){
			con.send(dgram);
		}
	}

	public void disconnectFromServer(){
		if(isClient()){
			if(this.disconnectcallback!=null) this.disconnectcallback.disconnectCallback("[CLIENT] Leave Server.");
			clientConnection.shutdown();
		}
	}

	public void init(){
		new NetworkCommands();
		addChatListener(new ConsoleChatListener());
	}

	public void update(){
		handlePendingDespawns();
		handleNewConnections();
		handleDisconnects();
		replicateServerEntities();
		handleDatagramsClient();
		handleDatagramsServer();
		checkStats();
		if(isClient()) clientConnection.send(new PingDatagram(System.currentTimeMillis()));
	}

	private void handlePendingDespawns(){
		if(!isClient()) return;
		DespawnDatagram despawn;
		while((despawn=pendingDespawns.poll())!=null){
			clientDgramHandler.handle(despawn, clientConnection);
		}
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
				//Nothing more required here, zones need no replication to the client.
			}
			else{
				logger.warn("[SERVER] Unknown entity type {} can't be replicated.", entity.getClass().getCanonicalName());
			}
		}
	}

	private void handleNewConnections(){
		if(isServer()){
			if(Main.getInstance().getCurrentState() == GameStates.SERVERGAMEPLAY.get()){
				NetConnection connection = serverReception.getNextNewConnection();
				while(connection != null){
					connection.shutdown();
					connection = serverReception.getNextNewConnection();
				}
				return;
			}
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
			List<NetConnection> toRemove = new ArrayList<>();
			for(NetConnection c : serverConnections){
				if(!c.isConnected()){
					toRemove.add(c);
				}
			}
			if(toRemove.size() > 0){
				List<Integer> ids = new ArrayList<>();
				for(NetConnection rc : toRemove){
					serverConnections.remove(rc);
					logger.info("[SERVER] {} disconnected.", ((ConnectionAttachment) rc.getAttachment()).getPlayername());
					broadcastToClients(new ChatDeliverDatagram("[SERVER]", ((ConnectionAttachment) rc.getAttachment()).playername + " disconnected."));
					ids.add(((ConnectionAttachment) rc.getAttachment()).getId());
				}
				if(this.playerdisconnectcallback != null){
					this.playerdisconnectcallback.playerDisconnectCallback(ids.toArray(new Integer[ids.size()]));
				}
			}
		}
		if(clientConnection != null && !clientConnection.isConnected()){
			if(this.disconnectcallback != null){
				this.disconnectcallback.disconnectCallback("[NETWORK] Disconnected from Server.");
			}
			clientConnection = null;
		}
	}

	private void handleDatagrams(DatagramHandler handler, NetConnection connection) {
		connection.sendPendingDatagrams();
		while(connection.hasIncoming()){
			INetDatagram dgram = connection.receive();
			if(dgram instanceof BaseDatagram){
				((BaseDatagram) dgram).handle(handler, connection);
			}
		}
	}

	private void handleDatagramsClient(){
		if(!isClient()) return;
		handleDatagrams(clientDgramHandler, clientConnection);
	}

	private void handleDatagramsServer(){
		if(!isServer()) return;
		Iterator<NetConnection> it = serverConnections.iterator();
		while(it.hasNext()){
			NetConnection connection = it.next();
			handleDatagrams(serverDgramHandler, connection);
			
			if(!connection.isConnected()){
				logger.info("[SERVER] {} disconnected.", ((ConnectionAttachment) connection.getAttachment()).getPlayername());
				it.remove();
			}
		}
	}

	public void setPlayerEntityId(int playerId, long entityId){
		for(NetConnection nc : serverConnections){
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
				if(this.disconnectcallback != null){
					this.disconnectcallback.disconnectCallback("[SERVER] Stopped.");
				}
				ServerEntityManager.getInstance().getGameInfo().removeListner(gameInfoListener);
				for(NetConnection nc : serverConnections){
					nc.shutdown();
				}
				nextPlayerNumber = 1;
				serverConnections = new ArrayList<>();
				serverReception.shutdown();
				serverReception = null;
			}
			else{
				logger.warn("[NETWORK] Can't stop, i'm not a Server.");
			}
		}
		catch (Exception e){
			logger.error("[SERVER] Can't Stop Server.", e);
		}
	}

	public void dispose(){
		if(isServer()){
			stopServer();
		}
		if(isClient()){
			disconnectFromServer();
		}
	}
	
	private GameInfoListener gameInfoListener = new GameInfoListener(){
		
		@Override
		public void gameInfoChanged(int blackPoints, int whitePoints, int remainingEgg){
			broadcastToClients(new GameInfoReplicationDatagram(blackPoints,whitePoints,remainingEgg));
		}
	};
}
