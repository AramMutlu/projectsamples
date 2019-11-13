class Buffer {
    constructor(options) {
        this.position = 0;
        if (options.array) {
            this.array = options.array;
        } else {
            this.array = [];
        }
    }

/*  // OLD CONSTRUCTOR
    constructor() {
        this.array = [];
        this.position = 0;
    }

    // OLD CONSTRUCTOR
    constructor(array) {
        this.array = array;
        this.position = 0;
    }*/

    getNext(){
        return this.array[this.position++];
    }

    write(array){
        let old = this.array;
        this.array = new Uint8Array(this.array.length + array.length);
        this.array.set(old,0);
        this.array.set(array, old.length);
    }
}

module.exports = Buffer;