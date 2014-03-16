package de.hochschuletrier.gdw.commons.netcode.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * A normal (uncompressed) message
 *
 * @author Santo Pfingsten
 */
public class NetMessage implements INetMessageInternal {

    /** The maximum number of bytes this message can take */
    public static final int MAX_SIZE = 2048;
    /** The buffer to take the message */
    private final ByteBuffer buffer = ByteBuffer.allocate(MAX_SIZE);

    /**
     * Rewind the buffer
     */
    public void rewind() {
        buffer.rewind();
    }

    @Override
    public void recycle() {
        buffer.clear();
    }

    @Override
    public void free() {
        NetMessageAllocator.free(this);
    }

    @Override
    public int capacity() {
        return buffer.capacity();
    }

    @Override
    public int position() {
        return buffer.position();
    }

    @Override
    public int remaining() {
        return buffer.remaining();
    }

    @Override
    public int limit() {
        return buffer.limit();
    }

    @Override
    public byte get() {
        return buffer.get();
    }

    @Override
    public boolean getBool() {
        // booleans are stored as byte
        return buffer.get() != 0;
    }

    @Override
    public char getChar() {
        return buffer.getChar();
    }

    @Override
    public short getShort() {
        return buffer.getShort();
    }

    @Override
    public int getInt() {
        return buffer.getInt();
    }

    @Override
    public float getFloat() {
        return buffer.getFloat();
    }

    @Override
    public String getString() {
        StringBuilder sb = new StringBuilder();
        char c;
        // read characters until a '\0' char was found.
        while ((c = buffer.getChar()) != '\0') {
            sb.append(c);
        }

        return sb.toString();
    }

    @Override
    public void put(byte value) {
        buffer.put(value);
    }

    @Override
    public void putBool(boolean value) {
        // booleans are stored as byte
        buffer.put((byte) (value ? 1 : 0));
    }

    @Override
    public void putChar(char value) {
        buffer.putChar(value);
    }

    @Override
    public void putShort(short value) {
        buffer.putShort(value);
    }

    @Override
    public void putInt(int value) {
        buffer.putInt(value);
    }

    @Override
    public void putFloat(float value) {
        buffer.putFloat(value);
    }

    @Override
    public void putString(String value) {
        putString(value, value.length());
    }

    @Override
    public void putString(String value, int maxLength) {
        int max = Math.min(value.length(), maxLength);

        for (int i = 0; i < max; i++) {
            buffer.putChar(value.charAt(i));
        }

        // terminate with a '\0' to signal the end
        buffer.putChar('\0');
    }

    @Override
    public void prepareReading(int messageSize, int deltaSize) {
        // Limit the buffer, so we only read the number of bytes set in the header
        buffer.limit(messageSize);
    }

    @Override
    public void prepareWriting() {
        buffer.flip();
    }

    @Override
    public void readFromSocket(SocketChannel channel) throws IOException {
        while (buffer.hasRemaining()) {
            channel.read(buffer);
        }

        buffer.flip();
    }

    @Override
    public void writeToSocket(SocketChannel channel) throws IOException {
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }
}
