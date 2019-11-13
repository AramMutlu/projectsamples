let express = require('express');
let dgram = require('dgram');
let ip = require('ip');
const bodyParser = require('body-parser');

// TODO For global protocol
// let IDAssign = require('./global/model/command/IDAssign');
// let IDRequest = require('./global/model/command/IDRequest');
// let NewMessage = require('./global/model/command/NewMessage');
// let RequestMessage = require('./global/model/command/RequestMessage');
// let VectorClock = require('./global/model/command/VectorClock');
// let Buffer = require('./global/model/Buffer');
// let reader = require('./global/utils/reader.js');
// let DataPacket = require('./global/model/packet/DataPacket');
// let Command = require('./global/model/command/Command');

// Initialize Express
let app = express();
app.use(express.static('public'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));

// Initialize all addresses and the port
let UDP_SERVER_ADDRESS = "0.0.0.0";
let UDP_CLIENT_ADDRESS = "255.255.255.255";
let PORT = 8888;
let MAX_PACKET_SIZE = 20;
let server;
let client;
let receivedMessage = "";

// Initialize the server and the client
initializeServer();
initializeClient();

/**
 * Initialize Server. The server receives all messages sent to port 8888
 */
function initializeServer() {
    // Create server
    server = dgram.createSocket("udp4");

    /**
     * Tell the console that we are online and enable broadcast
     */
    server.on("listening", function () {
        //Get address
        let address = server.address();
        log("Server listening on " + address.address + ":" + address.port);

        // Enable broadcast
        server.setBroadcast(true);

        /*
        // TODO Request a NodeID when global protocol is implemented
        log("Requesting an ID");
        let vectorOptions = {};
        vectorOptions.size = 10;
        let options = {};
        options.nodeId = 0;
        options.vectorClock = new VectorClock(vectorOptions);
        let request = new IDRequest(options);

        sendCommand(request);*/
    });

    // Received message, notify in console
    server.on("message", function (message, remote) {
        /*
        // TODO For when global protocol is implemented
        // TODO Decode command
        // TODO Check command type
        // TODO Broadcast message if not adressed to you

        // TODO Do something else when only a part of a message
        let read = new reader();
        let readed = read.readPart(message);
        if (readed) {
            // Received a full message
            if (readed instanceof IDAssign) {
                console.log("IDAssign");
            } else if (read instanceof IDRequest) {
                console.log("IDRequest");
            } else if (read instanceof NewMessage) {
                console.log("NewMessage");
            } else if (read instanceof RequestMessage) {
                console.log("RequestMessage");
            }
        }
        */

        log("DEBUG > Received message " + message);
        if (remote.address !== ip.address()) {
            log("Sender: " + remote.address + ':' + remote.port + ' - ' + message);
            receivedMessage += message;
        }
    });

    /**
     * On error set the server to null so the online request status knows
     * a connection cannot be established.
     */
    server.on("error", function () {
        server = null;
    });

    // Bind server to port and address
    server.bind(PORT, UDP_SERVER_ADDRESS);
}

/**
 * Initialize the client. The client can send a broadcast message
 */
function initializeClient() {
    // Create client
    client = dgram.createSocket("udp4");

    /**
     * Tell the console that we are online and enable broadcast
     */
    client.on("listening", function () {
        // Get address
        let address = client.address();
        log("Client listening on " + address.address + ":" + address.port);

        // Enable broadcast
        client.setBroadcast(true);
    });

    /**
     * On error set the client to null so the online request status knows
     * a connection cannot be established.
     */
    client.on("error", function () {
        client = null;
    });
}

/*
//TODO For integration with the global protocol specification
function sendMessage(buffer) {
    // Send the message
    client.send(buffer, 0, buffer.length, PORT, UDP_CLIENT_ADDRESS, function (err) {
        if (err) {
            log("Error: " + err);
            return false;
        } else {
            log("Message sent: " + message);
            return true;
        }
    });
}
*/

/*
//TODO For integration with the global protocol specification
function sendCommand(command) {
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
*/

/**
 * Allow the other NodeJS client to communicate :)
 */
app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});

/**
 * Send a message over UDP
 */
app.post('/send', function (request, response) {
    let message = request.body.message;

    // Check if the message doesn't contain anything
    if (message !== undefined && message.length !== 0) {
        let messageBuffer = new Buffer(message);

        // Send message
        client.send(messageBuffer, 0, messageBuffer.length, PORT, UDP_CLIENT_ADDRESS, function (err) {
            if (err) {
                log("Error: " + err);
                response.status(500).json({statusTexT: "Something went wrong"});
            } else {
                log("Message sent: " + message);
                response.status(200).json({});
            }
        });
    } else {
        response.status(400).json({});
    }
});

/**
 * Every 1 / 4 second the webpage will retrieve messages. If we have a message,
 * we will return it to the webpage which will handle it.
 */
app.get('/get', function (request, response) {
    // When a message is retrieved from the module, the message will be saved
    // to a local variable called receivedMessage. When the client uses a get
    // request to retrieve a message, the receivedMessage will be redirected
    if (receivedMessage.length > 0) {
        // Got an message, telling the client
        response.status(200).json({message: receivedMessage});
        receivedMessage = "";
    } else {
        // Got no new message
        response.status(200).json({}).end();
    }
});

/**
 * Get the online status. The web page sends a request to /online, and will
 * set the status accordingly. This notices the user that a connection
 * cannot be established
 */
app.get('/online', function (request, response) {
    response.status(200).json(server === null || client == null
        ? {online: false} : {online: true});
});

/**
 * Logging shortcut :)
 * @param message Message to log in the console
 */
function log(message) {
    console.log(message);
}


// Let it listen on port 4000
app.listen(4000);

module.exports = app;