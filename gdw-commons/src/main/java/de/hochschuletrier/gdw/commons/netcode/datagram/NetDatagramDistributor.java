package de.hochschuletrier.gdw.commons.netcode.datagram;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Makes it possible to distribute datagrams to their respective handler methods
 *
 * @author Santo Pfingsten
 */
public class NetDatagramDistributor {
    private static final Logger logger = LoggerFactory.getLogger(NetDatagramDistributor.class);

    private final HashMap<Class, Method> methods = new HashMap<Class, Method>();
    private final INetDatagramHandler handler;

    public NetDatagramDistributor(INetDatagramHandler handler) {
        this.handler = handler;
        try {
            for (Method method : handler.getClass().getMethods()) {
                if (method.getName().equals("handle")) {
                    Class<?>[] types = method.getParameterTypes();
                    if (types.length == 2 && INetDatagram.class.isAssignableFrom(types[0]) && types[1].equals(NetConnection.class)) {
                        methods.put(types[0], method);
                    }
                }
            }
        } catch (SecurityException e) {
            logger.error("Failed retrieving handle methods", e);
        }
    }

    public boolean handle(INetDatagram datagram, NetConnection connection) throws InvocationTargetException {
        try {
            Method method = methods.get(datagram.getClass());
            method.invoke(handler, datagram, connection);
            return true;
        } catch (IllegalAccessException e) {
            logger.error("Failed accessing handle method.. maybe it isn't public?", e);
        } catch (IllegalArgumentException e) {
            logger.error("Failed calling handle method.. wrong argument.. weird!", e);
        }
        return false;
    }
}
