package com.example.loraapp.lora.wlan;

import com.example.loraapp.global.model.command.Command;

public interface OnMessageReceived {
    void onReceived(Command command);
}
