package de.hochschuletrier.gdw.ws1314.network;

import de.hochschuletrier.gdw.ws1314.states.GameStates;

public interface GameStateAckCallback{
	/**
	 * Serverseitig: der Client teilt dem Server den angenommenen GameState mit
	 */
	public void gameStateAckCallback(int playerId, GameStates gameStates);

}
