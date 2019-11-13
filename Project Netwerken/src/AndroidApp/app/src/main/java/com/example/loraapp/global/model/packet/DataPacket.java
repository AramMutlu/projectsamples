package com.example.loraapp.global.model.packet;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Packet that will be sent with the encrypted command.
 */
public class DataPacket {
    private long id = -1;
    private byte[] encryptedCommand;
    private ArrayList<PacketPart> packetParts;

    public DataPacket(ArrayList<byte[]> receivedParts){
        packetParts = new ArrayList<>();
        for (byte[] part : receivedParts) {
            PacketPart packetPart = new PacketPart(part);
            assert packetPart.getTotal() == receivedParts.size()+1 : "Total size is not right!";
        }
    }

    /**
     * @param command command to send
     */
    public DataPacket(byte[] command, int packetByteSize) {
        this.id = getId();
        //TODO: encrypt here
        this.encryptedCommand = command;
        this.packetParts = createParts(packetByteSize);

    }

    /**
     * Creates packetParts that uses PacketSize for size.
     * For each part 13bytes are reserved for the PacketPart header
     * @return ArrayList of packetParts
     */
    private ArrayList<PacketPart> createParts(int packetByteSize) {
        ArrayList<PacketPart> packetParts = new ArrayList<>();
        ArrayList<byte[]> byteParts = splitByteArray(encryptedCommand, (packetByteSize-13));
        for (int i = 0; i < byteParts.size(); i++) {
            packetParts.add(new PacketPart(id, i + 1, byteParts.size(), byteParts.get(i)));
        }
        return packetParts;
    }

    /**
     * splits byte[] in multiple parts
     *
     * @param source   that will be split
     * @param partSize size of parts
     * @return parts of source in ArrayList
     */
    private static ArrayList<byte[]> splitByteArray(byte[] source, int partSize) {
        ArrayList<byte[]> result = new ArrayList<byte[]>();
        int start = 0;
        while (start < source.length) {
            int end = Math.min(source.length, start + partSize);
            result.add(Arrays.copyOfRange(source, start, end));
            start += partSize;
        }
        return result;
    }

    public ArrayList<PacketPart> getPacketParts() {
        return packetParts;
    }

    /**
     * @return temp id
     */
    public long getId() {
        if(id == -1){
            id = (long) (Math.random() * Long.MAX_VALUE + 1);
            assert id >= 0;
        }
        return id;
    }

    public byte[] getEncryptedCommand() {
        return encryptedCommand;
    }

}
