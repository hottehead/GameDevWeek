package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.network.datagrams.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerDatagramHandler implements DatagramHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerDatagramHandler.class);

    @Override
    public void handle(ChatSendDatagram chatDatagram, NetConnection connection) {
        String sender = ((ConnectionAttachment) connection.getAttachment()).getPlayername();
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
    public void handle(PlayerReplicationDatagram playerReplicationDatagram, NetConnection connection) {
        logger.warn("Server received a PlayerReplicationDatagram, which is only intended to be sent to a client, something is wrong here...");
    }

    @Override
    public void handle(PlayerUpdateDatagram playerUpdateDatagram, NetConnection connection) {
    	//connection.setAttachment(new PlayerData(playerUpdateDatagram.getPlayerName(), playerUpdateDatagram.getEntityType(), playerUpdateDatagram.getTeam(), playerUpdateDatagram.isAccept()));
    	int playerid = ((ConnectionAttachment) connection.getAttachment()).getId();
    	NetworkManager.getInstance().getPlayerUpdateCallback().callback(playerid, playerUpdateDatagram.getPlayerName(), playerUpdateDatagram.getEntityType(), playerUpdateDatagram.getTeam(), playerUpdateDatagram.isAccept());
    }

    @Override
    public void handle(LevelObjectReplicationDatagram levelObjectReplicationDatagram, NetConnection connection) {
        logger.warn("Server received a LevelObjectReplicationDatagram, which is only intended to be sent to a client, something is wrong here...");
    }

    @Override
    public void handle(ProjectileReplicationDatagram projectileReplicationDatagram, NetConnection connection) {
        logger.warn("Server received a ProjectileReplicationDatagram, which is only intended to be sent to a client, something is wrong here...");
    }

    @Override
    public void handle(ActionDatagram actionDatagram, NetConnection connection) {
    	long entityid = ((ConnectionAttachment) connection.getAttachment()).getEntityId();
    	ServerEntity tmp = ServerEntityManager.getInstance().getEntityById(entityid);
    	if(tmp==null){
    		logger.warn("Recieved ActionDatagram for entity with id {} but the entity does not exist.");
    		return;
    	}
    	if(tmp instanceof ServerPlayer){
    		((ServerPlayer) tmp).doAction(actionDatagram.getPlayerAction());
    	} else {
    		logger.warn("Server handle ActionDatagram: entityid of connection is no ServerPlayer");
    	}
    }

    @Override
    public void handle(EventDatagram eventDatagram, NetConnection connection) {
        logger.warn("Server received a EventDatagram, which is only intended to be sent to a client, something is wrong here...");
    }

    @Override
    public void handle(LobbyUpdateDatagram lobbyUpdateDatagram, NetConnection connection) {
        logger.warn("Server received a LobbyUpdateDatagram, which is only intended to be sent to a client, something is wrong here...");
    }

    @Override
    public void handle(MatchUpdateDatagram matchUpdateDatagram, NetConnection connection) {
    	//connection.setAttachment(matchUpdateDatagram);
    	NetworkManager.getInstance().getMatchUpdateCallback().callback(matchUpdateDatagram.getMap());
    }

    @Override
    public void handle(DespawnDatagram despawnDatagram, NetConnection connection) {
        logger.warn("Server received a DespawnDatagram, which is only intended to be sent to a client, something is wrong here...");
    }

    @Override
    public void handle(GameStateDatagram gameStateDatagram, NetConnection connection) {
        logger.warn("Server received a GameStateDatagram, which is only intended to be sent to a client, something is wrong here...");
    }

	@Override
	public void handle(ClientIdDatagram clientIdDatagram,
			NetConnection connection) {
		logger.warn("Server received a ClientIdDatagram, which is only intended to be sent to a client, something is wrong here...");
	}
}
