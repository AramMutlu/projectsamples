package model.message;

import sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl;
import utils.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Instant;

public abstract class BaseAlarm {
    public enum AlarmCode {
        SOS(10),
        HELP(11),
        INTEL(12),
        ACTIVITY(13),
        INCIDENT(14),
        COMMAND(15),
        STATUS(16),
        REQUEST(17),
        DELETE(99);

        private final int value;

        AlarmCode(int value) {
            this.value = value;
        }
    }

    private AlarmCode alarmCode;
    private long dateTime;

    public BaseAlarm(int alarmcode, ByteBuffer buffer){
        try {
            this.alarmCode = getAlarmCodefromInt(alarmcode);
        } catch (IllegalArgumentException ex){
            ex.fillInStackTrace();
        }
        this.dateTime = VarInt.readUnsignedVarLong(buffer);
    }

    public BaseAlarm(AlarmCode alarmCode) {
        this.alarmCode = alarmCode;
        this.dateTime = Instant.now().getEpochSecond();
    }

    public static AlarmCode getAlarmCodefromInt(int value) throws IllegalArgumentException {
        for (AlarmCode alarmCode : AlarmCode.values()) {
            if (alarmCode.value == value) {
                return alarmCode;
            }
        }
        throw new IllegalArgumentException("Invalid AlarmCode: " + value);
    }

    public AlarmCode getAlarmCode() {
        return alarmCode;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void write(ByteArrayOutputStream out) throws IOException {
        out.write(VarInt.writeUnsignedVarInt(alarmCode.value));
        VarInt.writeUnsignedVarLong(dateTime, out);
    }
}