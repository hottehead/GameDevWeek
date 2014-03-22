package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagramFactory;
import de.hochschuletrier.gdw.ws1314.network.datagrams.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatagramFactory implements INetDatagramFactory{
	private static final Logger logger = LoggerFactory.getLogger(DatagramFactory.class);

	@Override
	public INetDatagram createDatagram(byte type, short id, short param1, short param2){
		switch (type){
		case ActionDatagram.ACTION_DATAGRAM:
			return new ActionDatagram(type, id, param1, param2);
		case ChatSendDatagram.CHAT_SEND_DATAGRAM:
			return new ChatSendDatagram(type, id, param1, param2);
		case ChatDeliverDatagram.CHAT_DELIVER_DATAGRAM:
			return new ChatDeliverDatagram(type, id, param1, param2);
		case DespawnDatagram.DESPAWN_DATAGRAM:
			return new DespawnDatagram(type, id, param1, param2);
		case EventDatagram.EVENT_DATAGRAM:
			return new EventDatagram(type, id, param1, param2);
		case LevelObjectReplicationDatagram.LEVEL_OBJECT_REPLICATION_DATAGRAM:
			return new LevelObjectReplicationDatagram(type, id, param1, param2);
		case LobbyUpdateDatagram.LOBBY_UPDATE_DATAGRAM:
			return new LobbyUpdateDatagram(type, id, param1, param2);
		case MatchUpdateDatagram.MATCH_UPDATE_DATAGRAM:
			return new MatchUpdateDatagram(type, id, param1, param2);
		case PlayerReplicationDatagram.PLAYER_REPLICATION_DATAGRAM:
			return new PlayerReplicationDatagram(type, id, param1, param2);
		case PlayerUpdateDatagram.PLAYER_UPDATE_DATAGRAM:
			return new PlayerUpdateDatagram(type, id, param1, param2);
		case ProjectileReplicationDatagram.PROJETILE_REPLICATION_DATAGRAM:
			return new ProjectileReplicationDatagram(type, id, param1, param2);
		case GameStateDatagram.GAME_STATE_DATAGRAM:
			return new GameStateDatagram(type, id, param1, param2);
		case ClientIdDatagram.CLIENT_ID_DATAGRAM:
			return new ClientIdDatagram(type, id, param1, param2);
		case EntityIDDatagram.ENTITY_ID_DATAGRAM:
			return new EntityIDDatagram(type, id, param1, param2);
		case PingDatagram.PING_DATAGRAM:
			return new PingDatagram(type, id, param1, param2);
		case GameInfoReplicationDatagram.GAME_INFO_REPLICATION_DATAGRAM:
			return new GameInfoReplicationDatagram(type, id, param1, param2);
		default:
			logger.warn("Received datagram with unknown type {}", type);
			throw new IllegalArgumentException("Received datagram with unknown type: " + type);
		}
	}
}
