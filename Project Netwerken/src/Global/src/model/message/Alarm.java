package model.message;

import utils.VarInt;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Alarm extends BaseAlarm {
    private int priority;
    private String text;

    public Alarm(int alarmcode, ByteBuffer buffer){
        super(alarmcode, buffer);
        priority = VarInt.readUnsignedVarInt(buffer);
        byte[] fullArray = buffer.array();
        int offset = buffer.position();
        byte[] stringPart = Arrays.copyOfRange(fullArray,offset, fullArray.length);
        try {
            text = new String(stringPart, "ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public Alarm(AlarmCode alarmCode, int priority, String text) {
        super(alarmCode);
        assert alarmCode != AlarmCode.DELETE : "Wrong Alarmcode";
        this.text = text;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public String getText() {
        return text;
    }

    @Override
    public void write(ByteArrayOutputStream out) throws IOException {
        super.write(out);
        out.write(VarInt.writeUnsignedVarInt(priority));
        out.write(text.getBytes("ASCII"));
    }
}
