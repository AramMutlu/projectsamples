package com.example.loraapp.lora.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.loraapp.lora.AlertCode;
import com.example.loraapp.lora.LoraException;
import com.example.loraapp.lora.Message;
import com.example.loraapp.lora.Network;
import com.example.loraapp.lora.wlan.OnMessageReceived;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.content.ContentValues.TAG;
import static android.content.Context.BIND_AUTO_CREATE;

public class BluetoothController extends Network {
    private Context mContext;
    private static BluetoothController instance;
    private BluetoothLeService mBluetoothLeService;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;

    private OnMessageReceived onMessageReceived;
    private OnConnected onConnected;

    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";


    /**
     * Constructor of the bluetoothController
     *
     * @param context
     * @throws LoraException
     */
    private BluetoothController(Context context) {
        this.mContext = context;

        init();
    }

    /**
     * Intialize instance for the first time
     *
     * @param context
     * @return the BluetoothController instance
     */
    public static BluetoothController getInstance(Context context) {
        if (instance == null) instance = new BluetoothController(context);
        return instance;
    }

    /**
     * Initializes the BluetoothAdapter
     */
    private void initBluetoothAdapter() {
        BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        this.mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    /**
     * Initializes some parameters
     */
    @Override
    public void init() {
        mGattCharacteristics = new ArrayList<>();
        initBluetoothAdapter();
    }

    /**
     * This method is be used to send a message to the bluetooth device
     *
     * @param part the part
     */
    @Override
    public void sendPart(byte[] part) {
		if (mGattCharacteristics != null) {

            // TODO: set the correct UUID and implement the global specification
            UUID uuid = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
            BluetoothGattCharacteristic bluetoothGattCharacteristic = new BluetoothGattCharacteristic(uuid, BluetoothGattCharacteristic.PROPERTY_WRITE, BluetoothGattCharacteristic.PERMISSION_WRITE);
            //TODO: send byte[]
            //mBluetoothLeService.writeCharacteristic(bluetoothGattCharacteristic, part);
        }
    }

    /**
     * This method checks if the device supports Bluetooth Low Energy
     *
     * @return true when supported, false when not supported
     */
    public boolean bleSupport() {
        if (mBluetoothAdapter == null) initBluetoothAdapter();
        return mBluetoothAdapter != null;
    }


    private BluetoothException exception(String message) {
        return new BluetoothException(message);
    }

    /**
     * Connect to the selected device
     *
     * @param device the selected device
     */
    public void connect(BluetoothDevice device) {
        this.mDevice = device;

        // Destroy the currect connection if possible
        onDestroy();

        // Connect to the selected device
        Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
        mContext.bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        mContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

    }

    /**
     * Call this method to destroy the connection
     */
    public void onDestroy() {
        try {
            mContext.unbindService(mServiceConnection);
            mContext.unregisterReceiver(mGattUpdateReceiver);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Code to manage Service lifecycle
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to intialize");
                Toast.makeText(mContext, "Unable to initialize", Toast.LENGTH_SHORT).show();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDevice.getAddress());
            Log.d(TAG, "Connecting device");
            Toast.makeText(mContext, "Connecting device", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
            Log.d(TAG, "Connection disconnected");
            Toast.makeText(mContext, "Connection disconnected", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Handles various events fired by the Service.
     * ACTION_GATT_CONNECTED: connected to a GATT server.
     * ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
     * ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
     * ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
     * or notification operations.
     **/
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "Action: " + action);

            switch (action) {
                case Constant.ACTION_GATT_CONNECTED:
                    Log.d(TAG, "Connected");

                    // Callback
                    if (onConnected != null) onConnected.onConnected(true);
                    break;
                case Constant.ACTION_GATT_DISCONNECTED:
                    Log.d(TAG, "Disconnected");

                    // Callback
                    if (onConnected != null) onConnected.onConnected(false);
                    break;
                case Constant.ACTION_GATT_SERVICES_DISCOVERED:
                    // Show all the supported services and characteristics on the user interface.
                    displayGattServices(mBluetoothLeService.getSupportedGattServices());
                    break;
                case Constant.ACTION_DATA_AVAILABLE:
                    // We received some data from the BLE device!
                    String data = intent.getStringExtra(Constant.EXTRA_DATA);

                    // Do a callback
                    if (onMessageReceived != null && data != null) {
                        // TODO reassamble the message with the global specifications
                        Message message = new Message(AlertCode.getAlertCodes().get(1), data);
                        //TODO: send byte[] part
                        //onMessageReceived.onReceived(message);
                    }

                    // Log the data
                    if (data != null) Log.d(TAG, "Data: " + data);
                    break;
            }
        }
    };

    /**
     * Demonstrates how to iterate through the supported GATT Services/Characteristics.
     * In this sample, we populate the data structure that is bound to the ExpandableListView
     * on the UI.
     **/
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        String uuid;
        String service_name, characteristic_name;
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<>();
        mGattCharacteristics = new ArrayList<>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<>();

            uuid = gattService.getUuid().toString();
            service_name = GattAttributes.lookup(uuid, "Unkown service");
            currentServiceData.put(LIST_NAME, service_name);
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            // Log
            Log.d(TAG, "\n" + service_name + ": " + uuid);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<>();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<>();

                uuid = gattCharacteristic.getUuid().toString();
                characteristic_name = GattAttributes.lookup(uuid, "Unkown characteristics");
                currentCharaData.put(LIST_NAME, characteristic_name);
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);

                // Log
                Log.d(TAG, "\t" + characteristic_name + ": " + uuid);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_GATT_CONNECTED);
        intentFilter.addAction(Constant.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(Constant.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(Constant.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public void setOnMessageReceived(OnMessageReceived onMessageReceived) {
        this.onMessageReceived = onMessageReceived;
    }

    public void setOnConnected(OnConnected onConnected) {
        this.onConnected = onConnected;
    }
}
