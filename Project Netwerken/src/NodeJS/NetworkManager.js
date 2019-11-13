let p2p = require('./p2p');
let udp = require('./udp');
let IDAssign = require('./global/model/command/IDAssign');
let IDRequest = require('./global/model/command/IDRequest');
let NewMessage = require('./global/model/command/NewMessage');
let RequestMessage = require('./global/model/command/RequestMessage');
let VectorClock = require('./global/model/command/VectorClock');
let Buffer = require('./global/model/Buffer');
let reader = require('./global/utils/reader.js');
let DataPacket = require('./global/model/packet/DataPacket');
let Command = require('./global/model/command/Command');

let MAX_PACKET_SIZE = 20;

// TODO For combining UDP and P2P, not finished
/*
class NetworkManager {
    constructor() {
        this.udpClient = new udp(this);
        this.p2pClient = new p2p(this);
        this.udpClient.initia
    }

    /!**
     * Incoming messages from the UDP and P2P client
     * @param buffer the incoming message
     *!/
    receivedMessage(buffer) {

    }

    /!**
     * Sends a message to the UDP and P2P client
     * @param buffer the incoming message
     *!/
    sendMessage(buffer) {

    }

    sendCommand(command) {
        if (command instanceof Command) {
            let buffer = new Buffer();
            command.write(buffer);
            let dataPacket = new DataPacket(buffer.array, MAX_PACKET_SIZE);
            let packetParts = dataPacket.packetParts;
            for (let i = 0; i < packetParts.length; i++) {
                sendMessage(packetParts[i]);
            }
            return true;
        } else {
            console.log("Given command isn't a command!");
            return false;
        }
    }
}*/
