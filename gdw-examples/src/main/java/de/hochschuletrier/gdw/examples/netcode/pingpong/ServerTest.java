package de.hochschuletrier.gdw.examples.netcode.pingpong;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.NetReception;
import de.hochschuletrier.gdw.commons.utils.QuietUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Santo Pfingsten
 */
public class ServerTest {
	private NetReception reception;
	private final List<NetConnection> connections = new ArrayList<NetConnection>();
	
	
	private void sendPing(NetConnection connection, String text) {
		connection.send(new ChatDatagram(text));
	}
	
	private void handleDatagrams() {
		Iterator<NetConnection> it = connections.iterator();
		while (it.hasNext()) {
			NetConnection connection = it.next();
			if(!connection.isConnected()) {
				System.out.println("client disconnected");
				it.remove();
				continue;
			}
			connection.sendPendingDatagrams();
				
			ChatDatagram datagram = (ChatDatagram)connection.receive();
			if (datagram != null) {
				System.out.println(datagram.getText());
				sendPing(connection, "Pong");
			}
		}

	}
	
	private void getNewConnections() {
		while(reception.hasNewConnections()) {
			NetConnection connection = reception.getNextNewConnection();
			connection.setAccepted(true);
			connections.add(connection);
		}
	}
	
	private void run() {
		try {
			reception = new NetReception("localhost", 9090, 1, new DatagramFactory());
			for (;reception.isRunning();) {
				getNewConnections();

				handleDatagrams();
                QuietUtils.sleep(16);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ServerTest test = new ServerTest();
		test.run();
	}
}
