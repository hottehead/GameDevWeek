package de.hochschuletrier.gdw.commons.netcode.message;

/**
 * An incoming message
 *
 * @author Santo Pfingsten
 */
public interface INetMessageIn {

    /**
     * @return the maximum capacity of the buffer (not important while reading)
     */
    int capacity();

    /**
     * @return how many bytes have been read so far
     */
    int position();

    /**
     * @return how many bytes are left to read
     */
    int remaining();

    /**
     * @return the amount of stored bytes in this buffer
     */
    int limit();

    /**
     * @return one byte read from the buffer
     */
    byte get();

    /**
     * @return one boolean value (stored as byte) read from the buffer
     */
    boolean getBool();

    /**
     * @return one character read from the buffer
     */
    char getChar();

    /**
     * @return one short read from the buffer
     */
    short getShort();

    /**
     * @return one integer read from the buffer
     */
    int getInt();

    /**
     * @return one float read from the buffer
     */
    float getFloat();

    /**
     * @return one string read from the buffer
     */
    String getString();
}
