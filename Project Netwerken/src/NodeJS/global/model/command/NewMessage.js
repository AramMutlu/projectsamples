const Command  = require('./Command');
const varint = require('../../utils/varint/varint');

class NewMessage extends Command {
    constructor(options) {
        super(options);
        if (options.buffer) {
            // constructor(buffer)
            this.authorId = varint.decode(options.buffer);
            this.messageId = varint.decode(options.messageId);
            this.alarm = varint.decode(options.alarm);
        } else if (options.nodeId) {
            // constructor(nodeId, authorId, messageId, vectorClock, baseAlarm)
            this.authorId = options.authorId;
            this.messageId = options.messageId;
            this.alarm = options.baseAlarm;
        } else {
            // constructor(authorId, vectorClock, baseAlarm)
            this.authorId = options.nodeId;
            this.alarm = options.baseAlarm;
        }
    }

/*    // OLD CONSTRUCTOR
    constructor(buffer) {
        super(buffer);
        this.authorId = varint.decode(buffer);
        this.messageId = varint.decode(buffer);
        this.alarm = varint.decode(buffer);
    }

    // OLD CONSTRUCTOR
    constructor(nodeId, vectorClock, baseAlarm) {
        super(3, nodeId, vectorClock);
        this.authorId = nodeId;
        //this.messageId = ; super.getVectorClock().incrementVector(authorId);
        this.alarm = baseAlarm;
    }

    // OLD CONSTRUCTOR
    constructor(nodeId, authorId, messageId, vectorClock, baseAlarm) {
        super(3, nodeId, vectorClock);
        this.authorId = authorId;
        this.messageId = messageId;
        this.alarm = baseAlarm;
    }*/

    write(buffer) {
        super.write(buffer);
        buffer.write(varint.encode(this.authorId));
        buffer.write(varint.encode(this.messageId));
        buffer.write(varint.encode(this.alarm));
    }

}