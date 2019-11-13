const Command  = require('./Command');
const varint = require('../../utils/varint/varint');

class RequestMessage extends Command {
    constructor(options) {
        if (options.buffer) {
            // constructor(buffer)
            super(options);
            this.destinationId = varint.decode(options.buffer);
            this.messageIds = varint.decode(options.buffer);
        } else {
            // constructor(nodeId, vectorClock, destinationId, messageIds)
            options.type = 4;
            super(options);
            this.destinationId = options.destinationId;
            this.messageIds = options.messageIds;
        }
    }

/*  // OLD CONSTRUCTOR
    constructor(buffer) {
        super(buffer);
        this.destinationId = varint.decode(buffer);
        this.messageIds = varint.decode(buffer);
    }

    // OLD CONSTRUCTOR
    constructor(nodeId, vectorClock, destinationId, messageIds) {
        super(4, nodeId, vectorClock);
        this.destinationId = destinationId;
        this.messageIds = messageIds;
    }*/

    write(buffer) {
        super.write(buffer);
        buffer.write(varint.encode(this.destinationId));
        buffer.write(varint.encode(this.messageIds));
    }

}