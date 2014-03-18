package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;

public interface PlayerUpdateCallback {
	void callback(String playerName, EntityType type, byte team, boolean accept);
}
