package com.example.loraapp.global.model.command;

import com.example.loraapp.global.model.message.Alarm;
import com.example.loraapp.global.model.message.CancelAlarm;
import com.example.loraapp.global.utils.VarInt;
import com.example.loraapp.global.model.message.BaseAlarm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * New NewMessage or CancelAlarm
 */
public class NewMessage extends Command {
    private static int TYPE = 3;
    private int authorId; // Used to identify the creator of the command
    private int messageId;
    private BaseAlarm alarm;

    public NewMessage(ByteBuffer buffer){
        super(TYPE, buffer);
        authorId = VarInt.readUnsignedVarInt(buffer);
        messageId = VarInt.readUnsignedVarInt(buffer);
        int alarmcode = VarInt.readUnsignedVarInt(buffer);
        if(alarmcode == 99){
            alarm = new CancelAlarm(alarmcode, buffer);
        } else {
            alarm = new Alarm(alarmcode, buffer);
        }
    }

    /**
     * If you want to send a complete new message
     * @param authorId
     * @param vectorClock
     * @param baseAlarm
     */
    public NewMessage(int authorId, VectorClock vectorClock, BaseAlarm baseAlarm) {
        super(TYPE, authorId, vectorClock);
        assert authorId != 0;
        assert baseAlarm != null : "baseAlarm is null.";
        this.authorId = authorId;
        this.messageId = super.getVectorClock().incrementVector(authorId);
        this.alarm = baseAlarm;
    }

    /**
     * If you want to resend a message
     * @param nodeId
     * @param authorId
     * @param messageId
     * @param vectorClock
     * @param baseAlarm
     */
    public NewMessage(int nodeId, int authorId, int messageId, VectorClock vectorClock, BaseAlarm baseAlarm) {
        super(TYPE, nodeId, vectorClock);
        assert authorId != 0;
        assert baseAlarm != null : "baseAlarm is null.";
        this.authorId = authorId;
        this.messageId = messageId;
        this.alarm = baseAlarm;
    }

    /**
     * @return your index of vectorclock
     */
    public int getAuthorId() {
        return authorId;
    }

    public BaseAlarm getAlarm(){
        return alarm;
    }

    /**
     * @return current vectorclock value
     */
    public int getMessageId() {
        return messageId;
    }

    @Override
    public void write(ByteArrayOutputStream out) throws IOException {
        super.write(out);
        out.write(VarInt.writeUnsignedVarInt(authorId));
        out.write(VarInt.writeUnsignedVarInt(messageId));
        alarm.write(out);
    }
}
