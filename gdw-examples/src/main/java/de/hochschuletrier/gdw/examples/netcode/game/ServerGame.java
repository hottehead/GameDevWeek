package de.hochschuletrier.gdw.examples.netcode.game;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;

/**
 *
 * @author Santo Pfingsten
 */
public class ServerGame extends BaseGame {
	Entity player = new Entity();
	
	public static class Entity {
		Point position = new Point();
	}
	
	public ServerGame() {
		super("NetPack Game Example");
		
		Server.init("localhost", 9090);
	}

	@Override
    public void paint(Graphics g) {
		updateNetwork();
		
		setBackground(Color.black);
		g.clearRect(0, 0, 640, 480);
		g.setColor(Color.white);
		g.drawOval(player.position.x, player.position.y, 50, 50);
	}

	public void updateNetwork() {
		Server.getNewConnections();
		Server.handleDatagrams(player);

		PlayerDatagram datagram = new PlayerDatagram(player.position);
		for(NetConnection c: Server.connections)
			c.send(datagram);
	}
	
	@Override
    public void keyPressed(KeyEvent e) {
		// move the player a bit
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_W)
			player.position.y -= 5;
		if(keyCode == KeyEvent.VK_A)
			player.position.x -= 5;
		if(keyCode == KeyEvent.VK_S)
			player.position.y += 5;
		if(keyCode == KeyEvent.VK_D)
			player.position.x += 5;
	}
	
	public static void main(String[] argv) {
		ServerGame game = new ServerGame();
		game.start();
	}
}
