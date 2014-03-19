package de.hochschuletrier.gdw.ws1314.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.ws1314.network.datagrams.*;

public class ServerDatagramHandler implements DatagramHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerDatagramHandler.class);

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
        logger.warn("Server received a PlayerReplicationDatagram, which is only intended to be sent to a client, something is wrong here...");
    }

    @Override
    public void handle (PlayerUpdateDatagram playerUpdateDatagram, NetConnection connection) {
        NetworkManager.getInstance().getPlayerUpdateCallback().callback(playerUpdateDatagram.getPlayerName(),playerUpdateDatagram.getEntityType(), playerUpdateDatagram.getTeam(), playerUpdateDatagram.isAccept());
    }

    @Override
    public void handle (LevelObjectReplicationDatagram levelObjectReplicationDatagram, NetConnection connection) {
        logger.warn("Server received a LevelObjectReplicationDatagram, which is only intended to be sent to a client, something is wrong here...");
    }

    @Override
    public void handle (ProjectileReplicationDatagram projectileReplicationDatagram, NetConnection connection) {
        logger.warn("Server received a ProjectileReplicationDatagram, which is only intended to be sent to a client, something is wrong here...");
    }

    @Override
    public void handle (ActionDatagram actionDatagram, NetConnection connection) {
        //TODO
    }

    @Override
    public void handle (EventDatagram eventDatagram, NetConnection connection) {
        logger.warn("Server received a EventDatagram, which is only intended to be sent to a client, something is wrong here...");
    }

    @Override
    public void handle (LobbyUpdateDatagram lobbyUpdateDatagram, NetConnection connection) {
        logger.warn("Server received a LobbyUpdateDatagram, which is only intended to be sent to a client, something is wrong here...");
    }

    @Override
    public void handle (MatchUpdateDatagram matchUpdateDatagram, NetConnection connection) {
        NetworkManager.getInstance().getMatchUpdateCallback().callback(matchUpdateDatagram.getMap());
    }

    @Override
    public void handle (DespawnDatagram despawnDatagram, NetConnection connection) {
        logger.warn("Server received a DespawnDatagram, which is only intended to be sent to a client, something is wrong here...");
    }

}
