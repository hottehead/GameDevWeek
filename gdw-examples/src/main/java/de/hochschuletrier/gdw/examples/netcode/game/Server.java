package de.hochschuletrier.gdw.examples.netcode.game;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.NetReception;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Santo Pfingsten
 */
public class Server {
	public static NetReception reception;
	public static List<NetConnection> connections = new ArrayList<NetConnection>();
	
	public static boolean isRunning() {
		return reception != null && reception.isRunning();
	}
	
	public static void handleDatagrams(ServerGame.Entity player) {
		if(reception == null || !reception.isRunning())
			return;
		
		Iterator<NetConnection> it = connections.iterator();
		while (it.hasNext()) {
			NetConnection connection = it.next();
			connection.sendPendingDatagrams();
			
			// not interested in anything the client has to say, but clean up just in case.
			while(connection.hasIncoming())
				connection.receive();
			
			if (!connection.isConnected()) {
				System.out.println("client disconnected");
				it.remove();
				continue;
			}
		}

	}
	
	public static void getNewConnections() {
		if(isRunning()) {
			NetConnection connection = reception.getNextNewConnection();
			while (connection != null) {
				connection.setAccepted(true);
				connections.add(connection);
				connection = reception.getNextNewConnection();
			}
		}
	}
	
	public static void init(String ip, int port) {
		try {
			reception = new NetReception(ip, port, 10, new DatagramFactory());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
