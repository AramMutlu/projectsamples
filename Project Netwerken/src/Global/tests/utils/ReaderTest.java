package utils;

import model.command.Command;
import model.command.NewMessage;
import model.command.VectorClock;
import model.message.Alarm;
import model.message.BaseAlarm;
import model.packet.DataPacket;
import model.packet.PacketPart;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ReaderTest {
    private Reader reader;
    private NewMessage message1;
    private Alarm alarm1;
    private NewMessage message2;
    private ArrayList<byte[]> receivedParts;
    @Before
    public void setup() {
        alarm1 =new Alarm(BaseAlarm.AlarmCode.SOS,1, "Help, ik ben gewond!");
        message1 = new NewMessage(1, new VectorClock(10), alarm1);
        message2 = new NewMessage(2, new VectorClock(10), alarm1);
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        try {
            message1.write(out1);
            message2.write(out2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<PacketPart> parts1 = new DataPacket(out1.toByteArray(), 20).getPacketParts();
        ArrayList<PacketPart> parts2 = new DataPacket(out2.toByteArray(), 20).getPacketParts();

        receivedParts = new ArrayList<>();
        for (int i = 0; i < parts1.size(); i++) {
            try {
                PacketPart part1 = parts1.get(i);
                receivedParts.add(part1.write());
                if(i < parts1.size()-1){
                    PacketPart part2 = parts2.get(i);
                    receivedParts.add(part2.write());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        reader = new Reader();
    }
    @Test
    public void readCommand() {

    }

    @Test
    public void readPart() {
        for (byte[] part : receivedParts) {
            Command complete = reader.readPart(part);
            if(complete != null){
                if(complete instanceof NewMessage){
                    NewMessage newMessage = (NewMessage) complete;
                    assertEquals(message1.getType(), newMessage.getType());
                    assertEquals(message1.getAuthorId(), newMessage.getAuthorId());
                    assertTrue(Arrays.equals(message1.getVectorClock().getArray(), newMessage.getVectorClock().getArray()));
                    assertEquals(message1.getAuthorId(),newMessage.getAuthorId());
                    assertEquals(message1.getMessageId(),newMessage.getMessageId());
                    Alarm readAlarm = (Alarm) newMessage.getAlarm();
                    assertEquals(alarm1.getAlarmCode(),readAlarm.getAlarmCode());
                    assertEquals(alarm1.getDateTime(),readAlarm.getDateTime());
                    assertEquals(alarm1.getPriority(),readAlarm.getPriority());
                    assertEquals(alarm1.getText(),readAlarm.getText());
                }
            }
        }
    }
}