package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;

public interface PlayerUpdateCallback {
	void callback(String playerName, EntityType type, TeamColor team, boolean accept);
}
