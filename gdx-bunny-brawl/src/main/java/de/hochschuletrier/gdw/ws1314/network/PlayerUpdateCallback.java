package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;

public interface PlayerUpdateCallback{

	/**
	 * Serverseitig: der Client teilt seine Spielerdaten mit(Spielername, Klasse, Team und Accept)
	 */
	void callback(int playerid, String playerName, EntityType type, TeamColor team, boolean accept);
}
