const varint = require('../../utils/varint/varint');

class BaseAlarm {
    constructor(alarmcode, buffer) {
        this.alarmCode = alarmcode;
        this.dateTime = varint.decode(buffer);
    }

    constructor(alarmCode) {
        if (new.target === Abstract) {
            throw new TypeError("Cannot construct Abstract instances directly");
        }
        this.alarmCode = alarmCode;
        this.dateTime = Math.round((new Date()).getTime() / 1000);
    }

    write(buffer){
        buffer.write(varint.encode(this.alarmCode));
        buffer.write(varint.encode(this.dateTime));
    }
}