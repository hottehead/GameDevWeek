package de.hochschuletrier.gdw.ws1314.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientEgg;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.render.ClientEntityManagerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Jerry on 18.03.14.
 */
public class ClientEntityManager {
    private static final Logger logger = LoggerFactory.getLogger(ClientEntityManager.class);

	private static ClientEntityManager instance = null;
	
	private LinkedList<ClientEntity> entityList;
    private HashMap<Long,ClientEntity> entityListMap;
    protected Queue<ClientEntity> removalQueue;
    protected Queue<ClientEntity> insertionQueue;
    protected ArrayList<ClientEntityManagerListener> listeners;

    private long playerEntityID = -1;

    protected ClientEntityManager(){
        entityList = new LinkedList<ClientEntity>();
        entityListMap = new HashMap<Long, ClientEntity>();
		removalQueue = new LinkedList<ClientEntity>();
		insertionQueue = new LinkedList<ClientEntity>();
		listeners = new ArrayList<ClientEntityManagerListener>();
    }

    public static ClientEntityManager getInstance()
    {
    	if (instance == null)
    		instance = new ClientEntityManager();
    	
    	return instance;
    }
    
    public ClientEntity createEntity(long id, Vector2 pos,EntityType type){
        ClientEntity e = null;
        switch(type){
            case Ei :{
                e = new ClientEgg();

                break;
            }
            case Noob:{
                e = createPlayer(PlayerKit.NOOB);
                break;
            }
            case Hunter:{
                e = createPlayer(PlayerKit.HUNTER);
                break;
            }
            /*case Tank :{
                e = createPlayer(PlayerKit.TANK);
                break;
            }*/
            case Knight :{
                e = createPlayer(PlayerKit.KNIGHT);
                break;
            }
        }
        if(e!=null)
        {
            e.setID(id);
            e.setPosition(pos);
            addEntity(e);
        }
        return e;
    }

    public void setPlayerEntityID(long id) {
        playerEntityID = id;
    }

    public long getPlayerEntityID() {
        if(playerEntityID < 0)
            logger.warn("Sieht so aus als wurde PlayerID noch nicht gesetzt. getPlayerEntityID muss warscheinlich nochmal aufgerufen werden.");

        return playerEntityID;
    }

    private ClientEntity createPlayer(PlayerKit pk) {
        ClientPlayer cp = new ClientPlayer();
        cp.setPlayerKit(pk);
        return cp;
    }

    private void addEntity(ClientEntity e) {
        insertionQueue.add(e);
    }

    public void removeEntity(ClientEntity e) {
        if(e!=null)
            removalQueue.add(e);
    }

    public int getListSize() {
        return entityList.size();
    }

    public ClientEntity getListEntity(int index) {
        return entityList.get(index);
    }

    private boolean internalRemove() {
        boolean listChanged = false;
        while (!removalQueue.isEmpty()) {
            listChanged = true;
            ClientEntity e = removalQueue.poll();
            e.dispose();
            entityList.remove(e);
            entityListMap.remove(e.getID());
            for(ClientEntityManagerListener l : listeners) {
            	l.onEntityRemove(e);
            }
        }
        return listChanged;
    }

    private boolean internalInsert() {
        boolean listChanged = false;
        while (!insertionQueue.isEmpty()) {
            listChanged = true;
            ClientEntity e = insertionQueue.poll();
            entityList.add(e);
            entityListMap.put(e.getID(),e);
            for(ClientEntityManagerListener l : listeners) {
            	l.onEntityInsert(e);
        	}
        }
        return listChanged;
    }


    public ClientEntity getEntityById(long id) {
        Long lid = new Long(id);
        if(entityListMap.containsKey(lid))
        {
            return entityListMap.get(lid);
        }
        return null;
    }

    public void update(float delta) {
        internalRemove();
        internalInsert();

        for (ClientEntity e : entityList)
            e.update( delta);

    }
    
    public void Clear()
    {
    	internalRemove();
    	this.entityList.clear();
    	this.entityListMap.clear();
    	this.insertionQueue.clear();
    }
    
    public void provideListener(ClientEntityManagerListener l) {
    	this.listeners.add(l);
    }
    
    public void removeListener(ClientEntityManagerListener l) {
    	this.listeners.remove(l);
    }
}

