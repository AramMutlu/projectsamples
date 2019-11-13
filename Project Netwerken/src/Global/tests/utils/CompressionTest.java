package utils;

import model.command.NewMessage;
import model.command.VectorClock;
import model.message.Alarm;
import model.message.BaseAlarm;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CompressionTest {
    private byte[] commandToCompress;
    private byte[] ASCIItoCompress;

    @Before
    public void setup() {
        NewMessage message = new NewMessage(1, new VectorClock(10), new Alarm(BaseAlarm.AlarmCode.SOS,1, "Help, ik ben gewond!"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            message.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        commandToCompress = out.toByteArray();

        String text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.";
        try {
            ASCIItoCompress = text.getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void compressText() {
        byte[] compressed = Compression.compress(ASCIItoCompress);
        assertTrue(ASCIItoCompress.length > compressed.length);
    }

    @Test(expected = AssertionError.class)
    public void compressCommand() {
        byte[] compressed = Compression.compress(commandToCompress);
        assertTrue(commandToCompress.length > compressed.length);
    }

    @Test
    public void decompress() {
        byte[] compressed = Compression.compress(commandToCompress);
        byte[] decompressed = Compression.decompress(compressed);
        assertTrue(Arrays.equals(commandToCompress, decompressed));
    }
}