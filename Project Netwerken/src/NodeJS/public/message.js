class Message {
	/**
	 * Construct an message containing the text and the sender
	 * @param text Message Body
	 * @param sentByMe Sender
	 */
	constructor(text, sentByMe) {
		this.text = text;
		this.sentByMe = sentByMe;
		this.time = new Date();
	}

	/**
	 * Get Message Body
	 * @returns {*}
	 */
	getText() {
		return this.text;
	}

	/**
	 * Check if message is sent by yourself
	 * @returns {*}
	 */
	isSentByMe() {
		return this.sentByMe;
	}

	/**
	 * Get the time of the message
	 * @returns {Date}
	 */
	getTime() {
		return this.time;
	}
}