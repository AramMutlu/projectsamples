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

public class IDAssignTest {
    private IDRequest idRequest;
    private long random;
    private IDAssign idAssign;
    @Before
    public void setup() {
        idRequest = new IDRequest(new VectorClock(10));
        random = idRequest.getRandom();
        idAssign = new IDAssign(2,new VectorClock(10), random,1);
        assertNotNull(idAssign);
        assertEquals(2, idAssign.getAuthorId());
    }

    @Test
    public void getRandom() {
        assertEquals(idAssign.getRandom(), random);
    }

    @Test
    public void getId() {
        assertEquals(idAssign.getId(), 1);
    }

    @Test
    public void write() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            idAssign.write(out);
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
            idAssign.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //read
        Reader reader = new Reader();
        IDAssign read = (IDAssign) reader.readCommand(out.toByteArray());
        assertEquals(idAssign.getType(), read.getType());
        assertEquals(idAssign.getAuthorId(), read.getAuthorId());
        assertTrue(Arrays.equals(idAssign.getVectorClock().getArray(), read.getVectorClock().getArray()));
        assertEquals(idAssign.getRandom(),read.getRandom());
        assertEquals(idAssign.getId(), read.getId());
    }


    //Bad weather

    @Test(expected = AssertionError.class)
    public void testWriteWithNull(){
        try {
            idAssign.write(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = AssertionError.class)
    public void testConstructorWithNullVectorClock(){
        IDAssign id = new IDAssign(0, null, random, 1);
    }
    @Test(expected = AssertionError.class)
    public void testConstructorWithNegativeRandom(){
        IDAssign id = new IDAssign(0, idAssign.getVectorClock(), -1, 1);
    }
    @Test(expected = AssertionError.class)
    public void testConstructorWithZeroId(){
        IDAssign id = new IDAssign(0, idAssign.getVectorClock(), random, 0);
    }
}