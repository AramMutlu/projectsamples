package com.example.loraapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loraapp.adapters.MessageAdapter;
import com.example.loraapp.global.model.command.Command;
import com.example.loraapp.global.model.command.NewMessage;
import com.example.loraapp.global.model.command.RequestMessage;
import com.example.loraapp.global.model.command.VectorClock;
import com.example.loraapp.global.model.message.Alarm;
import com.example.loraapp.global.model.message.BaseAlarm;
import com.example.loraapp.global.model.packet.DataPacket;
import com.example.loraapp.global.model.packet.PacketPart;
import com.example.loraapp.interfaces.OnMessageClick;
import com.example.loraapp.lora.AlertCode;
import com.example.loraapp.lora.Message;
import com.example.loraapp.lora.NetworkReceiver;
import com.example.loraapp.lora.NetworkStatus;
import com.example.loraapp.lora.OnNetworkReceiver;
import com.example.loraapp.lora.bluetooth.BluetoothController;
import com.example.loraapp.lora.wlan.OnMessageReceived;
import com.example.loraapp.lora.wlan.UDPNetwork;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private int nodeId;
    private VectorClock currentVectorClock;
    private NetworkReceiver networkReceiver;
    private MessageAdapter messageAdapter;
    private List<NewMessage> messages;
    private UDPNetwork udpNetwork;
    private BluetoothController bluetoothController;
    private AlertCode selectedAlert;

    @BindView(R.id.etMessage)
    EditText messageInput;
    @BindView(R.id.etPriority)
    EditText messagePriority;
    @BindView(R.id.ivAlert)
    ImageButton alertView;
    @BindView(R.id.tvAlert)
    TextView alertText;
    @BindView(R.id.btnSend)
    Button sendButton;
    @BindView(R.id.rvReceivedMessages)
    RecyclerView receivedMessages;
    @BindView(R.id.wifiStatus)
    TextView wifiStatus;
    @BindView(R.id.bluetoothStatus)
    TextView bluetoothStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // TODO: get messages from a data instace?
        messages = new ArrayList<>();

        // TODO: get vectorclock from a data instace?
        currentVectorClock = new VectorClock(10);

        //TODO: IDRequest/ data instace
        nodeId = 2;

        // Set recyclerview with message adapter
        receivedMessages.setLayoutManager(new LinearLayoutManager(this));
        receivedMessages.setAdapter(getMessageAdapter());

        // Initialize UDP
        initUDP();

        // Initialize BLE
        initBLE();

        // Check network status
        networkReceiver = new NetworkReceiver(this);
        networkReceiver.setOnNetworkReceiver(new OnNetworkReceiver() {
            @Override
            public void onWifi(NetworkStatus status) {
                updateStatus(status, wifiStatus);
            }

            @Override
            public void onBluetooth(NetworkStatus status) {
                updateStatus(status, bluetoothStatus);
            }
        });
    }

    /**
     * This method initializes the UDP network
     */
    private void initUDP() {
        udpNetwork = new UDPNetwork();
        udpNetwork.setOnMessageReceived(new OnMessageReceived() {
            /**
             * This callback 'll be called when a message has been received
             * @param command the command
             */
            @Override
            public void onReceived(Command command) {
                if(command instanceof NewMessage){
                    NewMessage message = (NewMessage) command;
                    if(message.getAlarm() instanceof Alarm){
                        if(getMessage(message.getAuthorId(), message.getMessageId()) == null){
                            messages.add(message);
                            Integer[][] missingIds = currentVectorClock.getMissingIds(message.getVectorClock());
                            if(missingIds.length > 0){
                                //sending requestMessages
                                for(int i = 0; i < missingIds.length; i++){
                                    if(missingIds[i] != null){
                                        ArrayList<Integer> ids = new ArrayList<Integer>(Arrays.asList(missingIds[i]));
                                        RequestMessage request = new RequestMessage(nodeId, currentVectorClock, i, ids);
                                        sendCommand(request);
                                    }
                                }
                            } else {
                                currentVectorClock.incrementVector(message.getAuthorId());
                            }
                        }
                    } else {
                        //TODO: maybe not remove from the list but flag as canceled
                        NewMessage toRemove = getMessage(message.getNodeId(), message.getMessageId());
                        messages.remove(toRemove);
                    }
                }
                refreshList();
            }
        });
    }

    /**
     * This method initializes the BLE network
     */
    private void initBLE() {
        bluetoothController = BluetoothController.getInstance(this);
        bluetoothController.setOnMessageReceived(new OnMessageReceived() {
            @Override
            public void onReceived(Command command) {
//                messages.add(command);
//                refreshList();
            }
        });
    }
    private NewMessage getMessage(int authorId, int messageId){
        for (NewMessage message: messages) {
            if(message.getAuthorId() == authorId && message.getMessageId() == messageId){
                return message;
            }
        }
        return null;
    }

    /**
     * This method refreshes the recyclerview with the newest data
     */
    private void refreshList() {
        runOnUiThread(() -> messageAdapter.refresh(messages));
    }

    /**
     * This method creates a message adapter
     *
     * @return a message adapter
     */
    private MessageAdapter getMessageAdapter() {
        if (messageAdapter == null) {
            messageAdapter = new MessageAdapter(messages);
            messageAdapter.setOnMessageClick(new OnMessageClick() {
                /**
                 * Build a dialog to show the extended newMessage
                 * @param newMessage the newMessage
                 */
                @Override
                public void onClick(NewMessage newMessage) {
                    // Show extended dialog
                    try {
                        // Format date to day-month-year hours:minutes
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm", new Locale("nl", "NL"));

                        // Create dialog
                        Alarm alarm = (Alarm) newMessage.getAlarm();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        Date date = new java.util.Date((long) alarm.getDateTime()*1000);
                        builder.setTitle(dateFormat.format(date))
                                .setMessage(alarm.getText());
                        AlertDialog dialog = builder.create();

                        //TODO: not important right now
                        // Set background of dialog based on the newMessage color
                        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(newMessage.getAlertCode().getColor()));

                        // Show dialog
                        dialog.show();
                    } catch (Exception e) {
                        // Show error
                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        return messageAdapter;
    }

    /**
     * This method updates the wifi and bluetooth -status textview
     *
     * @param status   the status of the network
     * @param textView textview of the network
     */
    private void updateStatus(final NetworkStatus status, final TextView textView) {
        this.runOnUiThread(() -> {
            String message = "Unkown";

            switch (status) {
                case ENABLED:
                    message = "Enabled";
                    break;
                case DISABLED:
                    message = "Disabled";
                    break;
                case TURNING_OFF:
                    message = "Turning off";
                    break;
                case TURNING_ON:
                    message = "Turning on";
                    break;
            }

            // Update the textView with the message
            textView.setText(message);
        });
    }
    public void sendCommand(Command command){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            command.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<PacketPart> parts = new DataPacket(out.toByteArray(), 20).getPacketParts();
        for (PacketPart part: parts) {
            try {
                udpNetwork.sendPart(part.write());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method sends the message that has been submitted in the edittext
     */
    @OnClick(R.id.btnSend)
    public void sendNewMessage() {
        String msgInput = messageInput.getText().toString();
        int prio = Integer.valueOf(messagePriority.getText().toString());
        // Check if inputfield isn't empty and a alarm is selected
        if (!msgInput.isEmpty() && selectedAlert != null) {
            // Send the message

            Alarm newMessage = new Alarm(BaseAlarm.getAlarmCodefromInt(selectedAlert.getCode()), prio, msgInput);
            NewMessage message = new NewMessage(nodeId, currentVectorClock, newMessage);
            sendCommand(message);

            // Reset the the fields
            selectedAlert = null;
            messageInput.setText("");
            messagePriority.setText("");

        } else {
            // Show error
            String alertMsg = msgInput.isEmpty() ? "Please type a message" : "Please select a alarm code";
            Toast.makeText(this, alertMsg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method shows a dialog with a listView
     * The user can now select a alarm code
     */
    @OnClick(R.id.ivAlert)
    public void selectAlert() {
        // Show extended dialog
        try {
            // Get the alarmcodes
            final List<AlertCode> alertCodes = AlertCode.getAlertCodes();

            // Create a dialog
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setIcon(R.drawable.ic_announcement_black_24dp);
            builderSingle.setTitle("Select a alert");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice);
            for (AlertCode alertCode : alertCodes) {
                arrayAdapter.add(alertCode.getDescription());
            }

            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                /**
                 * Dismiss the dialog
                 * @param dialog
                 * @param which
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                /**
                 * Get the selected alarmcode and update the textview
                 * @param dialog
                 * @param which position of the listView
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedAlert = alertCodes.get(which);

                    alertView.setColorFilter(selectedAlert.getColor());
                    alertText.setText(String.format("Alert '%s' is selected", selectedAlert.getDescription()));
                }
            });

            // Build and show the dialog
            builderSingle.show();
        } catch (Exception e) {
            // Whops an error occurred
            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method 'll be called when the app closes
     */
    @Override
    protected void onStop() {
        super.onStop();

        // Stop the broadcastReceiver
        if (networkReceiver != null) networkReceiver.stop();
    }

    @OnClick(R.id.bluetoothStatus)
    public void startBluetoothActivity() {
        startActivity(new Intent(this, BluetoothActivity.class));
    }
}
