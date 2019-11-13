const PacketPart = require('../model/packet/PacketPart');
const Buffer = require('../model/Buffer');
const IDAssign = require('../model/command/IDAssign');
const IDRequest = require('../model/command/IDRequest');
const NewMessage = require('../model/command/NewMessage');
const RequestMessage = require('../model/command/RequestMessage');

const varint = require('../utils/varint/varint');

class reader {
    constructor(){
        this.packetParts = new Map();
    }

    readCommand(buffer) {
        let type = varint.decode(buffer);
        switch(type) {
            case 1:
                return new IDRequest(buffer);
            case 2:
                return new IDAssign(buffer);
            case 3:
                return new NewMessage(buffer);
            case 4:
                return new RequestMessage(buffer);
            default:
                return null;
        }
    }

    readPart(bytes){
        let thisPart = new PacketPart(bytes);
        let id = thisPart.id;
        let currentParts = [];
        if(!this.packetParts.has(id)){
            currentParts = new PacketPart[thisPart.total];
        } else {
            currentParts = this.packetParts.get(id);
        }
        currentParts[thisPart.currentPart-1] = thisPart;
        this.packetParts.set(id, currentParts);

        //lastpart
        if(this.isComplete(currentParts)){
            let buffer = new Buffer();
            for(let i = 0; i < currentParts.length; i++) {
                buffer.write(currentParts[i].commandPart);
            }
            this.packetParts.delete(id);
            return this.readCommand(buffer);
        } else {
            return false;
        }

    }

    isComplete(currentParts) {
        for(let i = 0; i < currentParts.length; i++){
            let part = currentParts[i];
            if(part === undefined){
                return false;
            }
        }
        return true;
    }
}