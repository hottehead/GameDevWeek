package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientLevelObject;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ClientProjectile;
import de.hochschuletrier.gdw.ws1314.network.datagrams.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;

public class ClientDatagramHandler implements DatagramHandler{
	private static final Logger logger = LoggerFactory.getLogger(ClientDatagramHandler.class);

	@Override
	public void handle(ChatSendDatagram chatDatagram, NetConnection connection){
		logger.warn("Client received a ChatSendDatagram, which is only intended to be sent to a server, something is wrong here...");
	}

	@Override
	public void handle(ChatDeliverDatagram chatDeliverDatagram, NetConnection connection){
		NetworkManager.getInstance().receiveChat(chatDeliverDatagram.getSender(), chatDeliverDatagram.getText());
	}

	@Override
	public void handle(PlayerReplicationDatagram playerReplicationDatagram, NetConnection connection){
		if(ClientEntityManager.getInstance().isPendingSpawn(playerReplicationDatagram.getEntityId())) return;
		ClientEntity entity = ClientEntityManager.getInstance().getEntityById(playerReplicationDatagram.getEntityId());
		if(entity == null){
			logger.debug("Spawning player entity {} of type {}.", playerReplicationDatagram.getEntityId(), playerReplicationDatagram.getEntityType().name());
			entity = ClientEntityManager.getInstance().createEntity(playerReplicationDatagram.getEntityId(),
					new Vector2(playerReplicationDatagram.getXposition(), playerReplicationDatagram.getYposition()), playerReplicationDatagram.getEntityType());
			if(entity == null){
				logger.warn("Couldn't spawn entity of type {}.", playerReplicationDatagram.getEntityType().name());
				return;
			}
		}
		if(!(entity instanceof ClientPlayer)){
			logger.warn("Received PlayerReplicationDatagram for entity {} which is no player entity, something is really wrong here ...",
					playerReplicationDatagram.getEntityId());
			return;
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
	public void handle(PlayerUpdateDatagram playerUpdateDatagram, NetConnection connection){
		logger.warn("Client received a PlayerUpdateDatagram, which is only intended to be sent to a server, something is wrong here...");
	}

	@Override
	public void handle(LevelObjectReplicationDatagram levelObjectReplicationDatagram, NetConnection connection){
		if(ClientEntityManager.getInstance().isPendingSpawn(levelObjectReplicationDatagram.getEntityId())) return;
		ClientEntity entity = ClientEntityManager.getInstance().getEntityById(levelObjectReplicationDatagram.getEntityId());
		if(entity == null){
			logger.debug("Spawning level-object entity {}.", levelObjectReplicationDatagram.getEntityId());
			entity = ClientEntityManager.getInstance().createEntity(levelObjectReplicationDatagram.getEntityId(),
					new Vector2(levelObjectReplicationDatagram.getXposition(), levelObjectReplicationDatagram.getYposition()),
					levelObjectReplicationDatagram.getEntityType());
			if(entity == null){
				logger.warn("Couldn't spawn entity of type {}.", levelObjectReplicationDatagram.getEntityType());
				return;
			}
		}
		if(!(entity instanceof ClientLevelObject)){
			logger.warn("Received LevelObjectReplicationDatagram for entity {} which is no level-object entity, something is really wrong here ...",
					levelObjectReplicationDatagram.getEntityId());
			return;
		}
		ClientLevelObject levelObject = (ClientLevelObject) entity;
		levelObject.setPosition(new Vector2(levelObjectReplicationDatagram.getXposition(), levelObjectReplicationDatagram.getYposition()));
		if(levelObjectReplicationDatagram.getVisibility()){
			levelObject.enable();
		}
		else{
			levelObject.disable();
		}
	}

	@Override
	public void handle(ProjectileReplicationDatagram projectileReplicationDatagram, NetConnection connection){
		if(ClientEntityManager.getInstance().isPendingSpawn(projectileReplicationDatagram.getEntityId())) return;
		ClientEntity entity = ClientEntityManager.getInstance().getEntityById(projectileReplicationDatagram.getEntityId());
		if(entity == null){
			logger.debug("Spawning projectile entity {}.", projectileReplicationDatagram.getEntityId());
			entity = ClientEntityManager.getInstance().createEntity(projectileReplicationDatagram.getEntityId(),
					new Vector2(projectileReplicationDatagram.getXposition(), projectileReplicationDatagram.getYposition()), EntityType.Projectil);
			if(entity == null){
				logger.warn("Couldn't spawn entity of type {}.", EntityType.Projectil);
				return;
			}
		}
		if(!(entity instanceof ClientProjectile)){
			logger.warn("Received ProjectileReplicationDatagram for entity {} which is no projectile entity, something is really wrong here ...",
					projectileReplicationDatagram.getEntityId());
			return;
		}
		ClientProjectile projectile = (ClientProjectile) entity;
		projectile.setPosition(new Vector2(projectileReplicationDatagram.getXposition(), projectileReplicationDatagram.getYposition()));
		projectile.setFacingDirection(projectileReplicationDatagram.getDirection());
		projectile.setTeamColor(projectileReplicationDatagram.getTeamColor());
	}

	@Override
	public void handle(ActionDatagram actionDatagram, NetConnection connection){
		logger.warn("Client received a ActionDatagram, which is only intended to be sent to a server, something is wrong here...");
	}

	@Override
	public void handle(EventDatagram eventDatagram, NetConnection connection){
		ClientEntity entity = ClientEntityManager.getInstance().getEntityById(eventDatagram.getEntityId());
		if(entity == null){
			logger.warn("Received EventDatagram for entity {} but the entity does not exist.", eventDatagram.getEntityId());
			return;
		}
		entity.doEvent(eventDatagram.getEventType());
	}

	@Override
	public void handle(LobbyUpdateDatagram lobbyUpdateDatagram, NetConnection connection){
		NetworkManager.getInstance().getLobbyUpdateCallback().callback(lobbyUpdateDatagram.getMap(), lobbyUpdateDatagram.getPlayers());
	}

	@Override
	public void handle(MatchUpdateDatagram matchUpdateDatagram, NetConnection connection){
		logger.warn("Client received a MatchUpdateDatagram, which is only intended to be sent to a server, something is wrong here...");
	}

	@Override
	public void handle(DespawnDatagram despawnDatagram, NetConnection connection){
		ClientEntity entity = ClientEntityManager.getInstance().getEntityById(despawnDatagram.getEntityId());
		if(entity == null){
			logger.warn("Received DespawnDatagram for already non-existent entity {}.", despawnDatagram.getEntityId());
			return;
		}
		ClientEntityManager.getInstance().removeEntity(entity);
	}

	@Override
	public void handle(GameStateDatagram gameStateDatagram, NetConnection connection){
		NetworkManager.getInstance().getGameStateCallback().callback(gameStateDatagram.getGameStates());
	}

	@Override
	public void handle(ClientIdDatagram clientIdDatagram, NetConnection connection){
		NetworkManager.getInstance().getClientIdCallback().callback(clientIdDatagram.getPlayerId());
	}

	@Override
	public void handle(EntityIDDatagram entityIDDatagram, NetConnection connection){
		// TODO Auto-generated method stub
		ClientEntityManager.getInstance().setPlayerEntityID(entityIDDatagram.getEntityId());
	}
}
