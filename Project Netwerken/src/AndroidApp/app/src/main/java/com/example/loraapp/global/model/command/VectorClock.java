package com.example.loraapp.global.model.command;

import android.support.annotation.NonNull;

import com.example.loraapp.global.utils.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

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

    public Integer[][] getMissingIds(VectorClock vectorClock){
        Integer[][] result = new Integer[this.array.length][];
        boolean isEmpty = true;
        for(int i = 0; i < array.length; i++){
            int length = (vectorClock.array[i] - this.array[i]-1);
            if(length > 0){
                isEmpty = false;
                int currentId = this.array[i];
                Integer[] missingIds = new Integer[length];
                for (int j = 0; j < missingIds.length; j++){
                    missingIds[j] = ++currentId;
                }
                result[i] = missingIds;
            }
        }
        if(isEmpty){
            return new Integer[0][];
        } else {
            return result;
        }
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
