package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.ws1314.network.datagrams.*;

public class ClientLobbyDatagramHandler implements DatagramHandler {

	@Override
	public void handle(ChatSendDatagram chatDatagram, NetConnection connection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handle(ChatDeliverDatagram chatDeliverDatagram,
			NetConnection connection) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void handle (PlayerReplicationDatagram playerReplicationDatagram, NetConnection connection) {

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
    	NetworkManager.getInstance().getLobbyUpdateCallback().callback(lobbyUpdateDatagram.getMap(), lobbyUpdateDatagram.getPlayers());

    }

    @Override
    public void handle (MatchUpdateDatagram matchUpdateDatagram, NetConnection connection) {
    	NetworkManager.getInstance().getMatchUpdateCallback().callback(matchUpdateDatagram.getMap());
    }
    
    @Override
    public void handle (PlayerUpdateDatagram playerUpdateDatagram, NetConnection connection) {
    	NetworkManager.getInstance().getPlayerUpdateCallback().callback(playerUpdateDatagram.getPlayerName(),playerUpdateDatagram.getEntityType(), playerUpdateDatagram.getTeam(), playerUpdateDatagram.isAccept());
    }

    @Override
    public void handle (DespawnDatagram despawnDatagram, NetConnection connection) {

    }

}
