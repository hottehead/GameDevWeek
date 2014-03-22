package de.hochschuletrier.gdw.ws1314.network;

public interface PlayerDisconnectCallback{
	/**
	 * PlayerDisconnectCallback: Serverseitig. Wenn einer oder mehrere Clients disconnecten, wird deren ID
	 * in diesem Callback mtigegeben damit die Serverdaten angepasst werden können.
	 * Danach ist eine Funktion wie LobbyUpdate notwendig um diese änderung den Clients mitzutzeilen
	 */
	public void playerDisconnectCallback(Integer[] playerid);
}
