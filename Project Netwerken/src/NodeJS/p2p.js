let express = require('express');
let SerialPort = require('serialport');
const bodyParser = require('body-parser');

//Initialize Express
let app = express();
app.use(express.static('public'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));

let PORT = 3000;
// Open serial port by looping through all COM ports
let port;

// Buffers for receiving messages
let messageReceiveWindowTimeout;
let receivedMessageBuffer = "";
let receivedMessage = "";

openComPort(0);

// Check and open a COM port recursively
function openComPort(portNumber) {
	port = new SerialPort('COM' + portNumber, function (err) {
		if (err) {
			// We have reached the maximum amount of COM ports. No module attached!
			if (portNumber === 256) {
				console.error('No COM port found to open..');
				//port = null;
				//process.exit(1);
			} else {
				openComPort(++portNumber)
			}
		} else {
			// Connected to the module
			console.log("Working connection on COM port " + portNumber + "!");
			initializeMessageReceiving();
		}
	});
}

/**
 * Initialize message receiving on the module
 */
function initializeMessageReceiving() {
	// Listen to new data
	port.on('readable', function () {
		// Clear the current timeout
		clearTimeout(messageReceiveWindowTimeout);

		//Read some received input
		receivedMessageBuffer += port.read();

		// Set a timer that will update the received message if
		// there is no more data in the coming second
		messageReceiveWindowTimeout = setTimeout(function () {
			if (receivedMessageBuffer.length > 1) {
				receivedMessage = receivedMessageBuffer;
				console.log("Message: " + receivedMessage);
			}

			receivedMessageBuffer = "";
		}, 100);
	});
}

/**
 * Allow the other NodeJS client to communicate :)
 */
app.use(function (req, res, next) {
	res.header("Access-Control-Allow-Origin", "*");
	res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
	next();
});

/**
 * Request to send message from the web page
 */
app.post('/send', function (request, response) {
	let message = request.body.message;

	// If the user did not enter an empty message, send it
	if (message !== undefined && message.length !== 0) {
		console.log("Message sent: " + message);

		// Write message to module
		port.write(message);

		// Send response to web page
		response.status(200).send();
	} else {
		// Bad request
		console.log("Message not sent: " + message);
		response.status(400).send();
	}
});

/**
 * This will be called 1 /4 second when the web page is shown
 */
app.get('/get', function (request, response) {
	// When a message is retrieved from the module, the message will be saved
	// to a local variable called receivedMessage. When the client uses a get
	// request to retrieve a message, the receivedMessage will be redirected
	if (receivedMessage.length > 0) {
		//Got an message, telling the client
		response.status(200).json({message: receivedMessage});
		receivedMessage = "";
	} else {
		//Got no new message
		response.status(200).json({}).end();
	}
});

/**
 * Get the online status. The web page sends a request to /online, and will
 * set the status accordingly. This notices the user that a connection
 * cannot be established
 */
app.get('/online', function (request, response) {
	response.status(200).json(port === null ? {online: false} : {online: true});
});

/**
 * Logging shortcut :)
 * @param message Message to log in the console
 */
function log(message) {
	console.log(message);
}

//Let it listen on port 3000
app.listen(PORT);

module.exports = app;