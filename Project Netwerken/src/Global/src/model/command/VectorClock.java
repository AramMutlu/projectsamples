package model.command;

import utils.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * With this vectorclock you can check if you have received the last message from every node.
 */
public class VectorClock {
    private int[] array;

    public VectorClock(ByteBuffer in) {
        int size = VarInt.readUnsignedVarInt(in);
        array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = VarInt.readUnsignedVarInt(in);
        }
    }

    public VectorClock(int size) {
        assert size > 1: "size must be bigger than one";
        this.array = new int[size];
    }

    public int[] getArray() {
        return array;
    }

    public int getSize() {
        return array.length;
    }

    public int incrementVector(int nodeId){
        return ++array[nodeId];
    }

    public void write(ByteArrayOutputStream out) throws IOException {
        out.write(VarInt.writeUnsignedVarInt(array.length));
        for (int vector : array) {
            out.write(VarInt.writeUnsignedVarInt(vector));
        }
    }
}
