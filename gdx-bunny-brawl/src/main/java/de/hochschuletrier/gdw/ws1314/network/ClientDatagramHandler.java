package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.network.datagrams.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;

public class ClientDatagramHandler implements DatagramHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientDatagramHandler.class);

    @Override
    public void handle(ChatSendDatagram chatDatagram, NetConnection connection) {
        logger.warn("Client received a ChatSendDatagram, which is only intended to be sent to a server, something is wrong here...");
    }

    @Override
    public void handle(ChatDeliverDatagram chatDeliverDatagram, NetConnection connection) {
        NetworkManager.getInstance().receiveChat(chatDeliverDatagram.getSender(), chatDeliverDatagram.getText());
    }

    @Override
    public void handle(PlayerReplicationDatagram playerReplicationDatagram, NetConnection connection) {
        ClientEntity entity = ClientEntityManager.getInstance().getEntityById(playerReplicationDatagram.getEntityId());
        if (entity==null){
        	logger.debug("Spawning player entity {}.",playerReplicationDatagram.getEntityId());
        	entity=ClientEntityManager.getInstance().createEntity(playerReplicationDatagram.getEntityId(),
        			new Vector2(playerReplicationDatagram.getXposition(),playerReplicationDatagram.getYposition()),
        			playerReplicationDatagram.getEntityType());
        }
        if(!(entity instanceof ClientPlayer)){
        	logger.warn("Received PlayerReplicationDatagram for entity {} which is no player entity, something is really wrong here ...",playerReplicationDatagram.getEntityId());
        }
        ClientPlayer player = (ClientPlayer) entity;
        player.setPosition(new Vector2(playerReplicationDatagram.getXposition(), playerReplicationDatagram.getYposition()));
        player.setEggCount(playerReplicationDatagram.getEggs());
        player.setCurrentHealth(playerReplicationDatagram.getHealth());
        player.setCurrentArmor(playerReplicationDatagram.getArmor());
        player.setFacingDirection(playerReplicationDatagram.getFacingDirection());
        player.setTeamColor(playerReplicationDatagram.getTeamColor());

    }

    @Override
    public void handle(PlayerUpdateDatagram playerUpdateDatagram, NetConnection connection) {
        logger.warn("Client received a PlayerUpdateDatagram, which is only intended to be sent to a server, something is wrong here...");
    }

    @Override
    public void handle(LevelObjectReplicationDatagram levelObjectReplicationDatagram, NetConnection connection) {
        //TODO
    }

    @Override
    public void handle(ProjectileReplicationDatagram projectileReplicationDatagram, NetConnection connection) {
        //TODO
    }

    @Override
    public void handle(ActionDatagram actionDatagram, NetConnection connection) {
        logger.warn("Client received a ActionDatagram, which is only intended to be sent to a server, something is wrong here...");
    }

    @Override
    public void handle(EventDatagram eventDatagram, NetConnection connection) {
        NetworkManager.getInstance().getEventCallback().callback(eventDatagram.getId(), eventDatagram.getEventType());
    }

    @Override
    public void handle(LobbyUpdateDatagram lobbyUpdateDatagram, NetConnection connection) {
        NetworkManager.getInstance().getLobbyUpdateCallback().callback(lobbyUpdateDatagram.getMap(), lobbyUpdateDatagram.getPlayers());
    }

    @Override
    public void handle(MatchUpdateDatagram matchUpdateDatagram, NetConnection connection) {
        logger.warn("Client received a MatchUpdateDatagram, which is only intended to be sent to a server, something is wrong here...");
    }

    @Override
    public void handle(DespawnDatagram despawnDatagram, NetConnection connection) {
        NetworkManager.getInstance().getDespawnCallback().callback(despawnDatagram.getId());
    }

    @Override
    public void handle(GameStateDatagram gameStateDatagram, NetConnection connection) {
        NetworkManager.getInstance().getGameStateCallback().callback(gameStateDatagram.getGameStates());
    }
}
