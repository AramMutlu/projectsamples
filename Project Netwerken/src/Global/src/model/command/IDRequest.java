package model.command;

import utils.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * request vectorclock index/nodeId from server.
 */
public class IDRequest extends Command {
    private static int TYPE = 1;
    private long random = -1; //temp id

    public IDRequest(ByteBuffer buffer) {
        super(TYPE, buffer);
        random = VarInt.readUnsignedVarLong(buffer);
    }

    /**
     * request vectorclock index from server.
     */
    public IDRequest(VectorClock vectorClock) {
        super(TYPE, 0, vectorClock);
        this.random = getRandom();
    }

    /**
     * @return temp id
     */
    public long getRandom() {
        if(random == -1){
            random = (long) (Math.random() * Long.MAX_VALUE + 1);
            assert random >= 0;
        }
        return random;
    }

    @Override
    public void write(ByteArrayOutputStream out) throws IOException {
        super.write(out);
        VarInt.writeUnsignedVarLong(random, out);
    }
}
