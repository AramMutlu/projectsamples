package com.example.loraapp.adapters;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.loraapp.R;
import com.example.loraapp.interfaces.OnDeviceClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BleDeviceListAdapter extends RecyclerView.Adapter<BleDeviceListAdapter.ViewHolder> {
    private List<BluetoothDevice> mBleDevices;
    private OnDeviceClickListener onDeviceClickListener;

    public BleDeviceListAdapter(ArrayList<BluetoothDevice> bleDevices) {
        this.mBleDevices = bleDevices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BluetoothDevice bluetoothDevice = mBleDevices.get(position);

        holder.tvDeviceName.setText(bluetoothDevice.getName());
        holder.tvMacAddress.setText(bluetoothDevice.getAddress());
        holder.llBluetoothDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeviceClickListener != null) {
                    onDeviceClickListener.onClick(bluetoothDevice);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBleDevices.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llBluetoothDevice)
        LinearLayout llBluetoothDevice;
        @BindView(R.id.tvDeviceName)
        TextView tvDeviceName;
        @BindView(R.id.tvMacAddress)
        TextView tvMacAddress;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }


    /**
     * This method updates the recyclerview when data is added or removed
     *
     * @param devices list with the bluetooth devices
     */
    public void refresh(List<BluetoothDevice> devices) {
        this.mBleDevices = devices;
        notifyDataSetChanged();
    }

    public void setOnDeviceClickListener(OnDeviceClickListener onDeviceClickListener) {
        this.onDeviceClickListener = onDeviceClickListener;
    }


}
