package model.command;

import model.message.Alarm;
import model.message.BaseAlarm;
import org.junit.Before;
import org.junit.Test;
import utils.Reader;
import utils.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NewMessageTest {
    private VectorClock vectorClock;
    private NewMessage message;
    private Alarm alarm;

    @Before
    public void setup() {
        vectorClock = new VectorClock(10);

        Alarm.AlarmCode alarmCode = BaseAlarm.AlarmCode.SOS;
        alarm = new Alarm(alarmCode,1, "Help, ik ben gewond!");
        message = new NewMessage(1, vectorClock, alarm);

    }

    @Test
    public void testGetNodeId(){
        assertNotNull(message.getAuthorId());
        assertEquals(message.getAuthorId(), 1);

    }

    @Test
    public void testGetMessageId(){
        assertNotNull(message.getMessageId());

        assertEquals(message.getMessageId(), 1);
    }

    @Test
    public void testWrite() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            message.write(out);
            int size = out.size();
            assertTrue(size > 0);

            assertEquals(size, out.toByteArray().length);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readAlarm(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            message.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //read
        Reader reader = new Reader();
        NewMessage read = (NewMessage) reader.readCommand(out.toByteArray());
        assertEquals(message.getType(), read.getType());
        assertEquals(message.getAuthorId(), read.getAuthorId());
        assertTrue(Arrays.equals(message.getVectorClock().getArray(), read.getVectorClock().getArray()));
        assertEquals(message.getAuthorId(),read.getAuthorId());
        assertEquals(message.getMessageId(),read.getMessageId());
        Alarm readAlarm = (Alarm) read.getAlarm();
        assertEquals(alarm.getAlarmCode(),readAlarm.getAlarmCode());
        assertEquals(alarm.getDateTime(),readAlarm.getDateTime());
        assertEquals(alarm.getPriority(),readAlarm.getPriority());
        assertEquals(alarm.getText(),readAlarm.getText());
    }
    //Bad weather

    @Test(expected = AssertionError.class)
    public void testWriteWithNull(){
        try {
            message.write(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AssertionError.class)
    public void testConstructorWithNullVectorClock(){
        NewMessage id = new NewMessage(0, null, message.getAlarm());
    }
    @Test(expected = AssertionError.class)
    public void testConstructorWithNullAlarm(){
        NewMessage id = new NewMessage(0, message.getVectorClock(), null);
    }
}