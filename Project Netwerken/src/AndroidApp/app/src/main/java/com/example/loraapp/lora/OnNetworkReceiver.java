package com.example.loraapp.lora;

public interface OnNetworkReceiver {
    void onWifi(NetworkStatus status);

    void onBluetooth(NetworkStatus status);
}
