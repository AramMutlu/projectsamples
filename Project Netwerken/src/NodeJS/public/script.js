let results = document.getElementById("results");
let message = document.getElementById("message");

//Initialize P2p and UDP
const P2P_URL = "http://localhost:3000";
const UDP_URL = "http://localhost:4000";
let p2pIsOnline = false;
let udpIsOnline = false;
let useP2P = true;
let useUDP = true;

let p2pElementClassList;
let udpElementClassList;

//List containing all messages, sent and received
let messages = [];

/**
 * Initialize web page
 */
function initialize() {
	//Initialize both elements
	p2pElementClassList = document.getElementById("p2p").classList;
	udpElementClassList = document.getElementById("udp").classList;

	//Get the online status of P2P and UDP and check for messages after that
	getOnlineStatus().then(function () {
		//Retrieve messages every 1 / 10 second
		setInterval(getMessage, 250);
	});
}

//Send message when pressing enter
document.addEventListener("keydown", function (event) {
	if (event.which === 13) sendMessage();
});

/**
 * Choose P2P as method to send messages
 */
function chooseP2P() {
	if (p2pIsOnline) {
		if (p2pElementClassList.contains("selected")) {
			//Remove selected state
			useP2P = false;
			p2pElementClassList.remove("selected");
		} else {
			//Add selected state
			useP2P = true;
			p2pElementClassList.add("selected");
		}
	}
}

/**
 * Choose UDP as method to send messages
 */
function chooseUDP() {
	if (udpIsOnline) {
		if (udpElementClassList.contains("selected")) {
			//Remove selected state
			useUDP = false;
			udpElementClassList.remove("selected");
		} else {
			//Add selected state
			useUDP = true;
			udpElementClassList.add("selected");
		}
	}
}

/**
 * Check if both P2P and UDP are working. Add the state to the button on the webpage and backend
 *
 * @param url URL of the P2P or UDP
 * @param element Element to add the state to
 * @returns {Promise<any>}
 */
function checkOnlineStatus(url, element) {
	return new Promise(function (resolve, reject) {
		fetch(url + "/online", {
			method: "GET"
		}).then(function (response) {
			response.json().then(function (json) {
				let classList = document.getElementById(element).classList;

				if (json.online === true) {
					classList.remove("offline");
					classList.add("online");
					resolve(true);
				} else {
					classList.remove("online");
					classList.add("offline");
					resolve(false);
				}
			});
		}, function () {
			let classList = document.getElementById(element).classList;

			classList.remove("online");
			classList.add("offline");

			reject(false);
		});
	});
}

/**
 * Change the text of the Send Message button temporarily
 */
function changeButtonText(message, duration) {
	//Change the text of the button
	let sendMessage = document.getElementById("send-message");
	sendMessage.innerHTML = message;

	//Reset the text after (duration) seconds
	setTimeout(function () {
		sendMessage.innerHTML = "Send Message";
	}, duration);
}

/**
 * This method checks which urls we should send messages and should get messages
 * from. Based on whether the user wants to use P2P, UDP or both we return the
 * urls needed to make those requests.
 *
 * @returns {*[]} Urls to send and receive messages
 */
function urlsToQuery() {
	if (useP2P && useUDP) {
		return [P2P_URL, UDP_URL];
	} else if (useP2P) {
		return [P2P_URL];
	} else if (useUDP) {
		return [UDP_URL];
	}

	return [];
}

/**
 * Send a message to the NodeJS client.
 */
function sendMessage() {
	//Save the message input field value
	let messageBody = message.value;

	//Get the possible urls to send to
	let urls = urlsToQuery();

	//Loop through all possible urls
	urls.forEach(function (url) {
		//Send a send request to the NodeJS
		fetch(url + "/send", {
			method: "POST",
			mode: "CORS",
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				message: messageBody
			})
		}).then(function (response) {
			let status = response.status;

			//Message successfully sent
			if (status === 200) {
				//Tell the user by changing the button text for 1 second
				changeButtonText("Sent", 1000);

				//If we are the last in the loop, add the sent message to the results and the list
				if (url === urls[urls.length - 1]) {
					//Add the message to the list
					let sentMessage = new Message(messageBody, true);
					messages.push(sentMessage);

					//Show the message on the screen
					showMessage(sentMessage);
				}

				//Reset the message input field
				message.value = null;
			} else if (status === 400) {
				//Failed to send message
				changeButtonText("Sending message failed", 2500);
			}
		}, function () {
			//Failed to send message
			changeButtonText("Sending message failed", 2500);
		});
	});
}

/**
 * Retrieve message from NodeJS client
 */
function getMessage() {
	//Get the possible urls to get messages from
	let urls = urlsToQuery();

	//Loop through all possible urls
	urls.forEach(function (url) {
		//Send a send request to the NodeJS
		fetch(url + "/get", {
			method: "GET"
		}).then(function (response) {
			response.json().then(function (json) {
				//If we received an message
				if (json.hasOwnProperty("message")) {
					//Check if the message is not empty
					if (json.message.length > 0) {
						//Save message to list
						let receivedMessage = new Message(json.message, false);
						messages.push(receivedMessage);

						//Show message
						showMessage(receivedMessage);
					}
				}
			});
		});
	});
}

/**
 * Check if the given message is sent by you. If so, append it to the results innerHTML as
 * an message sent by you. If not, visa versa.
 *
 * @param message Message to show
 */
function showMessage(message) {
	let html = "";
	if (message.isSentByMe()) {
		html = "" +
			"<div class='message sent'>" +
			"	<span class='text'>" + message.getText() + "</span>" +
			"	<span class='time'>" + getCurrentTime(message.getTime()) + "</span>" +
			"</div>";
	} else {
		html = "" +
			"<div class='message'>" +
			"	<span class='time'>" + getCurrentTime(message.getTime()) + "</span>" +
			"	<span class='text'>" + message.getText() + "</span>" +
			"</div>";
	}

	results.innerHTML = html + results.innerHTML;
}

/**
 * This method returns an string containing the time of the given date in the
 * format HH:MM:SS where seconds and minutes below 10 are prefixed by a zero
 *
 * @param date Date to get the time from
 * @returns {string} String containing the formatted time
 */
function getCurrentTime(date) {
	let hours = date.getHours();
	let minutes = date.getMinutes();
	let seconds = date.getSeconds();

	return hours + ":" +
		(minutes < 10 ? "0" + minutes : minutes) + ":" +
		(seconds < 10 ? "0" + seconds : seconds);
}

/**
 * Check if P2P and UDP are online, otherwise we can't use it
 */
function getOnlineStatus() {
	return new Promise(function (resolve) {
		let p2pDone = false;
		let udpDone = false;

		//Get online status for P2P
		checkOnlineStatus(P2P_URL, "p2p").then(function (status) {
			//Set online status
			p2pIsOnline = status;
			if (status) chooseP2P();
			else useP2P = false;

			//Resolve promise
			if (udpDone) resolve();
			p2pDone = true;
		}, function () {
			//Set online status
			p2pIsOnline = false;
			useP2P = false;

			//Resolve promise
			if (udpDone) resolve();
			p2pDone = true;
		});

		//Get online status for UDP
		checkOnlineStatus(UDP_URL, "udp").then(function (status) {
			//Set online status
			udpIsOnline = status;
			if (status) chooseUDP();
			else useUDP = false;

			//Resolve promise
			if (p2pDone) resolve();
			udpDone = true;
		}, function () {
			//Set online status
			udpIsOnline = false;
			useUDP = false;

			//Resolve promise
			if (p2pDone) resolve();
			udpDone = true;
		});
	});
}