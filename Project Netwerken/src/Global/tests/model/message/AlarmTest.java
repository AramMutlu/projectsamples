package model.message;

import model.command.RequestMessage;
import org.junit.Before;
import org.junit.Test;
import utils.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class AlarmTest {
    private Alarm alarm;
    @Before
    public void setup() {
        alarm = new Alarm(BaseAlarm.AlarmCode.SOS, 1, "Help, ik ben gewond!");
    }
    @Test
    public void getPriority() {
        assertEquals(1, alarm.getPriority());
    }

    @Test
    public void getText() {
        assertEquals("Help, ik ben gewond!", alarm.getText());
    }

    @Test
    public void write() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            alarm.write(out);
            int size = out.size();
            assertTrue(size > 0);

            assertEquals(size, out.toByteArray().length);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void read() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            alarm.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //read
        ByteBuffer buffer = ByteBuffer.wrap(out.toByteArray());
        int alarmcode = VarInt.readUnsignedVarInt(buffer);
        Alarm read = new Alarm(alarmcode, buffer);
        assertEquals(alarm.getAlarmCode(), read.getAlarmCode());
        assertEquals(alarm.getDateTime(), read.getDateTime());
        assertEquals(alarm.getPriority(), read.getPriority());
        assertEquals(alarm.getText(), read.getText());
    }
}