const PacketPart  = require('./PacketPart');
class DataPacket {
	constructor(command, packetByteSize) {
		this.id = Math.floor(Math.random() * 4228250625 +1);
        //TODO: encrypt here
		this.encryptedCommand = command;
		this.packetParts = this.createParts(packetByteSize);
	}

    createParts(packetByteSize){
        let result = [];
	    let byteParts = this.splitByteArray(this.encryptedCommand, (packetByteSize-13));
        for(let i = 0; i < byteParts.length; i++){
            result[i] = new PacketPart(this.id, i+1, byteParts.length, byteParts[i]);
        }
        return result;
	}

	splitByteArray(source, partSize){
	    let result = [];
        for (i=0; i < source.length; i+=partSize) {
            result[i] = array.slice(i,i+partSize);
        }
        return result;
    }
}