package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.utils.StringUtils;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by albsi on 20.03.14.
 */
public class NetworkCommands{
	private static final Logger logger = LoggerFactory.getLogger(NetworkCommands.class);

	public NetworkCommands(){
		Main.getInstance().console.register(connectCmd);
		Main.getInstance().console.register(listenCmd);
		Main.getInstance().console.register(sayCmd);
		Main.getInstance().console.register(listenDefCmd);
		Main.getInstance().console.register(stopCmd);
		Main.getInstance().console.register(disconnectCmd);
		Main.getInstance().console.register(devConnectCmd);
		Main.getInstance().console.register(sendDevPlayerUpdateCmd);
		Main.getInstance().console.register(sendLobbyUpdateCmd);
		Main.getInstance().console.register(sendMatchUpdateCmd);
		Main.getInstance().console.register(sendPlayerUpdateCmd);
	}

	private ConsoleCmd connectCmd = new ConsoleCmd("connect", 0, "[CLIENT] Connect to a server.", 2){

		@Override
		public void showUsage(){
			showUsage("<ip> <port>");
		}

		@Override
		public void execute(List<String> args){
			try{
				String ip = args.get(1);
				int port = Integer.parseInt(args.get(2));
				NetworkManager.getInstance().connect(ip, port);
			}
			catch (NumberFormatException e){
				showUsage();
			}
		}
	};

	private ConsoleCmd listenCmd = new ConsoleCmd("listen", 0, "[SERVER] Start listening for client connections. (Become a server.)", 2){

		@Override
		public void showUsage(){
			showUsage("<interface-ip> <port> [max-connections = 10]");
		}

		@Override
		public void execute(List<String> args){
			try{
				String ip = args.get(1);
				int port = Integer.parseInt(args.get(2));
				int maxConnections = 10;
				if(args.size() > 3){
					maxConnections = Integer.parseInt(args.get(3));
				}
				NetworkManager.getInstance().listen(ip, port, maxConnections);
			}
			catch (NumberFormatException e){
				showUsage();
			}
		}
	};

	private ConsoleCmd sayCmd = new ConsoleCmd("say", 0, "Post a message in chat.", 1){
		@Override
		public void showUsage(){
			showUsage("<message-text>");
		}

		@Override
		public void execute(List<String> args){
			NetworkManager.getInstance().sendChat(StringUtils.untokenize(args, 1, -1, false));
		}
	};

	private ConsoleCmd listenDefCmd = new ConsoleCmd("listendef", 0, "[SERVER] Start listening for client connections at " + NetworkManager.getInstance()
			.getDefaultServerIp() + " and Port " + NetworkManager.getInstance().getDefaultPort() + ". (Become a server.)", 0){

		@Override
		public void showUsage(){
			showUsage("");
		}

		@Override
		public void execute(List<String> args){
			try{
				int maxConnections = 10;
				if(args.size() > 3){
					maxConnections = Integer.parseInt(args.get(3));
				}
				NetworkManager.getInstance().listen(NetworkManager.getInstance().getDefaultServerIp(), NetworkManager.getInstance().getDefaultPort(), maxConnections);
			}
			catch (NumberFormatException e){
				logger.error("[SERVER] Can't create Server with default data:", e);
			}
		}
	};

	private ConsoleCmd stopCmd = new ConsoleCmd("stop", 0, "[SERVER] Stops the Server.", 0){

		@Override
		public void showUsage(){
			showUsage("");
		}

		@Override
		public void execute(List<String> args){
			try{
				NetworkManager.getInstance().stopServer();
			}
			catch (NumberFormatException e){
				showUsage();
			}
		}
	};

	private ConsoleCmd disconnectCmd = new ConsoleCmd("disconnect", 0, "[CLIENT] Client disconnects from Server.", 0){

		@Override
		public void showUsage(){
			showUsage("");
		}

		@Override
		public void execute(List<String> args){
			try{
				NetworkManager.getInstance().disconnectFromServer();
			}
			catch (NumberFormatException e){
				logger.error("[CLIENT] Can't disconnect from Server:", e);
			}
		}
	};

