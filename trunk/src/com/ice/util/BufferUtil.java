package com.ice.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static java.nio.ByteBuffer.allocateDirect;

/**
 * User: jason
 * Date: 13-2-5
 */
public class BufferUtil {

    public static FloatBuffer wrap(float... data) {
        ByteBuffer byteBuffer = allocateDirect(data.length * Float.SIZE / Byte.SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(data);
        floatBuffer.position(0);
        return floatBuffer;
    }

    public static ByteBuffer wrap(byte... data) {
        ByteBuffer byteBuffer = allocateDirect(data.length);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.put(data);
        byteBuffer.position(0);
        return byteBuffer;
    }

    public static IntBuffer intBuffer(int size) {
        ByteBuffer byteBuffer = allocateDirect(size * Integer.SIZE / Byte.SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        return byteBuffer.asIntBuffer();
    }

    public static FloatBuffer floatBuffer(int size) {
        ByteBuffer byteBuffer = allocateDirect(size * Float.SIZE / Byte.SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        return byteBuffer.asFloatBuffer();
    }

}
