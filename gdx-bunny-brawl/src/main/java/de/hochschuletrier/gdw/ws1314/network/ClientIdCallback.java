package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;

public interface ClientIdCallback {
	public void callback(PlayerData playerdata);
}
