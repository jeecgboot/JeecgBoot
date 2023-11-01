package org.jeecg.modules.im.io.agora.media;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Li on 10/1/2016.
 */
public class ByteBuf {
    ByteBuffer buffer = ByteBuffer.allocate(1024).order(ByteOrder.LITTLE_ENDIAN);

    public ByteBuf() {
    }

    public ByteBuf(byte[] bytes) {
        this.buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
    }

    public byte[] asBytes() {
        byte[] out = new byte[buffer.position()];
        buffer.rewind();
        buffer.get(out, 0, out.length);
        return out;
    }

    // packUint16
    public ByteBuf put(short v) {
        buffer.putShort(v);
        return this;
    }

    public ByteBuf put(byte[] v) {
        put((short)v.length);
        buffer.put(v);
        return this;
    }

    // packUint32
    public ByteBuf put(int v) {
        buffer.putInt(v);
        return this;
    }

    public ByteBuf put(long v) {
        buffer.putLong(v);
        return this;
    }

    public ByteBuf put(String v) {
        return put(v.getBytes());
    }

    public ByteBuf put(TreeMap<Short, String> extra) {
        put((short)extra.size());

        for (Map.Entry<Short, String> pair : extra.entrySet()) {
            put(pair.getKey());
            put(pair.getValue());
        }

        return this;
    }

    public ByteBuf putIntMap(TreeMap<Short, Integer> extra) {
        put((short)extra.size());

        for (Map.Entry<Short, Integer> pair : extra.entrySet()) {
            put(pair.getKey());
            put(pair.getValue());
        }

        return this;
    }

    public short readShort() {
        return buffer.getShort();
    }


    public int readInt() {
        return buffer.getInt();
    }

    public byte[] readBytes() {
        short length = readShort();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }

    public String readString() {
        byte[] bytes = readBytes();
        return new String(bytes);
    }

    public TreeMap readMap() {
        TreeMap<Short, String> map = new TreeMap<>();

        short length = readShort();

        for (short i = 0; i < length; ++i) {
            short k = readShort();
            String v = readString();
            map.put(k, v);
        }

        return map;
    }

    public TreeMap<Short, Integer> readIntMap() {
        TreeMap<Short, Integer> map = new TreeMap<>();

        short length = readShort();

        for (short i = 0; i < length; ++i) {
            short k = readShort();
            Integer v = readInt();
            map.put(k, v);
        }

        return map;
    }
}
