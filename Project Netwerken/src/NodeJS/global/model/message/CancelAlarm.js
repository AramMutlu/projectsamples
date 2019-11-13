const BaseAlarm = require('./BaseAlarm');
const varint = require('../../utils/varint/varint');

class Alarm extends BaseAlarm {
    constructor(alarmcode, buffer) {
        super(alarmcode, buffer);
        this.cancelID = varint.decode(buffer);
    }

    constructor(cancelID) {
        super(99);
        this.cancelID = cancelID;
    }

    write(buffer){
        super.write(buffer);
        buffer.write(varint.encode(this.cancelID));
    }
}