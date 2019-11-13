const varint = require('../../utils/varint/varint');

class Command {
    constructor(options) {
        this.type = options.type;
        if (options.buffer) {
            // constructor(type, buffer)
            this.nodeId = varint.decode(options.buffer);
        } else {
            // constructor(type, nodeId, vectorCock)
            this.nodeId = options.nodeId || options.authorId;
            this.vectorClock = options.vectorClock;
        }
        this.type = options.type;
        this.nodeId = options.nodeId || varint.decode(options.buffer);
        if (options.vectorClock) this.vectorClock = options.vectorClock;
    }

    /*	// OLD CONSTRUCTOR
        constructor(type, buffer){
            this.type = type;
            this.nodeId = varint.decode(buffer);
        }

        // OLD CONSTRUCTOR
        constructor(type, nodeId, vectorClock) {
            if (new.target === Abstract) {
                throw new TypeError("Cannot construct Abstract instances directly");
            }
            this.type = type;
            this.nodeId = nodeId;
            this.vectorClock = vectorClock;
        }*/

    write(buffer) {
        buffer.write(varint.encode(this.type));
        buffer.write(varint.encode(this.nodeId));
        this.vectorClock.write(buffer);
    }
}

module.exports = Command;