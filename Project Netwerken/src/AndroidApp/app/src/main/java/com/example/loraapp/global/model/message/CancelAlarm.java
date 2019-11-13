package com.example.loraapp.global.model.message;

import com.example.loraapp.global.utils.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The CancelAlarm is used to cancel an existing alarm
 */
public class CancelAlarm extends BaseAlarm {
    private int cancelID;

    public CancelAlarm(int alarmCode, ByteBuffer buffer){
        super(alarmCode, buffer);
        cancelID = VarInt.readUnsignedVarInt(buffer);
    }

    public CancelAlarm(int cancelID) {
        super(AlarmCode.DELETE);
        this.cancelID = cancelID;
    }

    public int getCancelID() {
        return cancelID;
    }

    @Override
    public void write(ByteArrayOutputStream out) throws IOException {
        super.write(out);
        out.write(VarInt.writeUnsignedVarInt(cancelID));
    }
}
