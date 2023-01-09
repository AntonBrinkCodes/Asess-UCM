package com.example.asessucm.uiutils;

/**
 * Based on Ble-Gatt-Movesense-2.0 from https://gits-15.sys.kth.se/anderslm/Ble-Gatt-Movesense-2.0
 */

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.asessucm.R;

import java.util.List;

public class DeviceRecyclerAdapter extends RecyclerView.Adapter<DeviceRecyclerAdapter.ViewHolder>{
    private List<BluetoothDevice> devices;
    // interface for callbacks when item selected
    public interface IOnItemSelectedCallBack {
        void onItemClicked(int position);
    }

    private IOnItemSelectedCallBack mOnItemSelectedCallBack;

    public DeviceRecyclerAdapter(List<BluetoothDevice> devices, IOnItemSelectedCallBack onItemSelectedCallBack){
        super();
        this.devices = devices;
        this.mOnItemSelectedCallBack = onItemSelectedCallBack;
    }

    @NonNull
    @Override
    public DeviceRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_item, parent, false);
        final ViewHolder vh = new ViewHolder(itemView, mOnItemSelectedCallBack);
        vh.deviceNameView = itemView.findViewById(R.id.device_name);
        vh.deviceIdView = itemView.findViewById(R.id.device_id);

        return vh;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull DeviceRecyclerAdapter.ViewHolder holder, int position) {
        BluetoothDevice device = devices.get(position);
        @SuppressLint("MissingPermission") String name = device.getName();
        holder.deviceNameView.setText(name == null ? "Unknown" : name);
        String tmp = device.getBluetoothClass() + ", " + device.getAddress();
        holder.deviceIdView.setText(tmp);
        Log.i("ScanActivity", "onBindViewHolder");
    }

    @Override
    public int getItemCount() {return devices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView deviceNameView;
        TextView deviceIdView;

        public ViewHolder(@NonNull View itemView, IOnItemSelectedCallBack onItemSelectedCallBack) {
            super(itemView);
            itemView.setOnClickListener(this);
            mOnItemSelectedCallBack = onItemSelectedCallBack;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnItemSelectedCallBack.onItemClicked(position);
        }
    }
}

