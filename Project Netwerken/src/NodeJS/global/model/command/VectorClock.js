const varint = require('../../utils/varint/varint');

class VectorClock {

    constructor(options) {
        if (options.buffer) {
            let size = varint.decode(options.buffer);
            this.array = [size];
            for (let i = 0; i < size; i++) {
                this.array[i] = varint.decode(options.buffer);
            }
        } else {
            this.array = [];
            for(let i = 0; i < options.size; i++){
                this.array[i] = 0;
            }
        }
    }

/*    // OLD CONSTRUCTOR
    constructor(buffer){
        let size = varint.decode(buffer);
        this.array = [size];
        for (let i = 0; i < size; i++) {
            this.array[i] = varint.decode(buffer);
        }
    }

    // OLD CONSTRUCTOR
    constructor(size) {
        this.array = [];
        for(let i = 0; i < size; i++){
            this.array[i] = 0;
        }
    }*/

    write(buffer){
        buffer.write(varint.encode(this.array.length));
        for(let i = 0; i < this.array.length; i++){
            buffer.write(varint.encode(this.array[i]));
        }
    }
}

module.exports = VectorClock;