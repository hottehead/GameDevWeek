package de.hochschuletrier.gdw.ws1314.entity;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.utils.id.Identifier;
import de.hochschuletrier.gdw.commons.utils.ClassUtils;
import de.hochschuletrier.gdw.commons.tiled.SafeProperties;
import de.hochschuletrier.gdw.ws1314.game.ClientServerConnect;
import de.hochschuletrier.gdw.ws1314.game.LevelLoader;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;



import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by jerry on 17.03.14.
 */
public class ServerEntityManager {
    private static ServerEntityManager instance = null;

    //ToDo Dirty Solution Please Fix
    private static PhysixManager physManager;
	
	private Identifier entityIDs;
    private LinkedList<ServerEntity> entityList;
    private HashMap<Long,ServerEntity> entityListMap;
    protected Queue<ServerEntity> removalQueue;
    protected Queue<ServerEntity> insertionQueue;
    protected HashMap<String, Class<? extends ServerEntity>> classMap = new HashMap<String, Class<? extends ServerEntity>>();
    protected ServerEntityFactory factory;

    protected ServerEntityManager(){
        entityList = new LinkedList<ServerEntity>();
        entityListMap = new HashMap<Long, ServerEntity>();
        factory = new ServerEntityFactory();
        entityIDs = new Identifier(20);
		removalQueue = new LinkedList<ServerEntity>();
		insertionQueue = new LinkedList<ServerEntity>();



        try {
            for (Class c : ClassUtils
                    .findClassesInPackage("de.hochschuletrier.gdw.ws1324.entity")) {
                if (ServerEntity.class.isAssignableFrom(c))
                    classMap.put(c.getSimpleName().toLowerCase(), c);
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't find entity classes", e);
        }

    }
    
    public static ServerEntityManager getInstance()
    {

    	if (instance == null)
    		instance = new ServerEntityManager();


    	return instance;
    }

    public void setPhysixManager(PhysixManager physManager){
        ServerEntityManager.physManager = physManager;
    }

    public ServerEntity getEntityById(long id) {
        return entityListMap.get(new Long(id));
    }

    public int getListSize() {
        return entityList.size();
    }

    public ServerEntity getListEntity(int index) {
        return entityList.get(index);
    }


    public void update(float delta) {
        internalRemove();
        internalInsert();

        for (ServerEntity e : entityList)
            e.update( delta);

    }


    private boolean internalRemove() {
        boolean listChanged = false;
        ClientServerConnect netManager = ClientServerConnect.getInstance();
        while (!removalQueue.isEmpty()) {
            listChanged = true;
            ServerEntity e = removalQueue.poll();
            e.dispose(physManager);
            entityList.remove(e);
            entityListMap.remove(e.getID());
            netManager.despawnEntity(e.getID());
            
        }
        return listChanged;
    }

    private boolean internalInsert() {
        boolean listChanged = false;
        while (!insertionQueue.isEmpty()) {
            listChanged = true;
            ServerEntity e = insertionQueue.poll();

            e.initialize();
            e.initPhysics(physManager);

            entityList.add(e);
            entityListMap.put(e.getID(),e);
        }
        return listChanged;
    }

    private void addEntity(ServerEntity e) {
        e.setId(entityIDs.requestID());
        insertionQueue.add(e);
    }

    public void removeEntity(ServerEntity e) {
        entityIDs.returnID(e.getID());
        removalQueue.add(e);

    }

    public <T extends ServerEntity> T createEntity(Class<? extends ServerEntity> entityClass, Vector2 position) {
        T e = factory.createEntity(entityClass);
        assert (e != null);
        SafeProperties properties = new SafeProperties();
        properties.setFloat("x",position.x);
        properties.setFloat("y",position.y);
        e.setProperties(properties);
        addEntity(e);
        return e;
    }

    public ServerEntity createEntity(String className, SafeProperties properties, Vector2 position) {
        Class<? extends ServerEntity> entityClass = classMap.get(className
                .toLowerCase());
        if (entityClass == null) {
            throw new RuntimeException("Could not find entity class for: "
                    + className);
        }
        ServerEntity e = factory.createEntity(entityClass);
        properties.setFloat("x",position.x);
        properties.setFloat("y",position.y);
        e.setProperties(properties);
        addEntity(e);
        return e;
    }

    public void Clear()
    {
    	internalRemove();
    	this.entityList.clear();
    	this.entityListMap.clear();
    	this.insertionQueue.clear();
    	// classMap und identifier brauchen nicht neu erstellt zu werden!?
    }

}
