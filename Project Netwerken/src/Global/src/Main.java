import model.Command;
import model.PacketPart;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String string = "Dit is wel een hele lange string. Dat kan niet of toch wel?";
        System.out.println(string);
        byte[] toByte = string.getBytes();
        Command cmd = new Command();
        ArrayList<PacketPart> parts = cmd.createParts(5, toByte);
        System.out.println(parts.size());
    }
}
