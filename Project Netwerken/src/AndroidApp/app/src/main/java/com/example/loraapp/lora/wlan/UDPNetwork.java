package com.example.loraapp.lora.wlan;

import android.graphics.Color;
import android.util.Log;

import com.example.loraapp.global.model.command.Command;
import com.example.loraapp.global.model.command.NewMessage;
import com.example.loraapp.global.utils.Reader;
import com.example.loraapp.lora.AlertCode;
import com.example.loraapp.lora.Message;
import com.example.loraapp.lora.Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class UDPNetwork extends Network {
    private static int PORT_NUMBER = 8888;
    private Reader reader;
    private OnMessageReceived onMessageReceived;

    /**
     * Constructor that initializes UDPNetwork
     */
    public UDPNetwork() {
        init();
    }

    /**
     * This method initializes the broadcast server listener
     */
    @Override
    public void init() {
        // Initialize UDP server listener
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create the socket
                    DatagramSocket socket = new DatagramSocket(PORT_NUMBER, InetAddress.getByName("0.0.0.0"));
                    socket.setBroadcast(true);
                    reader = new Reader();
                    // Endless loop, always listening
                    while (true) {
                        // Set message buffer
                        byte[] msgBuffer = new byte[15000];

                        // Create a packet
                        DatagramPacket packet = new DatagramPacket(msgBuffer, msgBuffer.length);
                        socket.receive(packet);

                        // If there is a message received, do a callback
                        if (packet.getLength() > 0 && onMessageReceived != null) {
                            // TODO: reassemble the message (Get a alarmcode etc.)

                            // Create the message
//                            String receivedMessage = new String(msgBuffer, 0, packet.getLength());
//                            AlertCode alertCode = new AlertCode(1, Color.RED, "TEST-ALERT");

                            // Do the callback
                            byte[] bytes = Arrays.copyOfRange(packet.getData(), packet.getOffset(), packet.getLength());
                            Command command = reader.readPart(bytes);
                            if(command != null){
                                onMessageReceived.onReceived(command);
                            }

                        }
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Start the server
        thread.start();
    }

    /**
     * This method 'll broadcast a message
     *
     * @param bytes the bytes that 'll be broadcasted
     */
    @Override
    public void sendPart(final byte[] bytes) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create socket
                    DatagramSocket socket = new DatagramSocket();
                    socket.setBroadcast(true);

                    try {
                        // Create packet
                        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("255.255.255.255"), PORT_NUMBER);

                        // Try to send the packet on the broadcast address
                        socket.send(packet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Start the thread
        thread.start();
    }

    /**
     * This method can be used to set a callback listener
     *
     * @param onMessageReceived
     */
    public void setOnMessageReceived(OnMessageReceived onMessageReceived) {
        this.onMessageReceived = onMessageReceived;
    }
}
