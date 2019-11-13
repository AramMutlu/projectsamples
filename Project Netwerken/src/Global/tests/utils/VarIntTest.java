package utils;

import utils.VarInt;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class VarIntTest {

    @Test
    public void testVarInt() {
        VarInt varInt = new VarInt();
        assertNotNull(varInt);
    }


    @Test
    public void testWriteUnsignedVarInt() {
        byte[] bytes = VarInt.writeUnsignedVarInt(300);
        assertNotNull(bytes);
        assertNotEquals(0, bytes.length);
    }


    @Test
    public void testReadUnsignedVarInt() {
        byte[] bytes = VarInt.writeUnsignedVarInt(300);

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int number = VarInt.readUnsignedVarInt(buffer);
        assertEquals(300, number);
    }

    @Test
    public void testWriteUnsignedVarLong() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertNotNull(out);
        assertEquals(0, out.size());
        VarInt.writeUnsignedVarLong(999999999, out);
        assertNotEquals(0, out.size());
    }

    @Test
    public void testReadUnsignedVarLong(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        VarInt.writeUnsignedVarLong(999999999, out);

        ByteBuffer buffer = ByteBuffer.wrap(out.toByteArray());
        long number = VarInt.readUnsignedVarLong(buffer);
        assertEquals(999999999, number);
    }


    //Bad weather
    @Test(expected = AssertionError.class)
    public void testReadUnsignedVarIntWithNullByteArray() {
        VarInt.readUnsignedVarInt(null);
    }


    @Test(expected = AssertionError.class)
    public void testWriteUnsignedVarLongWithNullOutput() {
        VarInt.writeUnsignedVarLong(999999999, null);
    }

    @Test(expected = AssertionError.class)
    public void testReadUnsignedVarLongWithNullOutput(){
        VarInt.readUnsignedVarLong(null);
    }

}
