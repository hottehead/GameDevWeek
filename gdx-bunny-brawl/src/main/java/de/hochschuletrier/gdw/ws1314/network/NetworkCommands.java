package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.commons.devcon.CCmdFlags;
import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.utils.StringUtils;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.states.GameStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NetworkCommands{
	private static final Logger logger = LoggerFactory.getLogger(NetworkCommands.class);
	private boolean isCheatMode = false;

	public NetworkCommands(){
		ConsoleCmd connectCmd = new ConsoleCmd("connect", 0, "[CLIENT] Connect to a server.", 0){

			@Override
			public void showUsage(){
				showUsage("[ip] [port] uses default if not set (port 1024 - 65535 allowed)");
			}

			@Override
			public void execute(List<String> args){
				try{
					String ip = NetworkManager.getInstance().getMyIp();
					if(args.size() > 1){
						ip = args.get(1);
					}
					int port = NetworkManager.getInstance().getDefaultPort();
					if(args.size() > 2){
						port = Integer.parseInt(args.get(2));
					}
					NetworkManager.getInstance().connect(ip, port);

					logger.info("Changing State to Server-Lobby...");
					GameStates.CLIENTLOBBY.init(Main.getInstance().getAssetManager());
					GameStates.CLIENTLOBBY.activate();
				}
				catch (NumberFormatException e){
					showUsage();
				}
			}
		};
		Main.getInstance().console.register(connectCmd);

		ConsoleCmd listenCmd = new ConsoleCmd("server", 0, "[SERVER] Start listening for client connections. (Become a server.)", 0){

			@Override
			public void showUsage(){
				showUsage("[mapName] [port] [max-connections = 10] [interface-ip] uses default if not set (port 1024 - 65535 allowed)");
			}

			@Override
			public void execute(List<String> args){
				NetworkManager.getInstance().serverStartCommand(args.toArray (new String[args.size()]));
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

		ConsoleCmd ponyCmd = new ConsoleCmd("pony", 0, "", 0){

			@Override
			public void showUsage(){
				showUsage("");
			}

			@Override
			public void execute(List<String> args){
				logger.info("you found an easter egg ;) - watch in your console");
				char[] c = {10,0,9604,13,10,32,9608,9619,9608,9604,13,10,32,9608,9619,9608,13,10,32,9608,9619,9608,13,10,32,9608,9619,9608,13,10,32,9608,9619,
						9608,9619,9608,13,10,32,9608,9619,9608,9604,32,9604,13,10,32,9608,9619,9608,9619,9608,32,9608,13,10,32,9608,9619,9608,32,9604,9608,13,10,32,9608,9619,9608,32,9608,13,10,32,9608,9619,9608,9619,9608,32,9604,32,9604,9608,13,10,32,9608,9619,9608,32,9608,9604,32,9604,9608,13,10,32,9608,9619,9608,9619,9608,9604,9608,13,10,32,9608,9619,9608,9619,9608,9619,13,10,32,9608,9619,9608,9619,9608,9618,9619,32,9604,32,13,10,32,9619,9608,9619,9608,9619,9608,9618,9619,32,9604,9608,9619,9608,9604,13,10,32,9619,9618,9608,9619,9608,9619,9608,9619,9608,9618,9619,9618,9619,9608,9619,9608,13,10,32,9619,9618,9608,9619,9608,9619,9618,9619,9618,9608,9619,9608,13,10,32,9619,9618,9608,9619,9608,9619,9618,9608,9619,9608,9619,9608,13,10,32,9619,9618,9608,9619,9618,9619,9608,9619,9608,13,10,32,9619,9618,9619,9618,9619,9608,9619,9608,13,10,32,9619,9618,9619,9618,9619,9608,9619,9608,13,10,32,9619,9618,9619,9608,13,10,32,9619,9618,9619,9608,9619,9608,9619,13,10,32,9619,9618,9619,9618,9619,9608,9619,9608,9618,9619,13,10,32,9619,9618,9619,9618,9608,9618,9619,9608,9619,9618,9619,13,10,32,9619,9618,9619,9618,9608,9619,9608,9619,9618,9619,32,13,10,32,9619,9618,9619,9618,9608,9600,32,9600,9608,9619,9618,9619,32,13,10,32,9619,9618,9619,9618,9608,9619,32,9616,9619,9618,9619,32,13,10,32,9619,9618,9619,9608,9619,32,9619,32,9616,9619,9618,9619,32,13,10,32,9619,9618,9619,9618,9619,9608,32,9619,32,9619,9618,9619,32,13,10,32,9619,9618,9619,9618,9619,9608,9612,32,9619,32,9619,9618,9619,32,13,10,32,9619,9618,9608,9619,9608,9600,32,9616,32,9619,32,9619,9618,9619,32,13,10,32,9619,9618,9608,9619,32,9608,9612,32,9608,9604,32,9618,32,9619,9618,9619,32,13,10,32,9619,9618,9608,9619,32,9608,9618,32,9619,9618,9619,9618,9619,32,13,10,32,9619,9618,9608,9619,32,9608,9618,32,9619,9618,9619,9618,9619,32,13,10,32,9619,9618,9608,9619,32,9619,9608,9619,32,9619,9608,9618,9619,9618,9619,32,13,10,32,9619,9618,9608,9619,9608,9619,9608,9619,9618,9619,9618,9619,32,13,10,32,9619,9618,9608,9619,9608,9619,9618,9619,9618,9619,32,13,10,32,9619,9618,9608,9619,9608,9619,9608,9619,9618,9619,9618,9619,32,13,10,32,9619,9618,9608,9619,9608,9618,9608,9619,9618,9619,9618,9619,32,13,10,32,9619,9618,9608,9618,9608,9619,9618,9619,9618,9619,32,13,10,32,9619,9618,9608,9619,9618,9619,9618,9619,13,10,32,9619,9618,9608,9619,9618,9619,9618,9619,13,10,32,9619,9618,9619,9618,9619,9608,9619,9618,9619,9618,9619,13,10,32,9619,9618,9619,9618,9619,32,9608,9619,9618,9619,9618,9619,13,10,32,9619,32,9619,9618,9619,9618,9619,9618,9619,32,9608,9619,9618,9619,9618,9619,13,10,32,9619,9618,9619,9618,9619,9618,9619,32,9619,9618,9619,9618,9619,13,10,32,9619,9618,9619,9618,9619,32,9619,9618,9619,9618,9619,13,10,32,9619,9618,9619,9618,9619,32,9619,9618,9619,9618,9619,13,10,32,9619,9618,9619,32,9619,9618,9619,9618,9619,13,10,32,9619,32,9619,9618,9619,9618,9619,32,13,10,32,9619,9618,9619,9618,9619,32,13,10,32,9619,9618,9619,32,9619,32,13,10,32,9619,9618,9619,32,9619,32,13,10,32,9619,9618,9619,32,9619,9618,9619,32,13,10,32,9619,9618,9619,32,9619,9618,9619,32,13,10,32,9619,9618,9619,9618,9619,32,13,10,32,9619,9618,9619,32,13,10,32,9619,9618,9619,32,13,10,32,9619,1};
				int[] i = {1,15,2,1,1,14,1,1,1,1,1,1,15,1,2,1,1,1,16,1,1,2,1,1,17,1,2,2,1,1,18,1,1,1,1,1,1,1,19,1,3,1,1,22,1,1,1,20,1,1,1,2,1,20,2,1,1,21,1,4,
						1,17,1,3,1,1,22,1,4,2,14,4,1,1,23,1,2,1,2,1,6,1,4,1,4,1,1,24,2,4,2,3,2,1,1,1,5,1,1,25,1,4,1,1,1,1,10,1,1,26,1,2,1,4,10,3,1,1,27,2,4,1,2,9,2,5,10,2,1,1,1,26,1,1,5,1,3,13,2,4,2,1,1,2,1,1,1,1,22,3,2,1,3,1,3,1,2,4,7,1,5,1,1,5,1,1,1,18,3,6,3,3,1,3,12,3,2,1,7,1,1,1,16,2,9,1,3,1,1,19,1,6,1,2,1,1,1,14,2,11,1,2,20,9,1,2,1,1,1,12,2,12,2,19,11,1,2,1,1,1,10,2,11,3,17,14,1,2,1,1,1,9,2,28,21,1,1,1,8,2,14,27,1,7,1,1,1,1,7,2,13,20,5,3,1,8,1,1,1,1,1,6,2,13,18,9,1,1,3,1,6,1,1,1,1,5,2,13,16,4,12,4,1,2,2,1,1,1,1,5,2,13,15,2,3,2,8,1,6,3,3,1,1,1,1,4,2,14,13,1,2,4,11,1,8,4,1,1,1,1,4,2,15,12,2,2,3,2,9,1,8,4,2,1,1,1,4,2,11,1,4,10,4,5,2,9,8,4,2,1,1,1,5,2,11,3,3,8,4,1,5,2,7,8,5,2,1,1,1,5,2,11,1,13,2,1,1,1,6,1,7,8,5,2,1,1,1,6,2,9,1,13,1,2,1,2,1,1,3,2,6,8,6,2,1,1,1,7,2,7,2,13,2,7,3,4,10,5,1,1,1,1,1,1,8,2,4,2,15,3,5,3,3,12,5,1,1,1,1,1,1,9,2,3,1,16,4,1,2,3,2,10,2,7,1,1,1,1,1,1,10,2,3,1,4,1,29,2,2,6,1,2,1,1,1,1,11,2,3,1,31,2,4,6,1,2,1,1,1,1,12,2,3,3,3,1,19,5,5,7,1,2,1,1,1,1,13,2,5,3,5,14,2,1,7,7,1,3,1,1,1,1,14,2,7,5,17,1,5,7,1,3,1,1,1,1,15,2,28,1,4,8,1,3,1,1,1,15,2,28,1,3,8,1,3,1,1,1,15,2,4,1,22,1,1,2,8,1,3,1,1,1,15,2,4,1,21,1,1,1,2,7,1,4,1,1,1,11,1,2,1,5,1,12,1,7,2,2,1,1,7,1,4,1,1,1,12,2,4,1,13,1,7,1,5,2,6,1,4,1,1,1,14,5,12,1,6,2,7,1,7,1,4,1,1,1,12,4,13,2,4,3,9,1,6,1,5,1,1,1,17,6,5,9,13,1,5,1,4,1,1,1,24,6,25,2,3,1,4,1,1,1,1,59,2,2,1,4,1,1,1,1,62,2,5,1,10,1,1,1,1,64,1,5,1,10,2,1,1,1,64,1,5,1,9,1,1,1,1,1,1,65,2,4,2,5,1,3,1,1,1,1,67,1,6,4,3,1,1,1,1,68,1,12,1,1,1,1,69,2,9,1,1,1,1,72,9,10};
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

		ConsoleCmd quit_f = new ConsoleCmd("quit", CCmdFlags.SYSTEM, "Exit the game."){

			@Override
			public void execute(List<String> args){
				Main.getInstance().dispose();
			}
		};
		Main.getInstance().console.register(quit_f);
	}

}
