const Command = require('./Command');
const varint = require('../../utils/varint/varint');

class IDRequest extends Command {
    constructor(options) {
        super(options);
        if (options.buffer) {
            // constructor(buffer)
            this.random = varint.decode(options.buffer);
        } else {
            // constructor(nodeId, vectorClock)
            this.random = Math.floor(Math.random() * 4228250625 + 1)
        }
    }

/*    // OLD CONSTRUCTOR
    constructor(buffer) {
        super(buffer);
        this.random = varint.decode(buffer);
    }

    // OLD CONSTRUCTOR
    constructor(nodeId, vectorClock) {
        super(1, nodeId, vectorClock);
        this.random = Math.floor(Math.random() * 4228250625 + 1);
    }*/

    write(buffer) {
        super.write(buffer);
        buffer.write(varint.encode(this.random));
    }
}

module.exports = IDRequest;