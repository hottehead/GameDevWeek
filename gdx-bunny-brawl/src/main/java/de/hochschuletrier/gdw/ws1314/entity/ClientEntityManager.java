package de.hochschuletrier.gdw.ws1314.entity;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Jerry on 18.03.14.
 */
public class ClientEntityManager {
	private static ClientEntityManager instance = null;
	
	private LinkedList<ClientEntity> entityList;
    private HashMap<Long,ClientEntity> entityListMap;
    protected Queue<ClientEntity> removalQueue;
    protected Queue<ClientEntity> insertionQueue;

    protected ClientEntityManager(){
        entityList = new LinkedList<ClientEntity>();
        entityListMap = new HashMap<Long, ClientEntity>();
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

                break;
            }
            case Hunter:{

                break;
            }
            case Tank :{

                break;
            }
            case Knight :{

                break;
            }
        }
        addEntity(e);
        return e;
    }

    private void addEntity(ClientEntity e) {
        insertionQueue.add(e);
    }

    public void removeEntity(ClientEntity e) {
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
        }
        return listChanged;
    }

    private boolean internalInsert() {
        boolean listChanged = false;
        while (!insertionQueue.isEmpty()) {
            listChanged = true;
            ClientEntity e = insertionQueue.poll();
            entityList.add(e);
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

}
