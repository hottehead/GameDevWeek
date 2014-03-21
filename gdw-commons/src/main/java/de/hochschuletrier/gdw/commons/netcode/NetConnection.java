package de.hochschuletrier.gdw.commons.netcode;

import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagramFactory;
import de.hochschuletrier.gdw.commons.netcode.datagram.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.datagram.NetEventDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A NetConnection represents a connection from server to client or vice versa.
 * This connection is capable of sending and receiving events/datagrams.
 *
 * @author Santo Pfingsten
 */
public class NetConnection extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(NetConnection.class);

    /** The header size */
    public static final int HEADER_SIZE = 1 + 3 * 2;
    /** The socket channel */
    private final SocketChannel channel;
    /** The factory used to create datagrams based on their type */
    private final INetDatagramFactory datagramFactory;
    /** The reception that accepted the connection */
    private NetReception reception;
    /** A user-set attachment, for example a player object */
    private Object attachment;
    /** Set to true during shutdown */
    private boolean shutdown = false;
    /** The exception that caused the disconnect */
    private IOException disconnectException;
    /** The time of the last outgoing message used to send keep alive signals */
    private long lastOutgoing;
    /** The time to wait before sending a keep alive signal */
    private int keepAliveTime = 1000;
    /** The byte buffer used to read in new datagram headers */
    private final ByteBuffer headerIn = ByteBuffer.allocate(HEADER_SIZE);
    /** The byte buffer used to write new datagram headers */
    private final ByteBuffer headerOut = ByteBuffer.allocate(HEADER_SIZE);
    /** Incoming datagrams fully processed, ready to be received. */
    private final ConcurrentLinkedQueue<INetDatagram> incomingDatagrams = new ConcurrentLinkedQueue<INetDatagram>();
    /** Outgoing datagrams unprocessed. */
    private final ConcurrentLinkedQueue<INetDatagram> outgoingDatagrams = new ConcurrentLinkedQueue<INetDatagram>();
    /** The message cache for incoming delta compressed datagrams */
    private final NetMessageCache messageCacheIn = new NetMessageCache();
    /** The message cache for outgoing delta compressed datagrams */
    private final NetMessageCache messageCacheOut = new NetMessageCache();
    /** Set to true so outgoing datagrams can be queued */
    private boolean accepted;
    
    /** Total bytes sent to this connection */
    private long bytesSent=0;
    /** Total bytes received from this connection */
    private long bytesReceived=0;
    /** Number of datagrams sent to this connection */
    private long datagramsSent=0;
    /** Number of datagrams received from this connection */
    private long datagramsReceived=0;
    

    /**
     * Create a connection to a client.
     * This starts a thread that will receive and prepare datagrams.
     *
     * @param reception the reception that accepted the connection.
     * @param channel the socket channel to the client
     * @param datagramFactory the factory used to create datagrams based on their type.
     * @throws IOException when setting the TCP_NODELAY option fails.
     */
    NetConnection(NetReception reception, SocketChannel channel, INetDatagramFactory datagramFactory) throws IOException {
        this.datagramFactory = datagramFactory;
        channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
        this.channel = channel;
        this.reception = reception;

        accepted = false;
        start();
    }

    /**
     * Create a connection to a server
     *
     * @param ip the server name or ip of the server to connect to
     * @param port the server listening port
     * @param datagramFactory the factory used to create datagrams based on their type.
     * @throws IOException when setting the TCP_NODELAY option fails.
     */
    public NetConnection(String ip, int port, INetDatagramFactory datagramFactory) throws IOException {
        this.datagramFactory = datagramFactory;
        channel = SocketChannel.open(new InetSocketAddress(InetAddress.getByName(ip), port));
        channel.setOption(StandardSocketOptions.TCP_NODELAY, true);

        accepted = true;
        start();
    }

    /**
     * @return a list of all ip addresses of localhost
     */
    public static List<InetAddress> getLocalAddresses() {
        List<InetAddress> addrList = new ArrayList<InetAddress>();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {

                try {
                    NetworkInterface ifc = en.nextElement();
                    if (ifc.isUp()) {
                        for (Enumeration<InetAddress> ena = ifc.getInetAddresses(); ena.hasMoreElements();) {
                            InetAddress addr = ena.nextElement();
                            addrList.add(addr);
                        }
                    }
                } catch (SocketException ex) {
                    logger.error("Exception trying to get local addresses", ex);
                }
            }

        } catch (SocketException ex) {
            logger.error("Exception trying to get local addresses", ex);
        }
        return addrList;
    }

    /**
     * @return the default ip of localhost
     */
    public static String getDefaultIp() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostAddress();
        } catch (UnknownHostException ex) {
            logger.error("Can't get ip for localhost", ex);
            return "localhost";
        }
    }

    /**
     * The main routine to read in and prepare datagrams
     */
    @Override
    public void run() {
        while (!shutdown) {
            try {
                // Read the datagram header into the buffer.
                headerIn.clear();
                while (headerIn.hasRemaining()) {
                    bytesReceived+=channel.read(headerIn);
                }

                // Get the values
                headerIn.flip();
                byte type = headerIn.get();
                short id = headerIn.getShort();
                short param1 = headerIn.getShort();
                short param2 = headerIn.getShort();

                if (type == NetDatagram.Type.DISCONNECT) {
                    shutdown = true; // so we don't send the disconnect signal back
                    shutdown();
                } // Handle the datagrams based on their type
                else if (type != INetDatagram.Type.KEEP_ALIVE) {
                    handleDatagram(type, id, param1, param2);
                }
			}catch (java.nio.channels.AsynchronousCloseException e){
				//
            } catch (IOException e) {
                logger.error("Failed reading NetDatagram", e);
                // During a shutdown, we don't record exceptions
                if (!shutdown) {
                    // An exception causes a disconnect right now, maybe want to change that ?
                    if (disconnectException == null) {
                        disconnectException = e;
                    }
                    shutdown();
                }
            }
        }

        // Clear the message caches, so the message objects can be recycled.
        messageCacheIn.clear();
        messageCacheOut.clear();

        // Notify the reception about the disconnect
        if (reception != null) {
            reception.onClientDisconnect(this);
        }
    }

    /**
     * Handle an incoming datagram
     *
     * @param type the datagram type
     * @param id a unique id, used in combo with type for identifying delta message bases
     * @param param1 the first parameter of the header. If the message type is not none, this contains the message size.
     * @param param2 the second parameter of the header. If the message type is not none, this contains the delta message size.
     * @throws IOException
     */
    private void handleDatagram(byte type, short id, short param1, short param2) throws IOException {
        INetDatagram datagram = datagramFactory.createDatagram(type, id, param1, param2);

        NetMessage msg;
        NetMessageDelta deltaMsg;
        NetMessage base;
        NetMessage newBase;

        switch (datagram.getMessageType()) {
            case NONE:
                // No message, we're done.
                break;
            case NORMAL:
                // Normal message, param1 contains the message size.
                msg = NetMessageAllocator.createMessage();
                msg.prepareReading(param1, 0);
                bytesReceived+=msg.readFromSocket(channel);

                // Let the datagram read its data
                datagram.readFromMessage(msg);
                // Don't need the message anymore, free it so it can be recycled.
                msg.free();
                break;
            case DELTA:
                // Delta messages are a little more complex.
                deltaMsg = (NetMessageDelta) NetMessageAllocator.createMessageDelta();
                // If this is not the first message, we'll need the content of the last received message.
                base = messageCacheIn.get(datagram.getType(), datagram.getID());
                // The new base will store the full (decompressed) content of this message for use on the next message.
                newBase = NetMessageAllocator.createMessage();
                // Prepare the delta message for reading
                deltaMsg.prepareDeltaReading(base, newBase);

                // Read both the message and the delta bits from the channel
                deltaMsg.prepareReading(param1, param2);
                bytesReceived+=deltaMsg.readFromSocket(channel);

                // Let the datagram read its data
                datagram.readFromMessage(deltaMsg);
                deltaMsg.free();

                // Set the new base (which will also free the old base)
                messageCacheIn.set(datagram.getType(), datagram.getID(), newBase);
                break;
        }
        datagramsReceived++;
        // The datagram is ready to be received
        incomingDatagrams.add(datagram);
    }

    /**
     * Send all pending (outgoing) datagrams now.
     */
    public void sendPendingDatagrams() {
        if (!shutdown) {
            // If no message has been scheduled within the last x seconds, send a keep alive signal.
            if (lastOutgoing != 0 && keepAliveTime > 0 && (System.currentTimeMillis() - lastOutgoing) > keepAliveTime) {
                sendEvent(INetDatagram.Type.KEEP_ALIVE, (short) 0, (short) 0, (short) 0);
            }

            try {
                while (!outgoingDatagrams.isEmpty()) {
                	datagramsSent++;
                    INetDatagram datagram = outgoingDatagrams.poll();
                    switch (datagram.getMessageType()) {
                        case NONE:
                            // No message, just send the header
                            sendHeader(datagram.getType(), datagram.getID(), datagram.getParam1(), datagram.getParam2());
                            break;
                        case NORMAL:
                            // Just send a simple message
                            sendMessage(datagram);
                            break;
                        case DELTA:
                            // Send a message delta compressed
                            sendMessageDelta(datagram);
                            break;
                    }
                }
            } catch (IOException e) {
                logger.error("Failed sending NetDatagram", e);

                // An exception causes a disconnect right now, maybe want to change that ?
                if (disconnectException == null) {
                    disconnectException = e;
                }
                shutdown();
            }
        }
    }

    /**
     * Send a datagram header
     *
     * @param type the datagram type
     * @param id a unique id, used in combo with type for identifying delta message bases
     * @param param1 the first parameter of the header. If the message type is not none, this contains the message size.
     * @param param2 the second parameter of the header. If the message type is not none, this contains the delta message size.
     * @throws IOException
     */
    private void sendHeader(byte type, short id, short param1, short param2) throws IOException {
        headerOut.clear();
        headerOut.put(type);
        headerOut.putShort(id);
        headerOut.putShort(param1);
        headerOut.putShort(param2);
        headerOut.flip();

        while (headerOut.hasRemaining()) {
            bytesSent+=channel.write(headerOut);
        }
    }

    /**
     * Send a normal message datagram
     *
     * @param datagram the datagram to send
     * @throws IOException
     */
    private void sendMessage(INetDatagram datagram) throws IOException {
        // Let the datagram write its data
        NetMessage msg = NetMessageAllocator.createMessage();
        datagram.writeToMessage(msg);

        // Send the header and the message
        sendHeader(datagram.getType(), datagram.getID(), (short) msg.position(), (short) 0);
        sendMessage(msg);
    }

    /**
     * Send a delta compressed message datagram
     *
     * @param datagram the datagram to send
     * @throws IOException
     */
    private void sendMessageDelta(INetDatagram datagram) throws IOException {
        NetMessageDelta deltaMsg = (NetMessageDelta) NetMessageAllocator.createMessageDelta();
        // If this is not the first message, we'll need the content of the last send message.
        NetMessage base = messageCacheOut.get(datagram.getType(), datagram.getID());
        // The new base will store the full (decompressed) content of this message for use on the next message.
        NetMessage newBase = NetMessageAllocator.createMessage();
        // Prepare the delta message for writing
        deltaMsg.prepareDeltaWriting(base, newBase);

        // Let the datagram write its data
        datagram.writeToMessage(deltaMsg);

        // Nothing changed, free up the messages we created
        if (!deltaMsg.hasChanged()) {
            deltaMsg.free();
            newBase.free();
            return;
        }

        // Set the new base (which will also free the old base)
        messageCacheOut.set(datagram.getType(), datagram.getID(), newBase);

        // Send the header and both the message and the delta bits from the channel
        sendHeader(datagram.getType(), datagram.getID(), (short) deltaMsg.position(), deltaMsg.deltaSize());
        sendMessage(deltaMsg);
    }

    /**
     * Send the message content. Automatically frees the message afterwards.
     *
     * @param msg the message
     * @throws IOException
     */
    private void sendMessage(INetMessageInternal msg) throws IOException {
        msg.prepareWriting();
        bytesSent+=msg.writeToSocket(channel);
        msg.free();
    }

    /**
     * Shut down the connection (disconnect)
     */
    public void shutdown() {
        try {
            if (!shutdown) {
                try {
                    sendHeader(NetDatagram.Type.DISCONNECT, (short) 0, (short) 0, (short) 0);
                } catch (java.nio.channels.AsynchronousCloseException e) {
					return;
				} catch (IOException e) {
                    logger.error("Failed sending disconnect", e);
                    // doesn't matter if the disconnect event does not get send
                }
            }
            shutdown = true;
            channel.close();
        } catch (IOException e) {
            logger.error("Failed to close channel", e);
        }
    }

    /**
     * @return true if datagrams can be received
     */
    public boolean hasIncoming() {
        return !incomingDatagrams.isEmpty();
    }

    /**
     * @return the next datagram available
     */
    public INetDatagram receive() {
        return incomingDatagrams.poll();
    }

    /**
     * Add a datagram to the pending datagrams queue.
     * Note: If the connection has not been accepted yet, it will do nothing.
     *
     * @param datagram the datagram to be send
     */
    public void send(INetDatagram datagram) {
        if (accepted) {
            lastOutgoing = System.currentTimeMillis();
            outgoingDatagrams.add(datagram);
        }
    }

    /**
     * Add an event datagram to the pending datagrams queue.
     * Note: If the connection has not been accepted yet, it will do nothing.
     *
     * @param type the datagram type
     * @param id a unique id, used in combo with type for identifying delta message bases
     * @param param1 the first parameter of the header. If the message type is not none, this contains the message size.
     * @param param2 the second parameter of the header. If the message type is not none, this contains the delta message size.
     */
    public void sendEvent(byte type, short id, short param1, short param2) {
        if (accepted) {
            lastOutgoing = System.currentTimeMillis();
            outgoingDatagrams.add(new NetEventDatagram(type, id, param1, param2));
        }
    }

    /**
     * @return the exception that caused a disconnect
     */
    public IOException getDisconnectException() {
        return disconnectException;
    }

    /**
     * @return the attachment previously attached to this connection
     */
    public Object getAttachment() {
        return attachment;
    }

    /**
     * Set an attachment to this connection.
     * This can be helpful to (for example) store the player reference bound to this connection.
     *
     * @param object the attachment object
     */
    public void setAttachment(Object object) {
        attachment = object;
    }

    /**
     * @return true if the connection is still established
     */
    public boolean isConnected() {
        return !shutdown && channel.isConnected();
    }

    /**
     * Set the time when to send a keep alive signal.
     *
     * @param ms the time in ms since the last message has been send.
     */
    public void setKeepAlive(int ms) {
        keepAliveTime = ms;
    }

    /**
     * Set the accepted state of this connection.
     * If accepted is false, no new datagrams will be scheduled to be send.
     *
     * @param value the new value
     */
    public void setAccepted(boolean value) {
        accepted = value;
    }

    /**
     * @return the accepted state
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * @return Number of bytes sent to the connection.
     */
	public long getBytesSent(){
		return bytesSent;
	}

	/**
	 * @returnNumber of bytes received from the connection.
	 */
	public long getBytesReceived(){
		return bytesReceived;
	}

	public long getDatagramsSent(){
		return datagramsSent;
	}

	public long getDatagramsReceived(){
		return datagramsReceived;
	}
}