	private ConsoleCmd devConnectCmd = new ConsoleCmd("dc", 0, "[CLIENT][DEV CMD] start without arguments for help", 1){

		@Override
		public void showUsage(){
			showUsage("<flag> [l = localhost, t, j, d]\n"
					+ "\t\tl = localhost\n"
					+ "\t\td = 143.93.55.134 (RFT1301)\n"
					+ "\t\tt = 143.93.55.135 (RFT1305)\n"
					+ "\t\tj = 143.93.55.141 (RFT1311)");
		}

		/**
		 * DON'T USE ME
		 * EXCEPT YOU KNOW WHAT YOU DO
		 *
		 * l = localhost
		 * t = 143.93.55.134 (RFT1301)
		 * t = 143.93.55.135 (RFT1305)
		 * j = 143.93.55.141 (RFT1311)
		 * @param args char
		 */
		@Override
		public void execute(List<String> args){
			try{
				logger.warn("[CLIENT][dc] is only for network development tests !");
				if(args.get(1).equals("l")){
					NetworkManager.getInstance().connect("localhost", NetworkManager.getInstance().getDefaultPort());
				}
				else if(args.get(1).equals("t")){
					NetworkManager.getInstance().connect("143.93.55.135", NetworkManager.getInstance().getDefaultPort());
				}
				else if(args.get(1).equals("j")){
					NetworkManager.getInstance().connect("143.93.55.141", NetworkManager.getInstance().getDefaultPort());
				}
				else if(args.get(1).equals("d")){
					NetworkManager.getInstance().connect("143.93.55.134", NetworkManager.getInstance().getDefaultPort());
				}
			}
			catch (Exception e){
				logger.error("[CLIENT] can't connect to server", e);
			}
		}
	};

	private ConsoleCmd sendDevPlayerUpdateCmd = new ConsoleCmd("spu", 0, "[CLIENT][DEV CMD] only for network tests, sendPlayerUpdate.", 1){

		@Override
		public void showUsage(){
			showUsage("<flag> [1 = player 1, 2 = player 2, 3 = player 3]");
		}

		@Override
		public void execute(List<String> args){
			try{
				logger.warn("[CLIENT][spu] is only for network development tests !");
				if(args.get(1).equals("1")){
					NetworkManager.getInstance().sendPlayerUpdate("player1", EntityType.Knight, TeamColor.BLACK, false);
				}
				else if(args.get(1).equals("2")){
					NetworkManager.getInstance().sendPlayerUpdate("player2", EntityType.Hunter, TeamColor.WHITE, false);
				}
				else if(args.get(1).equals("3")){
					NetworkManager.getInstance().sendPlayerUpdate("player3", EntityType.Tank, TeamColor.BOTH, false);
				}
			}
			catch (Exception e){
				logger.error("[CLIENT] can't send data", e);
			}
		}
	};

	private ConsoleCmd sendLobbyUpdateCmd = new ConsoleCmd("sendLobbyUpdate", 0, "[SERVER][DEBUG] Post LobbyUpdate", 0){
		@Override
		public void showUsage(){
			showUsage("");
		}

		@Override
		public void execute(List<String> args){
			Main.getInstance().sendDevLobbyUpdate();
		}
	};

	private ConsoleCmd sendMatchUpdateCmd = new ConsoleCmd("sendMatchUpdate", 0, "[CLIENT][DEBUG] Post a mapname.", 1){
		@Override
		public void showUsage(){
			showUsage("<mapname-text>");
		}

		@Override
		public void execute(List<String> args){
			NetworkManager.getInstance().sendMatchUpdate(StringUtils.untokenize(args, 1, -1, false));
		}
	};

	private ConsoleCmd sendPlayerUpdateCmd = new ConsoleCmd("sendPlayerUpdate", 0, "[CLIENT][DEBUG] Post playerdata.", 1){
		@Override
		public void showUsage(){
			showUsage("<playername>");
		}

		@Override
		public void execute(List<String> args){
			logger.info("[CLIENT] name changed to {}", args.get(1));
			NetworkManager.getInstance().sendPlayerUpdate(args.get(1), EntityType.Noob, TeamColor.BLACK, false);
		}
	};
}
