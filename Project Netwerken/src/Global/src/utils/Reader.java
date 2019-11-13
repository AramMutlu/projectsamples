package utils;

import model.command.*;
import model.packet.PacketPart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class Reader {
    public HashMap<Long, PacketPart[]> packetParts;

    public Reader() {
        this.packetParts = new HashMap<>();
    }

    public Command readCommand(byte[] bytes){
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int type = VarInt.readUnsignedVarInt(buffer);
        switch (type){
            case 1:
                return new IDRequest(buffer);
            case 2:
                return new IDAssign(buffer);
            case 3:
                return new NewMessage(buffer);
            case 4:
                return new RequestMessage(buffer);
            default:
                throw new IllegalArgumentException("Unkown command type!");
        }
    }

    public Command readPart(byte[] bytes){
        PacketPart thisPart = new PacketPart(bytes);
        long id = thisPart.getId();
        PacketPart[] currentParts;
        if(!packetParts.containsKey(id)){
            currentParts = new PacketPart[thisPart.getTotal()];
            currentParts[thisPart.getCurrentPart()-1] = thisPart;
            packetParts.put(thisPart.getId(), currentParts);
        } else {
            currentParts = packetParts.get(id);
            currentParts[thisPart.getCurrentPart()-1] = thisPart;
            packetParts.put(id, currentParts);
        }


        //last part
        if(isComplete(currentParts)){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            for (PacketPart part : currentParts) {
                try {
                    out.write(part.getCommandPart());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            packetParts.remove(thisPart.getId());
            return readCommand(out.toByteArray());
        }
        return null;
    }
    private boolean isComplete(PacketPart[] packetParts){
        for (PacketPart part : packetParts) {
            if(part == null){
                return false;
            }
        }
        return true;
    }


}
