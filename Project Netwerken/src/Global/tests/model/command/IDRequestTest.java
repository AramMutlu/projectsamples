package model.command;

import org.junit.Before;
import org.junit.Test;
import utils.Reader;
import utils.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.*;

public class IDRequestTest {
    private IDRequest idRequest;

    @Before
    public void setup() {
        VectorClock vectorClock = new VectorClock(10);
        idRequest = new IDRequest(vectorClock);
        assertNotNull(idRequest);
        assertEquals(0, idRequest.getAuthorId());
        assertEquals(vectorClock, idRequest.getVectorClock());
    }

    @Test
    public void getNodeId() {
        assertEquals(idRequest.getAuthorId(), 0);
    }


    @Test
    public void write() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            idRequest.write(out);
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
            idRequest.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //read
        Reader reader = new Reader();
        IDRequest read = (IDRequest) reader.readCommand(out.toByteArray());
        assertEquals(idRequest.getType(), read.getType());
        assertEquals(idRequest.getAuthorId(), read.getAuthorId());
        assertTrue(Arrays.equals(idRequest.getVectorClock().getArray(), read.getVectorClock().getArray()));
        assertEquals(idRequest.getRandom(),read.getRandom());
    }


    //Bad weather

    @Test(expected = AssertionError.class)
    public void testWriteWithNull(){
        try {
            idRequest.write(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AssertionError.class)
    public void testConstructorWithOneVectorClock(){
        IDRequest id = new IDRequest(new VectorClock(1));
    }
}