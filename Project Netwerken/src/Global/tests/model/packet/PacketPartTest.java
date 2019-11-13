package model.packet;

import model.command.NewMessage;
import model.command.VectorClock;
import model.message.Alarm;
import model.message.BaseAlarm;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class PacketPartTest {
    private PacketPart packetPart;
    private ArrayList<PacketPart> packetParts;
    @Before
    public void setup() {
        NewMessage message = new NewMessage(1, new VectorClock(10), new Alarm(BaseAlarm.AlarmCode.SOS,1, "Help, ik ben gewond!"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            message.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataPacket dataPacket = new DataPacket(out.toByteArray(), 20);
        packetParts = dataPacket.getPacketParts();
        packetPart = packetParts.get(0);
    }

    @Test
    public void getCurrentPart() {
        assertEquals(packetPart.getCurrentPart(), 1);
    }

    @Test
    public void getTotal() {
        assertEquals(packetPart.getTotal(), packetParts.size());
    }

    @Test
    public void getPart() {
        assertTrue(packetPart.getCommandPart().length <= 20);
    }
}