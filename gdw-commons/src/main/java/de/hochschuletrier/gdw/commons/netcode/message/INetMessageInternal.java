package de.hochschuletrier.gdw.commons.netcode.message;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * The internal interface for the messages
 *
 * @author Santo Pfingsten
 */
public interface INetMessageInternal extends INetMessageIn, INetMessageOut {

    /**
     * Recycle this object, called right before it gets re-used
     */
    void recycle();

    /**
     * Mark this object to be available for re-use
     */
    void free();

    /**
     * Prepare the message for reading from a socket channel
     *
     * @param messageSize the size of the message buffer
     * @param deltaSize the size of the delta buffer
     */
    public void prepareReading(int messageSize, int deltaSize);

    /**
     * Prepare the message for writing to a socket channel
     */
    public void prepareWriting();

    /**
     * Read the message buffer (and optionally the delta buffer) from a channel
     *
     * @param channel the socket channel to read from
     * @return bytes read
     * @throws IOException
     */
    int readFromSocket(SocketChannel channel) throws IOException;

    /**
     * Write the message buffer (and optionally the delta buffer) to a channel
     *
     * @param channel the socket channel to write to
     * @return bytes written
     * @throws IOException
     */
    int writeToSocket(SocketChannel channel) throws IOException;
}
