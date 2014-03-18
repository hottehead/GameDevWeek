package de.hochschuletrier.gdw.ws1314.entity;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Jerry on 18.03.14.
 */
public class ServerEntityFactory {
    protected HashMap<Class<? extends ServerEntity>, LinkedList<ServerEntity>> recycleMap;

    public ServerEntityFactory() {
        recycleMap = new HashMap<Class<? extends ServerEntity>, LinkedList<ServerEntity>>();
    }

    public void recycle(ServerEntity e) {
        LinkedList<ServerEntity> recycleList = recycleMap.get(e.getClass());
        if (recycleList == null) {
            recycleList = new LinkedList<ServerEntity>();
            recycleMap.put(e.getClass(), recycleList);
        }
        recycleList.add(e);
    }

    private ServerEntity internalCreate(Class<? extends ServerEntity> entityClass) {
        ServerEntity e = null;
        try {
            assert (entityClass.getConstructor() != null);
            e = entityClass.newInstance();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return e;
    }

    @SuppressWarnings("unchecked")
    public <T extends ServerEntity> T createEntity(Class<? extends ServerEntity> entityClass) {
        ServerEntity e;
        e = internalCreate(entityClass);
        return (T) e;
    }

}
