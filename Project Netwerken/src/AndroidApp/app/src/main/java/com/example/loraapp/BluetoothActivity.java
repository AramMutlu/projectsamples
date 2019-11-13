package com.example.loraapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.loraapp.adapters.BleDeviceListAdapter;
import com.example.loraapp.interfaces.OnDeviceClickListener;
import com.example.loraapp.lora.bluetooth.BluetoothController;
import com.example.loraapp.lora.bluetooth.OnConnected;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BluetoothActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private BleDeviceListAdapter bleDeviceListAdapter;
    private BluetoothController bluetoothController;
    private ArrayList<BluetoothDevice> mBleDevices;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;

    @BindView(R.id.rvBluetoothDevices)
    RecyclerView rvBluetoothDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mHandler = new Handler();
        bluetoothController = BluetoothController.getInstance(this);
        mBleDevices = new ArrayList<>();

        // Listen to the callbacks
        bluetoothController.setOnConnected(connected -> {
            if (connected) {
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
            }
        });


        // Check if device supports bluetooth low energy
        // if not, finish the activity and show a toast
        if (!bluetoothController.bleSupport()) {
            Toast.makeText(this, "Device doesn't support Bluetooth Low Energy", Toast.LENGTH_LONG).show();
            finish();
        }

        // Initialize adapter
        bleDeviceListAdapter = new BleDeviceListAdapter(mBleDevices);
        bleDeviceListAdapter.setOnDeviceClickListener(device -> {
            // Try to connect
            bluetoothController.connect(device);

            // Stop scanning because the device has already been found
            mBluetoothAdapter.getBluetoothLeScanner().stopScan(mLeScanCallback);
        });

        // Initialize recyclerview
        rvBluetoothDevices.setLayoutManager(new LinearLayoutManager(this));
        rvBluetoothDevices.setAdapter(bleDeviceListAdapter);

        // Scan for ble devices
        scanBleDevices();
    }

    /**
     * Scan the bluetooth devices and stop after 10 seconds
     */
    private void scanBleDevices() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.getBluetoothLeScanner().startScan(mLeScanCallback);

        // Stop scanning after 10 seconds
        mHandler.postDelayed(() -> mBluetoothAdapter.getBluetoothLeScanner().stopScan(mLeScanCallback), SCAN_PERIOD);
    }

    /**
     * When device is found, add to list and refresh
     */
    private ScanCallback mLeScanCallback =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    if (!mBleDevices.contains(result.getDevice())) {
                        mBleDevices.add(result.getDevice());
                        bleDeviceListAdapter.refresh(mBleDevices);
                    }
                }
            };


}
