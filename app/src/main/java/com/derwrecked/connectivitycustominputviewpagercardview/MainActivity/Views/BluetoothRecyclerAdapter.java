package com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.derwrecked.connectivitycustominputviewpagercardview.R;

import java.util.ArrayList;

/**
 * Created by Derek on 3/6/2017.
 */

public class BluetoothRecyclerAdapter extends RecyclerView.Adapter<BluetoothRecyclerAdapter.ViewHolder> {
    private ArrayList<BluetoothRecyclerItem> deviceList;

    public BluetoothRecyclerAdapter(ArrayList<BluetoothRecyclerItem> deviceList) {
        this.deviceList = deviceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_recycler_item, parent, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView deviceName;
        public TextView deviceUUID;
        public TextView deviceInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            this.deviceName = (TextView) itemView.findViewById(R.id.BT_recycler_item_device_name);
            this.deviceUUID = (TextView) itemView.findViewById(R.id.BT_recycler_item_device_UUID);
            this.deviceInfo = (TextView) itemView.findViewById(R.id.BT_recycler_item_device_info);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.deviceName.setText(deviceList.get(position).getDeviceName());
        holder.deviceUUID.setText(deviceList.get(position).getDeviceUUID());
        holder.deviceInfo.setText(deviceList.get(position).getDeviceInfo());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }


}
