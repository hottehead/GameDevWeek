package de.hochschuletrier.gdw.ws1314.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientBridge;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientBridgeSwitch;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientBush;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientCarrot;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientClover;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientContactMine;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientEgg;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ClientSpinach;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ClientProjectile;
import de.hochschuletrier.gdw.ws1314.render.ClientEntityManagerListener;

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
        switch(type)
        {
            case Ei:
                e = new ClientEgg();
                break;
            case Noob:
                e = createPlayer(PlayerKit.NOOB);
                break;
            case Hunter:
                e = createPlayer(PlayerKit.HUNTER);
                break;
            case Tank:
                e = createPlayer(PlayerKit.TANK);
                break;
            case Knight:
                e = createPlayer(PlayerKit.KNIGHT);
                break;
            case Projectil: 
            	e = new ClientProjectile();
            	break;
            case Bridge:
            	e = new ClientBridge();
            	break;
            case BridgeSwitch:
            	e = new ClientBridgeSwitch();
            	break;
            case Bush:
            	e = new ClientBush();
            	break;
            case ContactMine:
            	e = new ClientContactMine();
            	break;
            case Carrot:
            	e = new ClientCarrot();
            	break;
            case Spinach:
            	e = new ClientSpinach();
            	break;
            case Clover:
            	e = new ClientClover();
            	break;
			default:
				break;
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

    public boolean isPendingSpawn(long entityId){
    	for(ClientEntity e: insertionQueue){
    		if(e.getID()==entityId){
    			return true;
    		}
    	}
    	return false;
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

