package model.command;

import org.junit.Before;
import org.junit.Test;
import utils.Reader;
import utils.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class RequestMessageTest {
    private RequestMessage requestMessage;
    private VectorClock vectorClock;
    private ArrayList<Integer> missingIds;
    @Before
    public void setup() {
        vectorClock  = new VectorClock(10);
        missingIds = new ArrayList<>();
        missingIds.add(1);
        missingIds.add(2);
        missingIds.add(3);
        requestMessage = new RequestMessage(3, vectorClock, 1, missingIds);

    }

    @Test
    public void getDestinationId() {
        assertEquals(1, requestMessage.getDestinationId());
    }

    @Test
    public void getMessageIds() {
        assertEquals(missingIds, requestMessage.getMessageIds());
    }

    @Test
    public void write() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            requestMessage.write(out);
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
            requestMessage.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Reader reader = new Reader();
        RequestMessage read = (RequestMessage) reader.readCommand(out.toByteArray());
        assertEquals(requestMessage.getType(), read.getType());
        assertEquals(requestMessage.getAuthorId(), read.getAuthorId());
        assertTrue(Arrays.equals(requestMessage.getVectorClock().getArray(), read.getVectorClock().getArray()));
        assertEquals(requestMessage.getDestinationId(), read.getDestinationId());
        assertEquals(requestMessage.getMessageIds(), read.getMessageIds());
    }

    //Bad weather

    @Test(expected = AssertionError.class)
    public void testWriteWithNull(){
        try {
            requestMessage.write(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}