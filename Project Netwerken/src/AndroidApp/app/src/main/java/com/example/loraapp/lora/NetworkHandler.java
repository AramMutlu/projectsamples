package com.example.loraapp.lora;

import java.util.ArrayList;
import java.util.List;

public class NetworkHandler implements Network.NetworkMessageListener {

    private List<Network> networks;
    private Encryption encryption;
    private MessageListener messageListener;

    public NetworkHandler() {
        this.networks = new ArrayList<>();
        this.encryption = new Encryption();
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void addNetwork(Network network) {
        network.setNetworkMessageListener(this);
        networks.add(network);
    }

    @Override
    public void onMessageReceived(Network network, byte[] part) {
        //encryption.decrypt(message);
        // TODO Receive message from network
        if (part != null && messageListener != null) {
            // TODO message is for this device
            messageListener.onMessageReceived(part);
        } else {
            // TODO not for this device send to other devices?
            sendMessage(part);
        }
    }

    public void sendMessage(byte[] part) {
        for (Network network : networks) {
            sendMessage(network, part);
        }
    }

    public void sendMessage(Network network, byte[] part) {
        //encryption.encrypt(message);
        network.sendPart(part);
    }

    public interface MessageListener {
        public void onMessageReceived(byte[] part);
    }
}
