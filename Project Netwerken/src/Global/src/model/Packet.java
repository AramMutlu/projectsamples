package model;

import java.util.ArrayList;

/**
 * Created by Victor on 20-3-2018.
 */

public class Packet {
    private byte[] encryptedCommand;

    public Packet(byte[] encryptedCommand) {
        this.encryptedCommand = encryptedCommand;
    }

}
