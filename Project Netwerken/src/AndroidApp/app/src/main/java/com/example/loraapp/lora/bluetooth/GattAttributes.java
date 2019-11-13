package com.example.loraapp.lora.bluetooth;

import java.util.HashMap;

public class GattAttributes {
    private static HashMap<String, String> attributes = new HashMap();

    static {
        // Sample Services.
        attributes.put("00001800-0000-1000-8000-00805f9b34fb", "Generic Access");
        attributes.put("00001801-0000-1000-8000-00805f9b34fb", "Generic Attribute");
        attributes.put("0000180f-0000-1000-8000-00805f9b34fb", "Battery Service");
        attributes.put("00001805-0000-1000-8000-00805f9b34fb", "Current Time Service");

        // Sample Characteristics.
        attributes.put("00002a00-0000-1000-8000-00805f9b34fb", "Device Name");
        attributes.put("00002a01-0000-1000-8000-00805f9b34fb", "Appearance");
        attributes.put("00002a04-0000-1000-8000-00805f9b34fb", "Peripheral Preferred Connection Parameters");
        attributes.put("00002a05-0000-1000-8000-00805f9b34fb", "Service Changed");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
