package model.command;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.*;

public class VectorClockTest {
    private VectorClock vectorClock;
    @Before
    public void setup() {
        vectorClock = new VectorClock(10);
    }

    @Test
    public void getArray() {
        int[] array = vectorClock.getArray();
        assertEquals(10, array.length);
    }

    @Test
    public void getSize() {
        assertEquals(10, vectorClock.getSize());
    }

    @Test
    public void incrementVector() {
        assertEquals(0, vectorClock.getArray()[2]);
        int i = vectorClock.incrementVector(2);
        assertEquals(1, vectorClock.getArray()[2]);
        assertEquals(1, i);
    }

    @Test
    public void write() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            vectorClock.write(out);
            int size = out.size();
            assertTrue(size > 0);
            assertEquals(size, out.toByteArray().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void read() {
        vectorClock.incrementVector(3);
        vectorClock.incrementVector(3);
        vectorClock.incrementVector(2);
        //write
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            vectorClock.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //read
        ByteBuffer buffer = ByteBuffer.wrap(out.toByteArray());
        VectorClock read = new VectorClock(buffer);
        assertTrue(Arrays.equals(vectorClock.getArray(),read.getArray()));
        assertEquals(vectorClock.getSize(), read.getSize());
    }

}