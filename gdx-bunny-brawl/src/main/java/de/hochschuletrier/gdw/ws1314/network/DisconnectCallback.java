package de.hochschuletrier.gdw.ws1314.network;

public interface DisconnectCallback{
	/**
	 * DisconnectCallback: wird auf Server und Clientseite aufgerufen, sobald die eigene Verbindung verloren geht.
	 * z.B: Client disconnected daraufhin wir dieser Callback aufgerufen, damit der GameState geändert werden kann
	 */
	public void disconnectCallback(String msg);
}
