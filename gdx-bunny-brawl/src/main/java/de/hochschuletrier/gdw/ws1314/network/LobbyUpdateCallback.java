package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;

public interface LobbyUpdateCallback{
	/**
	 * LobbyUpdateCallback: Clientseitig, dem Clienten wird die neue Spielerliste und Map zugeteilt
	 */
	public void callback(String map, PlayerData[] players);
}
