package de.hochschuletrier.gdw.ws1314.network;

public interface MatchUpdateCallback{
	/**
	 * Serverseitig: der Client teilt einen Mapvorschlag mit
	 */
	void callback(String map);
}
