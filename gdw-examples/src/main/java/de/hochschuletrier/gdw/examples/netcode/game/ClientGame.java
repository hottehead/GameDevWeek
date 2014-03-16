package de.hochschuletrier.gdw.examples.netcode.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Santo Pfingsten
 */
public class ClientGame  extends BaseGame {
	Entity player = new Entity();
	
	public static class Entity {
		Point position = new Point();
	}
	
	public ClientGame() {
		super("NetPack Game Example");
		
		Client.init("localhost", 9090);
	}

	@Override
    public void paint(Graphics g) {
		Client.handleDatagrams(player);
		
		setBackground(Color.black);
		g.clearRect(0, 0, 640, 480);
		
		// render the circle red when updates have been received, white otherwise (messages have been deltified away by the server)
		if((System.currentTimeMillis() - Client.lastMessage) < 50)
			g.setColor(Color.red);
		else
			g.setColor(Color.white);
		g.drawOval(player.position.x, player.position.y, 50, 50);
	}

	public static void main(String[] argv) {
		ClientGame game = new ClientGame();
		game.start();
	}
}
