package de.hochschuletrier.gdw.examples.netcode.pingpong;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.utils.QuietUtils;

/**
 *
 * @author Santo Pfingsten
 */
public class ClientTest {
	NetConnection connection;
	
	private void sendPing(String text) {
		connection.send(new ChatDatagram(text));
	}
	
	private void handleDatagrams() {
		if(connection == null || !connection.isConnected())
			return;
		
		connection.sendPendingDatagrams();
				
		ChatDatagram datagram = (ChatDatagram)connection.receive();
		if(datagram != null) {
			System.out.println(datagram.getText());
			sendPing("Ping");
		}
	}
	
	private void run() {
		try {
			connection = new NetConnection("localhost", 9090, new DatagramFactory());
			sendPing("Ping");
			for(;connection.isConnected();) {
				handleDatagrams();
                QuietUtils.sleep(16);
			}
			
			System.out.println("server disconnected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ClientTest test = new ClientTest();
		test.run();
	}
}
