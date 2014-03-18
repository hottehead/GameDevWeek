package de.hochschuletrier.gdw.commons.netcode.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.BitSet;

/**
 * A delta compressed message.
 *
 * Nice bonus: BitSet only creates enough bytes to store all bits.
 * So if you order the data from most changing to least changing, it will write very few bytes for the deltaBits.
 *
 * @author Santo Pfingsten
 */
public class NetMessageDelta implements INetMessageInternal {

    /** The maximum number of bytes the delta buffer can take. (number of writing/reading operations = MAX_DELTA_SIZE * 8) */
    public static final int MAX_DELTA_SIZE = 128;
    /** Stores one bit for each writing operation if the value has changed or not */
    private BitSet deltaBits = new BitSet();
    /** The position in the deltaBits BitSet, or in other words the number of write/read operations done so far. */
    private short deltaBitIndex = 0;
    /** Stores if the message has changed at all since the last time */
    private boolean changed;
    /** The delta buffer. Stores the deltaBits when writing to a channel */
    private final ByteBuffer deltaBuffer = ByteBuffer.allocate(MAX_DELTA_SIZE);
    /** The message buffer contains only the values that changed */
    private final NetMessage message = NetMessageAllocator.createMessage();
    /** This is the last known state of this message. Used to compare to. null when this is the first transmit of the message */
    private NetMessage base;
    /** This will store the current values, which will be used the next time to find out what changed */
    private NetMessage newBase;

    @Override
    public void recycle() {
        deltaBuffer.clear();
        message.recycle();
        deltaBits.clear();
        deltaBitIndex = 0;
        changed = false;
        base = null;
        newBase = null;
    }

    @Override
    public void free() {
        NetMessageAllocator.free(this);
    }

    /**
     * Prepare writing delta compressed messages
     *
     * @param base the last known state of this message
     * @param newBase the message to store the current state in
     */
    public void prepareDeltaWriting(NetMessage base, NetMessage newBase) {
        this.base = base;
        this.newBase = newBase;

        if (base != null) {
            base.rewind();
        }

        message.recycle();
        deltaBitIndex = 0;
        changed = false;
    }

    /**
     * Prepare reading delta compressed messages
     *
     * @param base the last known state of this message
     * @param newBase the message to store the current state in
     */
    public void prepareDeltaReading(NetMessage base, NetMessage newBase) {
        this.base = base;
        this.newBase = newBase;

        if (base != null) {
            base.rewind();
        }

        message.rewind();
        deltaBitIndex = 0;
        changed = false;
    }

    /**
     * @return true if the message changed since the last time
     */
    public boolean hasChanged() {
        return changed;
    }

    /**
     * @return The number of bytes to write to the delta buffer
     */
    public short deltaSize() {
        return (short) deltaBits.toByteArray().length;
    }

    @Override
    public int capacity() {
        return message.capacity();
    }

    @Override
    public int position() {
        return message.position();
    }

    @Override
    public int remaining() {
        return message.remaining();
    }

    @Override
    public int limit() {
        return message.limit();
    }

    @Override
    public byte get() {
        // This process is essentially the same for all get* methods:
        byte value;

        // If no last known state is available, read it normally.
        if (base == null) {
            value = message.get();
            changed = true;
        } else {
            // We have a last known state. Read the value from it.
            value = base.get();

            // If the delta bits say that it has changed since the last time, read it fresh from the message.
            if (deltaBits.get(deltaBitIndex++)) {
                value = message.get();
                changed = true;
            }
        }

        // Store the new value in newBase for use when this message arrives next time.
        if (newBase != null) {
            newBase.put(value);
        }
        return value;
    }

    @Override
    public boolean getBool() {
        return get() != 0;
    }

    @Override
    public char getChar() {
        char value;
        if (base == null) {
            value = message.getChar();
            changed = true;
        } else {
            value = base.getChar();
            if (deltaBits.get(deltaBitIndex++)) {
                value = message.getChar();
                changed = true;
            }
        }

        if (newBase != null) {
            newBase.putChar(value);
        }
        return value;
    }

    @Override
    public short getShort() {
        short value;
        if (base == null) {
            value = message.getShort();
            changed = true;
        } else {
            value = base.getShort();
            if (deltaBits.get(deltaBitIndex++)) {
                value = message.getShort();
                changed = true;
            }
        }

        if (newBase != null) {
            newBase.putShort(value);
        }
        return value;
    }

    @Override
    public int getInt() {
        int value;
        if (base == null) {
            value = message.getInt();
            changed = true;
        } else {
            value = base.getInt();
            if (deltaBits.get(deltaBitIndex++)) {
                value = message.getInt();
                changed = true;
            }
        }

        if (newBase != null) {
            newBase.putInt(value);
        }
        return value;
    }

    @Override
    public long getLong() {
        long value;
        if (base == null) {
            value = message.getLong();
            changed = true;
        } else {
            value = base.getLong();
            if (deltaBits.get(deltaBitIndex++)) {
                value = message.getLong();
                changed = true;
            }
        }

        if (newBase != null) {
            newBase.putLong(value);
        }
        return value;
    }

    @Override
    public float getFloat() {
        float value;
        if (base == null) {
            value = message.getFloat();
            changed = true;
        } else {
            value = base.getFloat();
            if (deltaBits.get(deltaBitIndex++)) {
                value = message.getFloat();
                changed = true;
            }
        }

        if (newBase != null) {
            newBase.putFloat(value);
        }
        return value;
    }

    @Override
    public double getDouble() {
        double value;
        if (base == null) {
            value = message.getDouble();
            changed = true;
        } else {
            value = base.getDouble();
            if (deltaBits.get(deltaBitIndex++)) {
                value = message.getDouble();
                changed = true;
            }
        }

        if (newBase != null) {
            newBase.putDouble(value);
        }
        return value;
    }
    
