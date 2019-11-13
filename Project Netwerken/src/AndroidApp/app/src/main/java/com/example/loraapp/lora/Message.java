package com.example.loraapp.lora;

import java.util.Date;


public class Message {
    private String targetAddress;
    private Date date;
    private AlertCode alertCode;
    private String message;

    public Message(AlertCode alertCode, String message) {
        this.date = new Date();
        this.alertCode = alertCode;
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public AlertCode getAlertCode() {
        return alertCode;
    }

    public String getMessage() {
        return message;
    }
}
