package de.hochschuletrier.gdw.ws1314.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.ws1314.network.datagrams.*;

public class ClientGameDatagramHandler implements DatagramHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientGameDatagramHandler.class);

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
