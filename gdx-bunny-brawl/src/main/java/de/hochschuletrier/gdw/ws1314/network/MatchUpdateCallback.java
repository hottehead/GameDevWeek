package de.hochschuletrier.gdw.ws1314.network;

public interface MatchUpdateCallback{
	/**
	 * Serverseitig: der Client teilt einen Mapvorschlag mit
	 */
	void matchUpateCallback(String map);
}
