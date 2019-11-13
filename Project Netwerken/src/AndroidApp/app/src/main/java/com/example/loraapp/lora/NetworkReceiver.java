package com.example.loraapp.lora;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

/**
 * This class 'll check if bluetooth and wifi is connected
 * When connection is disconnected callback 'll be called
 */
public class NetworkReceiver {
    private OnNetworkReceiver onNetworkReceiver;
    private BroadcastReceiver bluetoothBroadcastReceiver, wifiBroadcastReceiver;
    private Context mContext;

    public NetworkReceiver(Context context) {
        this.mContext = context;

        initWifiReceiver();
        initBluetoothReceiver();
    }

    /**
     * This method detects when the wifi state has been changed.
     * When true, a callback 'll be called.
     */
    private void initWifiReceiver() {
        IntentFilter wifiFilters = new IntentFilter();
        wifiFilters.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        wifiBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (onNetworkReceiver != null) {
                    switch (getWifiState()) {
                        case WifiManager.WIFI_STATE_ENABLED:
                            onNetworkReceiver.onWifi(NetworkStatus.ENABLED);
                            break;
                        case WifiManager.WIFI_STATE_DISABLED:
                            onNetworkReceiver.onWifi(NetworkStatus.DISABLED);
                            break;
                        case WifiManager.WIFI_STATE_ENABLING:
                            onNetworkReceiver.onWifi(NetworkStatus.TURNING_ON);
                            break;
                        case WifiManager.WIFI_STATE_DISABLING:
                            onNetworkReceiver.onWifi(NetworkStatus.DISABLED);
                            break;
                    }
                }
            }
        };
        mContext.registerReceiver(wifiBroadcastReceiver, wifiFilters);
    }

    /**
     * This method detects when the bluetooth state has been changed.
     * When true, a callback 'll be called.
     */
    private void initBluetoothReceiver() {
        IntentFilter bluetoothFilter = new IntentFilter();
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        bluetoothBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (onNetworkReceiver != null) {
                    switch (BluetoothAdapter.getDefaultAdapter().getState()) {
                        case BluetoothAdapter.STATE_ON:
                            onNetworkReceiver.onBluetooth(NetworkStatus.ENABLED);
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            onNetworkReceiver.onBluetooth(NetworkStatus.DISABLED);
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            onNetworkReceiver.onBluetooth(NetworkStatus.TURNING_OFF);
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            onNetworkReceiver.onBluetooth(NetworkStatus.TURNING_ON);
                            break;
                    }
                }
            }
        };
        mContext.registerReceiver(bluetoothBroadcastReceiver, bluetoothFilter);
    }

    public void stop() {
        try {
            if (wifiBroadcastReceiver != null)
                mContext.unregisterReceiver(wifiBroadcastReceiver);
            if (bluetoothBroadcastReceiver != null)
                mContext.unregisterReceiver(bluetoothBroadcastReceiver);
        }catch (Exception e){
            e.getMessage();
        }
    }

    /**
     * This method returns the wifi state
     *
     * @return wifi state
     */
    private int getWifiState() {
        WifiManager wifi = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifi.getWifiState();
    }

    /**
     * This method sets the interface
     *
     * @param onNetworkReceiver interface
     */
    public void setOnNetworkReceiver(OnNetworkReceiver onNetworkReceiver) {
        this.onNetworkReceiver = onNetworkReceiver;
    }
}
