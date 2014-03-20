package de.hochschuletrier.gdw.ws1314.render;

import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;

/*
 * Sebastian
 * Listener der aufgerufen wird nachdem intern Entities 
 * hinzugefügt oder entfernt wurden.
 */
public interface ClientEntityManagerListener {

	public void onEntityInsert(ClientEntity entity);
	
	public void onEntityRemove(ClientEntity entity);
	
}
