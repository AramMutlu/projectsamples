package com.example.loraapp.global.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

public class VarInt {
    private static int MSB = 0x80;
    private static int RST = 0x7F;

    /**
     * Encodes a value using the variable-length encoding from
     * <a href="http://code.google.com/apis/protocolbuffers/docs/encoding.html">
     * Google Protocol Buffers</a>. Zig-zag is not used, so input must not be negative.
     * If values can be negative, use {writeSignedVarLong(long, DataOutput)}
     * instead. This method treats negative input as like a large unsigned value.
     *
     * @param value value to encode
     * @param out   to write bytes to
     */
    public static void writeUnsignedVarLong(long value, ByteArrayOutputStream out) {
        assert out != null : "ByteArrayOutputStream is null.";
        while ((value & 0xFFFFFFFFFFFFFF80L) != 0L) {
            out.write(((int) value & RST) | MSB);
            value >>>= 7;
        }
        out.write((int) value & RST);
    }

    /**
     * @return decode value
     * @throws IllegalArgumentException if variable-length value does not terminate
     *                                  after 9 bytes have been read
     */
    public static long readUnsignedVarLong(ByteBuffer buffer) throws IllegalArgumentException {
        assert buffer != null : "ByteBuffer is null.";
        long value = 0L;
        int i = 0;
        long b;
        while (((b = buffer.get()) & MSB) != 0) {
            value |= (b & RST) << i;
            i += 7;
            if (i > 63) {
                throw new IllegalArgumentException("Variable length quantity is too long");
            }
        }
        return value | (b << i);
    }

    /**
     * <p/>
     * This one does not use streams and is much faster.
     * Makes a single object each time, and that object is a primitive array.
     */
    public static byte[] writeUnsignedVarInt(int value) {
        byte[] byteArrayList = new byte[10];
        int i = 0;
        while ((value & 0xFFFFFF80) != 0L) {
            byteArrayList[i++] = ((byte) ((value & RST) | MSB));
            value >>>= 7;
        }

        byteArrayList[i] = ((byte) (value & RST));
        byte[] out = new byte[i + 1];
        for (; i >= 0; i--) {
            out[i] = byteArrayList[i];
        }

        return out;
    }

    /**
     * @throws IllegalArgumentException if variable-length value does not terminate
     *                                  after 5 bytes have been read
     */
    public static int readUnsignedVarInt(ByteBuffer buffer) throws IllegalArgumentException {
        assert buffer != null : "ByteBuffer is null.";
        int value = 0;
        int i = 0;
        int b;
        while (((b = buffer.get()) & MSB) != 0) {
            value |= (b & RST) << i;
            i += 7;
            if (i > 35) {
                throw new IllegalArgumentException("Variable length quantity is too long");
            }
        }
        return value | (b << i);
    }

}