package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.commons.devcon.CCmdFlags;
import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.utils.StringUtils;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NetworkCommands{
	private static final Logger logger = LoggerFactory.getLogger(NetworkCommands.class);
	private boolean isCheatMode = false;

	public NetworkCommands(){
		ConsoleCmd connectCmd = new ConsoleCmd("connect", 0, "[CLIENT] Connect to a server.", 2){

			@Override
			public void showUsage(){
				showUsage("<ip> <port> (port 1024 - 65535 allowed)");
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
		Main.getInstance().console.register(connectCmd);
		ConsoleCmd listenCmd = new ConsoleCmd("listen", 0, "[SERVER] Start listening for client connections. (Become a server.)", 2){

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
		Main.getInstance().console.register(listenCmd);
		ConsoleCmd sayCmd = new ConsoleCmd("say", 0, "Post a message in chat.", 1){
			@Override
			public void showUsage(){
				showUsage("<message-text>");
			}

			@Override
			public void execute(List<String> args){
				NetworkManager.getInstance().sendChat(StringUtils.untokenize(args, 1, -1, false));
			}
		};
		Main.getInstance().console.register(sayCmd);
		ConsoleCmd listenDefCmd = new ConsoleCmd("listendef", 0, "[SERVER] Start listening for client connections at " + NetworkManager.getInstance()
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
		Main.getInstance().console.register(listenDefCmd);
		ConsoleCmd stopCmd = new ConsoleCmd("stop", 0, "[SERVER] Stops the Server.", 0){

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
		Main.getInstance().console.register(stopCmd);
		ConsoleCmd disconnectCmd = new ConsoleCmd("disconnect", 0, "[CLIENT] Client disconnects from Server.", 0){

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
		Main.getInstance().console.register(disconnectCmd);
		ConsoleCmd devConnectCmd = new ConsoleCmd("dc", 0, "[CLIENT][DEV CMD] start without arguments for help", 1){

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
		Main.getInstance().console.register(devConnectCmd);
		ConsoleCmd sendDevPlayerUpdateCmd = new ConsoleCmd("spu", 0, "[CLIENT][DEV CMD] only for network tests, sendPlayerUpdate.", 1){

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
		Main.getInstance().console.register(sendDevPlayerUpdateCmd);
		ConsoleCmd sendMatchUpdateCmd = new ConsoleCmd("sendMatchUpdate", 0, "[CLIENT][DEBUG] Post a mapname.", 1){
			@Override
			public void showUsage(){
				showUsage("<mapname-text>");
			}

			@Override
			public void execute(List<String> args){
				NetworkManager.getInstance().sendMatchUpdate(StringUtils.untokenize(args, 1, -1, false));
			}
		};
		Main.getInstance().console.register(sendMatchUpdateCmd);
		ConsoleCmd sendPlayerUpdateCmd = new ConsoleCmd("sendPlayerUpdate", 0, "[CLIENT][DEBUG] Post playerdata.", 1){
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
		Main.getInstance().console.register(sendPlayerUpdateCmd);
		ConsoleCmd cheatCmd = new ConsoleCmd("cheatMode", 0, "", 0){

			@Override
			public void showUsage(){
				showUsage("");
			}

			@Override
			public void execute(List<String> args){
				isCheatMode = !isCheatMode;
				logger.info("cheats {}", isCheatMode);
			}
		};
		Main.getInstance().console.register(cheatCmd);
		ConsoleCmd ponyCmd = new ConsoleCmd("pony", 0, "", 0){

			@Override
			public void showUsage(){
				showUsage("");
			}

			@Override
			public void execute(List<String> args){
				char[] c = {10, 32, 95, 32, 95, 32, 95, 32, 95, 32, 95, 32, 13, 10, 124, 32, 95, 32, 92, 32, 124, 32, 95, 32, 92, 32, 124, 32, 124, 32, 124, 32, 124, 32, 124, 32, 124, 32, 13, 10, 124, 32, 124, 32, 92, 47, 32, 95, 32, 95, 32, 95, 32, 95, 32, 95, 32, 95, 124, 32, 124, 32, 124, 32, 124, 95, 32, 95, 124, 32, 124, 32, 124, 32, 124, 32, 95, 32, 95, 124, 32, 124, 32, 95, 13, 10, 124, 32, 124, 32, 95, 32, 47, 32, 95, 96, 32, 124, 32, 39, 95, 32, 96, 32, 95, 32, 92, 32, 47, 32, 95, 32, 92, 32, 124, 32, 124, 32, 47, 32, 95, 32, 92, 32, 92, 32, 47, 32, 47, 32, 124, 47, 92, 124, 32, 124, 47, 32, 95, 32, 92, 47, 32, 95, 32, 92, 32, 124, 47, 32, 47, 13, 10, 124, 32, 124, 95, 92, 32, 92, 32, 40, 95, 124, 32, 124, 32, 124, 32, 124, 32, 124, 32, 124, 32, 124, 32, 95, 47, 32, 124, 47, 32, 47, 32, 95, 47, 92, 32, 86, 32, 47, 92, 32, 47, 92, 32, 47, 32, 95, 47, 32, 95, 47, 32, 60, 32, 13, 10, 32, 92, 95, 47, 92, 95, 44, 95, 124, 95, 124, 32, 124, 95, 124, 32, 124, 95, 124, 92, 95, 124, 95, 47, 32, 92, 95, 124, 32, 92, 95, 47, 32, 92, 47, 32, 92, 47, 32, 92, 95, 124, 92, 95, 124, 95, 124, 92, 95, 92, 13, 10, 32, 13, 10, 32, 13, 10, 32, 95, 32, 95, 32, 95, 32, 95, 32, 95, 32, 95, 32, 95, 32, 95, 32, 95, 32, 13, 10, 32, 124, 32, 124, 32, 124, 32, 47, 32, 95, 124, 32, 47, 32, 95, 32, 92, 124, 32, 95, 32, 124, 47, 32, 124, 32, 124, 95, 32, 124, 32, 47, 32, 47, 32, 124, 32, 47, 32, 124, 32, 13, 10, 32, 124, 32, 124, 32, 124, 32, 92, 32, 96, 45, 32, 96, 39, 32, 47, 32, 47, 39, 124, 32, 124, 47, 39, 32, 124, 96, 124, 32, 124, 32, 47, 32, 47, 32, 47, 32, 47, 96, 124, 32, 124, 32, 47, 32, 47, 124, 32, 124, 32, 13, 10, 32, 124, 32, 124, 47, 92, 124, 32, 124, 96, 45, 32, 92, 32, 47, 32, 47, 32, 124, 32, 47, 124, 32, 124, 32, 124, 32, 124, 32, 92, 32, 92, 47, 32, 47, 32, 124, 32, 124, 47, 32, 47, 95, 124, 32, 124, 32, 13, 10, 32, 92, 32, 47, 92, 32, 47, 92, 95, 47, 32, 47, 32, 47, 32, 47, 95, 92, 32, 124, 95, 47, 32, 47, 95, 124, 32, 124, 95, 32, 95, 47, 32, 47, 32, 47, 32, 95, 124, 32, 124, 92, 95, 32, 124, 32, 13, 10, 32, 92, 47, 32, 92, 47, 92, 95, 47, 32, 92, 95, 47, 32, 92, 95, 47, 32, 92, 95, 47, 92, 95, 47, 95, 47, 32, 92, 95, 47, 32, 124, 95, 47, 32, 13, 10, 32, 13, 10, 32, 13, 10, 95, 32, 95, 32, 95, 32, 13, 10, 124, 32, 95, 32, 92, 32, 124, 32, 95, 32, 92, 32, 124, 32, 124, 32, 13, 10, 124, 32, 124, 95, 47, 32, 47, 95, 32, 95, 32, 95, 32, 95, 32, 95, 32, 95, 32, 95, 32, 95, 32, 95, 124, 32, 124, 95, 47, 32, 47, 95, 32, 95, 32, 95, 32, 95, 32, 95, 124, 32, 124, 32, 13, 10, 124, 32, 95, 32, 92, 32, 124, 32, 124, 32, 124, 32, 39, 95, 32, 92, 124, 32, 39, 95, 32, 92, 124, 32, 124, 32, 124, 32, 124, 95, 124, 32, 95, 32, 92, 32, 39, 95, 47, 32, 95, 96, 32, 92, 32, 92, 32, 47, 92, 32, 47, 32, 47, 32, 124, 32, 13, 10, 124, 32, 124, 95, 47, 32, 47, 32, 124, 95, 124, 32, 124, 32, 124, 32, 124, 32, 124, 32, 124, 32, 124, 32, 124, 32, 124, 95, 124, 32, 124, 32, 124, 32, 124, 95, 47, 32, 47, 32, 124, 32, 124, 32, 40, 95, 124, 32, 124, 92, 32, 86, 32, 86, 32, 47, 124, 32, 124, 32, 13, 10, 92, 95, 47, 32, 92, 95, 44, 95, 124, 95, 124, 32, 124, 95, 124, 95, 124, 32, 124, 95, 124, 92, 95, 44, 32, 124, 32, 92, 95, 47, 124, 95, 124, 32, 92, 95, 44, 95, 124, 32, 92, 95, 47, 92, 95, 47, 32, 124, 95, 124, 32, 13, 10, 32, 95, 47, 32, 124, 32, 13, 10, 32, 124, 95, 47, 32, 13, 10, 32, 13, 1};
				int[] i = {1, 1, 5, 21, 6, 11, 1, 4, 1, 11, 1, 4, 1, 1, 1, 2, 2, 1, 1, 20, 1, 2, 1, 2, 1, 9, 1, 1, 1, 2, 1, 1, 1, 9, 1, 1, 1, 3, 1, 1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 5, 3, 1, 1, 1, 1, 2, 1, 1, 1, 1, 3, 2, 3, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 3, 1, 1, 1, 1, 1, 1, 4, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 66, 1, 1, 66, 1, 1, 6, 1, 4, 1, 1, 5, 3, 5, 2, 5, 2, 2, 3, 5, 4, 4, 4, 3, 6, 1, 1, 5, 1, 1, 1, 2, 1, 1, 1, 2, 3, 1, 1, 1, 1, 2, 2, 1, 1, 2, 1, 2, 1, 1, 2, 1, 1, 1, 4, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 3, 1, 5, 1, 1, 5, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 1, 1, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 3, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 5, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 1, 1, 5, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 3, 2, 1, 5, 1, 1, 6, 1, 1, 2, 1, 1, 1, 4, 1, 2, 1, 5, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 3, 1, 3, 1, 3, 1, 1, 1, 5, 1, 1, 66, 1, 1, 66, 1, 1, 6, 31, 6, 20, 1, 2, 1, 1, 1, 1, 3, 1, 1, 30, 1, 1, 3, 1, 1, 18, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 2, 2, 1, 1, 2, 2, 1, 3, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 3, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 3, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 6, 1, 4, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 26, 2, 1, 1, 1, 35, 1, 1, 25, 1, 3, 1, 36, 1, 1, 66, 1, 10};
				StringBuilder s = new StringBuilder(c.length <= i.length?c.length:i.length);
				for(int x = 0;x < c.length;x++){
					for(int y = 0;y < i[x];y++){
						s.append(c[x]);
					}
				}
				logger.debug(s.toString());
			}
		};
		Main.getInstance().console.register(ponyCmd);
		ConsoleCmd quit_f = new ConsoleCmd("quit", CCmdFlags.SYSTEM, "Exit without questions."){

			@Override
			public void execute(List<String> args){
				Main.getInstance().dispose();
			}
		};
		Main.getInstance().console.register(quit_f);
	}

}
