package com.escatrag.dingos;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.ArrayList;

public class BluetoothListAdapter implements ListAdapter {

    private ArrayList<BluetoothElementHandling> list;
    private Context context;
    private Activity activity;

    public BluetoothListAdapter(Context getcontext, Activity getActivity, ArrayList<BluetoothElementHandling> listArray) {
        list = listArray;
        context = getcontext;
        activity = getActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final BluetoothElementHandling device = list.get(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.bluetooth_listview_elem, parent, false);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("BLUETOOTH", "Trying to connect to macAddress " + device.deviceMacAddr);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Warning");
                    alertDialogBuilder.setMessage("Do you want to connect to this device ? This setting will be remembered");
                    alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Save preferences for auto reconnection
                            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("BLUETOOTH_ADDR", device.deviceMacAddr);
                            editor.apply();
                            Toast.makeText(context, "Preference saved! ", Toast.LENGTH_SHORT).show();
                            dialog.cancel();

                            ReceiveBtDatas bluetoothDataReceiver = new ReceiveBtDatas();
                            if (bluetoothDataReceiver.connect(device.deviceMacAddr) == 1){
                                Toast.makeText(context, "We could not connect to device. Are you sure the device is turned on ?", Toast.LENGTH_LONG).show();
                                Log.d("BLUETOOTH", "Connection failed! ");
                            } else {
                                Log.d("BLUETOOTH", "Connection successful! ");
                                Toast.makeText(context, "Connection successful !", Toast.LENGTH_LONG).show();

                                Log.d("BLUETOOTH", "Connected to " + device.deviceMacAddr);
                                BluetoothFragment.changeForWaitScan(bluetoothDataReceiver);
                            }
                        }
                    });
                    alertDialogBuilder.setNegativeButton(android.R.string.no, null);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

            TextView deviceName = convertView.findViewById(R.id.bluetooth_device_name);
            TextView macAddr = convertView.findViewById(R.id.elem_bluetooth_mac);

            deviceName.setText(device.deviceName);
            macAddr.setText(device.deviceMacAddr);
        }
        return convertView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }
}
