package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.ws1314.states.GameStates;

public interface GameStateCallback{
	/**
	 * Clientseitig: der Server teilt dem Clienten den GameState mit
	 */
	public void callback(GameStates gameStates);
}
