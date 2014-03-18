package de.hochschuletrier.gdw.ws1314.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.ws1314.network.datagrams.*;

public class ServerGameDatagramHandler implements DatagramHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerGameDatagramHandler.class);

	@Override
	public void handle(ChatSendDatagram chatDatagram, NetConnection connection) {
		String sender = (String) connection.getAttachment();
		NetworkManager.getInstance().broadcastToClients(
				new ChatDeliverDatagram(sender, 
						chatDatagram.getText()));
		NetworkManager.getInstance().receiveChat(sender, chatDatagram.getText());
	}

	@Override
	public void handle(ChatDeliverDatagram chatDeliverDatagram, NetConnection connection) {
		logger.warn("Server received a ChatDeliverDatagram, which is only intended to be sent to a client, something is wrong here...");
	}

    @Override
    public void handle (PlayerReplicationDatagram playerReplicationDatagram, NetConnection connection) {

    }

    @Override
    public void handle (PlayerUpdateDatagram playerUpdateDatagram, NetConnection connection) {

    }

    @Override
    public void handle (LevelObjectReplicationDatagram levelObjectReplicationDatagram, NetConnection connection) {

    }

    @Override
    public void handle (ProjectileReplicationDatagram projectileReplicationDatagram, NetConnection connection) {

    }

    @Override
    public void handle (ActionDatagram actionDatagram, NetConnection connection) {

    }

    @Override
    public void handle (EventDatagram eventDatagram, NetConnection connection) {

    }

    @Override
    public void handle (LobbyUpdateDatagram lobbyUpdateDatagram, NetConnection connection) {

    }

    @Override
    public void handle (MatchUpdateDatagram matchUpdateDatagram, NetConnection connection) {

    }

    @Override
    public void handle (DespawnDatagram despawnDatagram, NetConnection connection) {

    }

}
