package com.example.loraapp.global.model.command;

import com.example.loraapp.global.utils.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Request Message(s) when your vectorclock is behind
 */
public class RequestMessage extends Command {
    private static int TYPE = 4;
    private int destinationId; // Used to identify the creator of the command
    private ArrayList<Integer> messageIds;

    public RequestMessage(ByteBuffer buffer){
        super(TYPE, buffer);
        destinationId = VarInt.readUnsignedVarInt(buffer);
        messageIds = new ArrayList<>();
        while (buffer.hasRemaining()){
            messageIds.add(VarInt.readUnsignedVarInt(buffer));
        }
    }
    /**
     *
     * @param destinationId the vectorclock index/nodeId of the messages that you are missing
     * @param messageIds the id('s) of message(s) that you are missing
     */
    public RequestMessage(int nodeId, VectorClock vectorClock, int destinationId, ArrayList<Integer> messageIds) {
        super(TYPE, nodeId, vectorClock);
        this.destinationId = destinationId;
        this.messageIds = messageIds;
    }

    /**
     * @return the vectorclock index/nodeId of the messages that you are missing
     */
    public int getDestinationId() {
        return destinationId;
    }

    /**
     * @return the id('s) of message(s) that you are missing
     */
    public ArrayList<Integer> getMessageIds() {
        return messageIds;
    }

    @Override
    public void write(ByteArrayOutputStream out) throws IOException {
        super.write(out);
        out.write(VarInt.writeUnsignedVarInt(destinationId));
        for (int id : messageIds) {
            out.write(VarInt.writeUnsignedVarInt(id));
        }
    }
}
