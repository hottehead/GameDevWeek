package de.hochschuletrier.gdw.ws1314.entity;

import de.hochschuletrier.gdw.commons.utils.id.Identifier;
import de.hochschuletrier.gdw.commons.utils.ClassUtils;
import de.hochschuletrier.gdw.commons.tiled.SafeProperties;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by jerry on 17.03.14.
 */
public class ServerEntityManager {
    private Identifier entityIDs = new Identifier(20);
    private LinkedList<ServerEntity> entityList;
    private HashMap<Long,ServerEntity> entityListMap;
    protected Queue<ServerEntity> removalQueue;
    protected Queue<ServerEntity> insertionQueue;
    protected HashMap<String, Class<? extends ServerEntity>> classMap = new HashMap<String, Class<? extends ServerEntity>>();
    protected ServerEntityFactory factory;


    public ServerEntityManager(){
        entityList = new LinkedList<ServerEntity>();
        entityListMap = new HashMap<Long, ServerEntity>();
        factory = new ServerEntityFactory();

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

    public ServerEntity getEntityById(long id) {
        return entityListMap.get(new Long(id));
    }


    public void update(int delta) {
        internalRemove();
        internalInsert();

        for (ServerEntity e : entityList)
            e.update( delta);

    }


    private boolean internalRemove() {
        boolean listChanged = false;
        while (!removalQueue.isEmpty()) {
            listChanged = true;
            ServerEntity e = removalQueue.poll();
            e.dispose();
            entityList.remove(e);
        }
        return listChanged;
    }

    private boolean internalInsert() {
        boolean listChanged = false;
        while (!insertionQueue.isEmpty()) {
            listChanged = true;
            ServerEntity e = insertionQueue.poll();

            e.initialize();


            entityList.add(e);
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

    public <T extends ServerEntity> T createEntity(Class<? extends ServerEntity> entityClass) {
        T e = factory.createEntity(entityClass);
        assert (e != null);
        addEntity(e);
        return e;
    }

    public ServerEntity createEntity(String className, SafeProperties properties) {
        Class<? extends ServerEntity> entityClass = classMap.get(className
                .toLowerCase());
        if (entityClass == null) {
            throw new RuntimeException("Could not find entity class for: "
                    + className);
        }
        ServerEntity e = factory.createEntity(entityClass);
        e.setProperties(properties);
        addEntity(e);
        return e;
    }


}
