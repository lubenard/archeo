package com.escatrag.dingos;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothFragment extends Fragment {

    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.bluetooth_fragment, container, false);
    }

    /*private void switchToListenFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    }*/

    public static void launchConnection(String macAddress) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(macAddress);
        ReceiveBtDatas bluetoothDataReceiver = new ReceiveBtDatas(device);
        try {
            bluetoothDataReceiver.listenForDatas();
            Log.d("BLUETOOTH", "Connected to " + macAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //switchToListenFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList deviceItemList = new ArrayList<BluetoothElementHandling>();

        // Get list of already paired devices.
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                BluetoothElementHandling newDevice = new BluetoothElementHandling(device.getName(), device.getAddress());
                Log.d("BLUETOOTH", "Device paired: " + device.getName() + " at address " + device.getAddress());
                deviceItemList.add(newDevice);
            }
        }

        //cancel any prior bt device discovery
        if (btAdapter.isDiscovering()){
            btAdapter.cancelDiscovery();
        }

        // Discover new devices around
        btAdapter.startDiscovery();

        //let's make a broadcast receiver to register our things if devices are found
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getContext().registerReceiver(receiver, filter);

        ListView deviceListView = view.findViewById(R.id.bluetoothListView);
        BluetoothListAdapter customAdapter = new BluetoothListAdapter(getContext(), getActivity(),deviceItemList);
        deviceListView.setAdapter(customAdapter);
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("BLUETOOTH", "Device discovered: " + device.getName() + " at address " + device.getAddress());
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cancel discovery when fragment is destroyed
        btAdapter.cancelDiscovery();
        getContext().unregisterReceiver(receiver);
    }
}