    @Override
    public <T> T getEnum(Class<T> clazz) {
        return clazz.getEnumConstants()[get()];
    }

    @Override
    public String getString() {
        String value;
        if (base == null) {
            value = message.getString();
            changed = true;
        } else {
            value = base.getString();
            if (deltaBits.get(deltaBitIndex++)) {
                value = message.getString();
                changed = true;
            }
        }

        if (newBase != null) {
            newBase.putString(value, value.length());
        }
        return value;
    }

    @Override
    public void put(byte value) {
        // This process is essentially the same for all put* methods:
        // Store the new value in newBase for use when this message gets resend.
        if (newBase != null) {
            newBase.put(value);
        }

        // If no last known state is available, write it normally.
        if (base == null) {
            message.put(value);
            changed = true;
        } else {
            // We have a last known state. Read the value from it.
            byte baseValue = base.get();

            if (baseValue == value) {
                // If the value is the same, just note it in the delta bits
                deltaBits.set(deltaBitIndex++, false);
            } else {
                // If the value is not the same, note it in the delta bits and write the new value to the message
                deltaBits.set(deltaBitIndex++, true);
                message.put(value);
                changed = true;
            }
        }
    }

    @Override
    public void putBool(boolean value) {
        put((byte) (value ? 1 : 0));
    }

    @Override
    public void putChar(char value) {
        if (newBase != null) {
            newBase.putChar(value);
        }

        if (base == null) {
            message.putChar(value);
            changed = true;
        } else {
            char baseValue = base.getChar();
            if (baseValue == value) {
                deltaBits.set(deltaBitIndex++, false);
            } else {
                deltaBits.set(deltaBitIndex++, true);
                message.putChar(value);
                changed = true;
            }
        }
    }

    @Override
    public void putShort(short value) {
        if (newBase != null) {
            newBase.putShort(value);
        }

        if (base == null) {
            message.putShort(value);
            changed = true;
        } else {
            short baseValue = base.getShort();
            if (baseValue == value) {
                deltaBits.set(deltaBitIndex++, false);
            } else {
                deltaBits.set(deltaBitIndex++, true);
                message.putShort(value);
                changed = true;
            }
        }
    }

    @Override
    public void putInt(int value) {
        if (newBase != null) {
            newBase.putInt(value);
        }

        if (base == null) {
            message.putInt(value);
            changed = true;
        } else {
            int baseValue = base.getInt();
            if (baseValue == value) {
                deltaBits.set(deltaBitIndex++, false);
            } else {
                deltaBits.set(deltaBitIndex++, true);
                message.putInt(value);
                changed = true;
            }
        }
    }

    @Override
    public void putLong(long value) {
        if (newBase != null) {
            newBase.putLong(value);
        }

        if (base == null) {
            message.putLong(value);
            changed = true;
        } else {
            long baseValue = base.getLong();
            if (baseValue == value) {
                deltaBits.set(deltaBitIndex++, false);
            } else {
                deltaBits.set(deltaBitIndex++, true);
                message.putLong(value);
                changed = true;
            }
        }
    }

    @Override
    public void putFloat(float value) {
        if (newBase != null) {
            newBase.putFloat(value);
        }

        if (base == null) {
            message.putFloat(value);
            changed = true;
        } else {
            float baseValue = base.getFloat();
            if (baseValue == value) {
                deltaBits.set(deltaBitIndex++, false);
            } else {
                deltaBits.set(deltaBitIndex++, true);
                message.putFloat(value);
                changed = true;
            }
        }
    }

    @Override
    public void putDouble(double value) {
        if (newBase != null) {
            newBase.putDouble(value);
        }

        if (base == null) {
            message.putDouble(value);
            changed = true;
        } else {
            double baseValue = base.getDouble();
            if (baseValue == value) {
                deltaBits.set(deltaBitIndex++, false);
            } else {
                deltaBits.set(deltaBitIndex++, true);
                message.putDouble(value);
                changed = true;
            }
        }
    }

    @Override
    public void putEnum(Enum value) {
        putInt(value.ordinal());
    }

    @Override
    public void putString(String value) {
        putString(value, value.length());
    }

    @Override
    public void putString(String value, int maxLength) {
        if (newBase != null) {
            newBase.putString(value, maxLength);
        }

        if (base == null) {
            message.putString(value, maxLength);
            changed = true;
        } else {
            String baseValue = base.getString();
            String capValue = value;
            if (capValue.length() > maxLength) {
                capValue = value.substring(0, maxLength);
            }
            if (baseValue.equals(capValue)) {
                deltaBits.set(deltaBitIndex++, false);
            } else {
                deltaBits.set(deltaBitIndex++, true);
                message.putString(capValue, maxLength);
                changed = true;
            }
        }
    }

    @Override
    public void prepareReading(int messageSize, int deltaSize) {
        deltaBuffer.limit(deltaSize);
        message.prepareReading(messageSize, 0);
    }

    @Override
    public void prepareWriting() {
        deltaBuffer.clear();
        deltaBuffer.put(deltaBits.toByteArray());
        deltaBuffer.flip();
        message.prepareWriting();
    }

    @Override
    public void readFromSocket(SocketChannel channel) throws IOException {
        message.readFromSocket(channel);

        while (deltaBuffer.hasRemaining()) {
            channel.read(deltaBuffer);
        }

        deltaBuffer.flip();
        deltaBits = BitSet.valueOf(deltaBuffer);
    }

    @Override
    public void writeToSocket(SocketChannel channel) throws IOException {
        message.writeToSocket(channel);

        while (deltaBuffer.hasRemaining()) {
            channel.write(deltaBuffer);
        }
    }
}
