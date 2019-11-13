package com.example.loraapp.global.model.packet;

import com.example.loraapp.global.utils.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * PacketPart to send the packet in parts when needed.
 */
public class PacketPart {
    private long id = -1;
    private int currentPart;
    private int total;
    private byte[] commandPart;

    public PacketPart(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        this.id = VarInt.readUnsignedVarLong(buffer);
        this.currentPart = VarInt.readUnsignedVarInt(buffer);
        this.total = VarInt.readUnsignedVarInt(buffer);
        byte[] fullArray = buffer.array();
        int offset = buffer.position();
        this.commandPart = Arrays.copyOfRange(fullArray,offset, fullArray.length);
    }
    /**
     * @param id serie id of parts
     * @param currentPart current commandPart number of total
     * @param total number of parts
     * @param commandPart commandPart of encryptedCommand
     */
    public PacketPart(long id, int currentPart, int total, byte[] commandPart) {
        this.id = id;
        this.currentPart = currentPart;
        this.total = total;
        this.commandPart = commandPart;
    }

    public long getId() {
        return id;
    }

    /**
     * @return current commandPart number of total
     */
    public int getCurrentPart() {
        return currentPart;
    }

    /**
     * @return number of parts
     */
    public int getTotal() {
        return total;
    }

    /**
     * @return commandPart of encryptedCommand
     */
    public byte[] getCommandPart() {
        return commandPart;
    }

    public byte[] write() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        VarInt.writeUnsignedVarLong(id, out);
        out.write(VarInt.writeUnsignedVarInt(currentPart));
        out.write(VarInt.writeUnsignedVarInt(total));
        out.write(commandPart);
        return out.toByteArray();
    }
}
