const BaseAlarm  = require('./BaseAlarm');
const varint = require('../../utils/varint/varint');

class Alarm extends BaseAlarm {

    constructor(alarmcode, buffer) {
        super(alarmcode, buffer);
        this.priority = varint.decode(buffer);
        let offset = buffer.position();
        let fullarray = buffer.array;
        let stringpart = fullarray.slice(offset, fullarray.length);
        this.text = varint.decode(stringpart);
    }

    constructor(alarmcode, priority, text) {
        super(alarmcode);
        this.priority = priority;
        this.text = text;
    }

    write(buffer){
        super.write(buffer);
        buffer.write(varint.encode(this.priority));
        buffer.write(varint.encode(this.text));
    }

}