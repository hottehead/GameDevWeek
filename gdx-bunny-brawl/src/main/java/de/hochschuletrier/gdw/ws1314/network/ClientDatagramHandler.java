package de.hochschuletrier.gdw.ws1314.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.ws1314.network.datagrams.*;

public class ClientDatagramHandler implements DatagramHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientDatagramHandler.class);

	@Override
	public void handle(ChatSendDatagram chatDatagram, NetConnection connection) {
		logger.warn("Client received a ChatSendDatagram, which is only intended to be sent to a server, something is wrong here...");
	}

	@Override
	public void handle(ChatDeliverDatagram chatDeliverDatagram,	NetConnection connection) {
		NetworkManager.getInstance().receiveChat(chatDeliverDatagram.getSender(), chatDeliverDatagram.getText());
	}

    @Override
    public void handle (PlayerReplicationDatagram playerReplicationDatagram, NetConnection connection) {
        //TODO
    }

    @Override
    public void handle (PlayerUpdateDatagram playerUpdateDatagram, NetConnection connection) {
        logger.warn ("Client received a PlayerUpdateDatagram, which is only intended to be sent to a server, something is wrong here...");
    }

    @Override
    public void handle (LevelObjectReplicationDatagram levelObjectReplicationDatagram, NetConnection connection) {
        //TODO
    }

    @Override
    public void handle (ProjectileReplicationDatagram projectileReplicationDatagram, NetConnection connection) {
        //TODO
    }

    @Override
    public void handle (ActionDatagram actionDatagram, NetConnection connection) {
        logger.warn ("Client received a ActionDatagram, which is only intended to be sent to a server, something is wrong here...");
    }

    @Override
    public void handle (EventDatagram eventDatagram, NetConnection connection) {
        //TODO
    }

    @Override
    public void handle (LobbyUpdateDatagram lobbyUpdateDatagram, NetConnection connection) {
        NetworkManager.getInstance().getLobbyUpdateCallback().callback(lobbyUpdateDatagram.getMap(), lobbyUpdateDatagram.getPlayers());
    }

    @Override
    public void handle (MatchUpdateDatagram matchUpdateDatagram, NetConnection connection) {
        logger.warn ("Client received a MatchUpdateDatagram, which is only intended to be sent to a server, something is wrong here...");
    }

    @Override
    public void handle (DespawnDatagram despawnDatagram, NetConnection connection) {
        //TODO
    }

    @Override
    public void handle (GameStateDatagram gameStateDatagram, NetConnection connection) {
        //TODO
    }
}
