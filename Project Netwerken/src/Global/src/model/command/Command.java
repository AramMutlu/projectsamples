package model.command;

import utils.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Commands that will be encrypted in the DataPacket. This is where the data is stored.
 */
public abstract class Command {
    private int type;
    private int nodeId; // Used for vector clock
    private VectorClock vectorClock;

    public Command(int type, ByteBuffer buffer){
        this.type = type;
        nodeId = VarInt.readUnsignedVarInt(buffer);
        vectorClock = new VectorClock(buffer);
    }

    /**
     * @param nodeId vectorclock index
     * @param vectorClock the new vectorclock
     */
    public Command(int type, int nodeId, VectorClock vectorClock) {
        assert vectorClock != null : "Vector clock is null.";
        this.type = type;
        this.nodeId = nodeId;
        this.vectorClock = vectorClock;
    }

    public int getAuthorId() {
        return nodeId;
    }

    public VectorClock getVectorClock() {
        return vectorClock;
    }

    public int getType() {
        return type;
    }
    /**
     * This method will translate the whole command to byte[].
     * @return byte[]
     */
    public void write(ByteArrayOutputStream out) throws IOException {
        assert out != null : "ByteArrayOutputStream is null.";
        out.write(VarInt.writeUnsignedVarInt(type));
        out.write(VarInt.writeUnsignedVarInt(nodeId));
        vectorClock.write(out);
    }
}
