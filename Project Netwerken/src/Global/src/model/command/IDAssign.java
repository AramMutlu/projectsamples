package model.command;

import utils.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * You will receive this data from the server.
 */
public class IDAssign extends Command {
    private static int TYPE = 2;
    private long random;
    private int id;

    public IDAssign(ByteBuffer buffer) {
        super(TYPE, buffer);
        random = VarInt.readUnsignedVarLong(buffer);
        id = VarInt.readUnsignedVarInt(buffer);
    }

    /**
     * @param random unique id of node
     * @param id index of vector clock/nodeId
     */
    public IDAssign(int nodeId, VectorClock vectorClock, long random, int id) {
        super(TYPE, nodeId, vectorClock);
        assert nodeId != 0;
        assert random >= 0;
        assert id != 0;
        this.random = random;
        this.id = id;
    }

    /**
     * @return unique id of node
     */
    public long getRandom() {
        return random;
    }

    /**
     * @return index of vector clock
     */
    public int getId() {
        return id;
    }

    @Override
    public void write(ByteArrayOutputStream out) throws IOException {
        super.write(out);
        VarInt.writeUnsignedVarLong(random, out);
        out.write(VarInt.writeUnsignedVarInt(id));
    }
}
