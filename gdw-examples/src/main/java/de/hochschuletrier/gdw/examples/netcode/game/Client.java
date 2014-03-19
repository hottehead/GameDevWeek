package de.hochschuletrier.gdw.examples.netcode.game;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;

/**
 *
 * @author Santo Pfingsten
 */
public class Client {
	public static NetConnection connection;
	public static long lastMessage;

	public static boolean isRunning() {
		return connection != null && connection.isConnected();
	}
	
	public static void handleDatagrams(ClientGame.Entity player) {
		if(isRunning()) {
			connection.sendPendingDatagrams();
			while(connection.hasIncoming()) {
				PlayerDatagram datagram = (PlayerDatagram)connection.receive();
				if(datagram != null) {
					player.position.setLocation(datagram.getPosition());
					lastMessage = System.currentTimeMillis();
				}
			}
		}
	}
	
	public static void init(String ip, int port) {
		try {
			connection = new NetConnection(ip, port, new DatagramFactory());
		} catch (Exception e) {
			connection = null;
			e.printStackTrace();
		}
	}
	
	public static void disconnect() {
		if(connection == null)
			return;
		connection.shutdown();
		connection = null;
	}
}
