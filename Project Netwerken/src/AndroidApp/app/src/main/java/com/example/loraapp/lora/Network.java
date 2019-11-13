package com.example.loraapp.lora;

/**
 * Created by tom on 13-3-18.
 */

public abstract class Network {

    private NetworkMessageListener networkMessageListener;

    public abstract void init() throws LoraException;
    public abstract void sendPart(byte[] part);

    public void receiveMessage(byte[] part) {
        if (part != null) this.networkMessageListener.onMessageReceived(this, part);
    }

    public void setNetworkMessageListener(NetworkMessageListener networkMessageListener) {
        this.networkMessageListener = networkMessageListener;
    }

    public interface NetworkMessageListener {
        public void onMessageReceived(Network network, byte[] part);
    }

}
