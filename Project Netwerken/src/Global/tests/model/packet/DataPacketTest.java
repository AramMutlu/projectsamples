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

public class DataPacketTest {
    private DataPacket dataPacket;

    @Before
    public void setup() {
        NewMessage message = new NewMessage(1, new VectorClock(10), new Alarm(BaseAlarm.AlarmCode.SOS,1, "Help, ik ben gewond!"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            message.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataPacket = new DataPacket(out.toByteArray(), 20);
    }

    @Test
    public void getParts() {
        try{
            ArrayList<PacketPart> packetParts = dataPacket.getPacketParts();
            ArrayList<byte[]> parts = new ArrayList<>();
            for (PacketPart part : packetParts) {
                byte[] tobyteArray = part.write();
                parts.add(tobyteArray);
                assertTrue(tobyteArray.length <= 20);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}