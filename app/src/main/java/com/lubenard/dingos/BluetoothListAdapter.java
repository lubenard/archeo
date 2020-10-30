package com.lubenard.dingos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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

    private void saveMacAddr(String macToSave) {
        // Save preferences for auto reconnection
        SharedPreferences prefs = activity.getPreferences(Context.MODE_PRIVATE);
        prefs.edit().putString("BLUETOOTH_ADDR", macToSave).apply();
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
                    alertDialogBuilder.setTitle(context.getString(R.string.bluetooth_warning_title));
                    alertDialogBuilder.setMessage(context.getString(R.string.bluetooth_warning_text));
                    alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            ReceiveBtDatas bluetoothDataReceiver = new ReceiveBtDatas();
                            if (bluetoothDataReceiver.connect(device.deviceMacAddr) == 1){
                                Toast.makeText(context, context.getString(R.string.bluetooth_toast_error), Toast.LENGTH_LONG).show();
                                Log.d("BLUETOOTH", "Connection failed! ");
                            } else {
                                Log.d("BLUETOOTH", "Connection successful");
                                Toast.makeText(context, context.getString(R.string.bluetooth_toast_success), Toast.LENGTH_LONG).show();
                                Log.d("BLUETOOTH", "Connected to " + device.deviceMacAddr);
                                saveMacAddr(device.deviceMacAddr);
                                BluetoothFragment.setBluetoothDataReceiver(bluetoothDataReceiver);
                                BluetoothFragment.changeForWaitScan();
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
            TextView status = convertView.findViewById(R.id.elem_bluetooth_status);

            deviceName.setText(device.deviceName);
            macAddr.setText(device.deviceMacAddr);
            status.setText(device.deviceStatus);
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
