const Command  = require('./Command');
const varint = require('../../utils/varint/varint');

class IDAssign extends Command {

    constructor(options) {
        super(options);
        if (options.buffer) {
            // constructor(buffer)
            this.random = varint.decode(options.buffer);
            this.id = varint.decode(options.buffer);
        } else {
            // constructor(nodeId, vectorClock, random, id)
            this.random = options.random;
            this.id = options.id;
        }
    }

/*    // OLD CONSTRUCTOR
    constructor(buffer) {
        super(buffer);
        this.random = varint.decode(buffer);
        this.id = varint.decode(buffer);
    }

    // OLD CONSTRUCTOR
    constructor(nodeId, vectorClock, random, id) {
        super(2, nodeId, vectorClock);
        this.random = random;
        this.id = id;
    }*/

    write(buffer) {
        super.write(buffer);
        buffer.write(varint.encode(this.random));
        buffer.write(varint.encode(this.id));
    }
}