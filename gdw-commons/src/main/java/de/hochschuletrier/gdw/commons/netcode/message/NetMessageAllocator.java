package de.hochschuletrier.gdw.commons.netcode.message;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A message allocator. Helps to not create/remove buffers too often.
 *
 * @author Santo Pfingsten
 */
public class NetMessageAllocator {

    /** Stores all free normal messages */
    private static final ConcurrentLinkedQueue<NetMessage> freeMessages = new ConcurrentLinkedQueue<NetMessage>();
    /** Stores all free delta messages */
    private static final ConcurrentLinkedQueue<NetMessageDelta> freeDeltaMessages = new ConcurrentLinkedQueue<NetMessageDelta>();

    /**
     * Get a NetMessage object to work with
     *
     * @return a new or recycled NetMessage object
     */
    public static NetMessage createMessage() {
        NetMessage message = freeMessages.poll();
        if (message == null) {
            message = new NetMessage();
        } else {
            message.recycle();
        }
        return message;
    }

    /**
     * Get a NetMessageDelta object to work with
     *
     * @return a new or recycled NetMessageDelta object
     */
    public static NetMessageDelta createMessageDelta() {
        NetMessageDelta message = freeDeltaMessages.poll();
        if (message == null) {
            message = new NetMessageDelta();
        } else {
            message.recycle();
        }
        return message;
    }

    /**
     * Mark a message ready to be re-used
     *
     * @param message the message to free
     */
    public static void free(NetMessage message) {
        freeMessages.add(message);
    }

    /**
     * Mark a message ready to be re-used
     *
     * @param message the message to free
     */
    public static void free(NetMessageDelta message) {
        freeDeltaMessages.add(message);
    }
}
