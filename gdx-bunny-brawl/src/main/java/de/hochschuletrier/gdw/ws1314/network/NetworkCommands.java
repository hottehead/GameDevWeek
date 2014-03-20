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
public class NetworkCommands {
	private static final Logger logger = LoggerFactory.getLogger(NetworkManager.class);
	private final String defaultIP = "0.0.0.0";
	private final int defaultPort = 666;
	private NetworkManager networkManager;

	public NetworkCommands(NetworkManager networkManager){
		this.networkManager = networkManager;
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

	private ConsoleCmd connectCmd = new ConsoleCmd("connect", 0, "Connect to a server.", 2) {

		@Override
		public void showUsage() {
			showUsage("<ip> <port>");
		}

		@Override
		public void execute(List<String> args) {
			try {
				String ip = args.get(1);
				int port = Integer.parseInt(args.get(2));
				networkManager.connect(ip, port);
			} catch (NumberFormatException e) {
				showUsage();
			}
		}
	};
	private ConsoleCmd listenCmd = new ConsoleCmd("listen", 0, "Start listening for client connections. (Become a server.)", 2) {

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
				if (args.size() > 3) {
					maxConnections = Integer.parseInt(args.get(3));
				}
				networkManager.listen(ip, port, maxConnections);
			} catch (NumberFormatException e) {
				showUsage();
			}
		}
	};

	private ConsoleCmd sayCmd = new ConsoleCmd("say", 0, "Post a message in chat.", 1) {
		@Override
		public void showUsage() {
			showUsage("<message-text>");
		}

		@Override
		public void execute(List<String> args) {
			networkManager.sendChat(StringUtils.untokenize(args, 1, -1, false));
		}
	};

	private ConsoleCmd listenDefCmd = new ConsoleCmd("listendef", 0, "Start listening for client connections at " + defaultIP + " and Port " + defaultPort + ". " +
			"(Become a server.)", 0) {

		@Override
		public void showUsage() {
			showUsage("");
		}

		@Override
		public void execute(List<String> args) {
			try {
				int maxConnections = 10;
				if (args.size() > 3) {
					maxConnections = Integer.parseInt(args.get(3));
				}
				networkManager.listen(defaultIP, defaultPort, maxConnections);
			} catch (NumberFormatException e) {
				logger.error("Can't create Server with default data:", e);
			}
		}
	};

	private ConsoleCmd stopCmd = new ConsoleCmd("stop", 0, "Stops the Server.", 0) {

		@Override
		public void showUsage() {
			showUsage("");
		}

		@Override
		public void execute(List<String> args) {
			try {
				networkManager.stopServer();
			} catch (NumberFormatException e) {
				showUsage();
			}
		}
	};

	private ConsoleCmd disconnectCmd = new ConsoleCmd("disconnect", 0, "Client disconnects from Server.", 0) {

		@Override
		public void showUsage() {
			showUsage("");
		}

		@Override
		public void execute(List<String> args) {
			try {
				networkManager.disconnectFromServer();
			} catch (NumberFormatException e) {
				logger.error("Can't disconnect from Server:", e);
			}
		}
	};

	private ConsoleCmd devConnectCmd = new ConsoleCmd("dc", 0, "[DEV CMD] only for network tests, connect to localhost or to test client", 1) {

		@Override
		public void showUsage() {
			showUsage("<flag> [l = localhost, t = test server, j = jerry]");
		}

		@Override
		public void execute(List<String> args) {
			try {
				logger.warn("[dc] is only for network development tests !");
				if (args.get(1).equals("l")) {
					networkManager.connect("localhost", defaultPort);
				} else if (args.get(1).equals("t")) {
					networkManager.connect("143.93.55.135", defaultPort);
				}else if (args.get(1).equals("j")) {
					networkManager.connect("143.93.55.141", defaultPort);
				}
			} catch (Exception e) {
				logger.error("can't connect to server", e);
			}
		}
	};

	private ConsoleCmd sendDevPlayerUpdateCmd = new ConsoleCmd("spu", 0, "[DEV CMD] only for network tests, sendPlayerUpdate", 1) {

		@Override
		public void showUsage() {
			showUsage("<flag> [1 = player 1, 2 = player 2, 3 = player 3]");
		}

		@Override
		public void execute(List<String> args) {
			try {
				logger.warn("[spu] is only for network development tests !");
				if (args.get(1).equals("1")) {
					NetworkManager.getInstance().sendPlayerUpdate("player1", EntityType.Knight, TeamColor.BLACK, false);
				} else if (args.get(1).equals("2")) {
					NetworkManager.getInstance().sendPlayerUpdate("player2",EntityType.Hunter,TeamColor.WHITE,false);
				}else if (args.get(1).equals("3")) {
					NetworkManager.getInstance().sendPlayerUpdate("player3", EntityType.Tank, TeamColor.BOTH, false);
				}
			} catch (Exception e) {
				logger.error("can't connect to server", e);
			}
		}
	};

	private ConsoleCmd sendLobbyUpdateCmd = new ConsoleCmd("sendLobbyUpdate",0,"[DEBUG]", 0) {
		@Override
		public void showUsage() {
			showUsage("");
		}

		@Override
		public void execute(List<String> args) {
			Main.getInstance().sendDevLobbyUpdate();
		}
	};

	private ConsoleCmd sendMatchUpdateCmd = new ConsoleCmd("sendMatchUpdate",0,"[DEBUG]Post a mapname.",1) {
		@Override
		public void showUsage() {
			showUsage("<mapname-text>");
		}

		@Override
		public void execute(List<String> args) {
			NetworkManager.getInstance().sendMatchUpdate(StringUtils.untokenize(args, 1, -1, false));
		}
	};

	private ConsoleCmd sendPlayerUpdateCmd = new ConsoleCmd("sendPlayerUpdate",0,"[DEBUG]Post playerdata",1) {
		@Override
		public void showUsage() {
			showUsage("<playername>");
		}

		@Override
		public void execute(List<String> args) {
			logger.info(args.get(1));
			NetworkManager.getInstance().sendPlayerUpdate(args.get(1),EntityType.Noob,TeamColor.BLACK,false);
		}
	};
}
