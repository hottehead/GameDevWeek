package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.ws1314.network.datagrams.LobbyUpdateDatagram.PlayerData;

public interface LobbyUpdateCallback {
	public void callback(String map, PlayerData[] players);
}
