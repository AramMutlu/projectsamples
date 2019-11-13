package model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Victor on 20-3-2018.
 */

public class Command {
    private int id;
    public static final int GETID = 1;
    public static final int SETID = 2;
    public static final int NEW_MESSAGE = 3;


    public ArrayList<PacketPart> createParts(int partSize, byte[] encryptedMessage){
        int amount = (encryptedMessage.length/partSize)+1;
        ArrayList<PacketPart> packetParts = new ArrayList<>();
        ArrayList<byte[]> byteParts = splitByteArray(encryptedMessage, partSize);
        for (int i = 0; i < amount; i++){
            packetParts.add(new PacketPart(id, i, amount, byteParts.get(i)));
        }
        return packetParts;
    }
    public static ArrayList<byte[]> splitByteArray(byte[] source, int partSize) {
        ArrayList<byte[]> result = new ArrayList<byte[]>();
        int start = 0;
        while (start < source.length) {
            int end = Math.min(source.length, start + partSize);
            result.add(Arrays.copyOfRange(source, start, end));
            start += partSize;
        }
        return result;
    }
}
