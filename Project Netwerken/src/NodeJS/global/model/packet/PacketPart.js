const varint = require('../../utils/varint/varint');

class PacketPart {
    constructor(options) {
        if (options.buffer) {
            this.id = varint.decode(options.buffer);
            this.currentPart = varint.decode(options.buffer);
            this.total = varint.decode(options.buffer);
            let offset = options.buffer.position();
            let fullarray = options.buffer.array;
            this.commandPart = varint.decode(fullarray.slice(offset, fullarray.length));
        } else {
            this.id = options.id;
            this.currentPart = options.currentPart;
            this.total = options.total;
            this.commandPart = options.commandPart;
        }
    }

/*    // OLD CONSTRUCTOR
    constructor(buffer) {
        this.id = varint.decode(buffer);
        this.currentPart = varint.decode(buffer);
        this.total = varint.decode(buffer);
        let offset = buffer.position();
        let fullarray = buffer.array;
        this.commandPart = varint.decode(fullarray.slice(offset, fullarray.length));
    }

    // OLD CONSTRUCTOR
	constructor(id, currentPart, total, commandPart) {
		this.id = id;
		this.currentPart = currentPart;
		this.total = total;
		this.commandPart = commandPart;
	}*/

    write(buffer){
        buffer.write(varint.encode(this.id));
        buffer.write(varint.encode(this.currentPart));
        buffer.write(varint.encode(this.total));
        buffer.write(this.commandPart);
    }
}