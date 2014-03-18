package de.hochschuletrier.gdw.ws1314.basic;


public interface PlayerInfoListener {
	public void teamChanged(PlayerInfo source);
	public void readyStateChanged(PlayerInfo source);
	public void characterClassChanged(PlayerInfo source);
	public void nameChanged(PlayerInfo source);
}
