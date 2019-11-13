package model.message;

import org.junit.Before;
import org.junit.Test;
import utils.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class CancelAlarmTest {
    CancelAlarm cancelAlarm;
    @Before
    public void setup(){
        cancelAlarm = new CancelAlarm(1);
    }
    @Test
    public void getCancelID() {
        assertEquals(1, cancelAlarm.getCancelID());
    }

    @Test
    public void write() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            cancelAlarm.write(out);
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
            cancelAlarm.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //read
        ByteBuffer buffer = ByteBuffer.wrap(out.toByteArray());
        int alarmcode = VarInt.readUnsignedVarInt(buffer);
        CancelAlarm read = new CancelAlarm(alarmcode, buffer);
        assertEquals(cancelAlarm.getAlarmCode(), read.getAlarmCode());
        assertEquals(cancelAlarm.getDateTime(), read.getDateTime());
        assertEquals(cancelAlarm.getCancelID(), read.getCancelID());
    }
}