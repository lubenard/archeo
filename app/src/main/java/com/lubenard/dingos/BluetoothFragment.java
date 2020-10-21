package com.lubenard.dingos;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothFragment extends Fragment {

    private static BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private ArrayList<BluetoothElementHandling> deviceItemList = new ArrayList<BluetoothElementHandling>();
    private View mainView;
    private static FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.bluetooth_fragment, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bluetooth_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reload_bt:
                Toast.makeText(getContext(), getContext().getString(R.string.reload_bt), Toast.LENGTH_SHORT).show();
                deviceItemList.clear();
                discoverBt();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void setFragmentManager(FragmentManager newFragmentManager) {
        fragmentManager = newFragmentManager;
    }

    public static void changeForWaitScan(ReceiveBtDatas bluetoothDataReceiver) {
        // Stop scanning for new devices
        btAdapter.cancelDiscovery();

        Bundle bundle = new Bundle();
        bundle.putSerializable("dataReceiver", (Serializable) bluetoothDataReceiver);
        bundle.putBoolean("launchThread", true);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WaitScan fragment = new WaitScan();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void updateListView() {
        ListView deviceListView = mainView.findViewById(R.id.bluetoothListView);
        BluetoothListAdapter customAdapter = new BluetoothListAdapter(getContext(), getActivity(),deviceItemList);
        deviceListView.setAdapter(customAdapter);
    }

    private void discoverBt() {
        // Get list of already paired devices.
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                BluetoothElementHandling newDevice = new BluetoothElementHandling(device.getName(), device.getAddress(), getContext().getString(R.string.bluetooth_paired));
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

        updateListView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentManager = getFragmentManager();

        mainView = view;

        discoverBt();
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

                int flag = 0;

                for (int i = 0; i < deviceItemList.size(); i++) {
                    if (device.getAddress().equals(deviceItemList.get(i).deviceMacAddr))
                        flag = 1;
                }
                if (flag == 0) {
                    BluetoothElementHandling newDevice = new BluetoothElementHandling(device.getName(), device.getAddress(), getContext().getString(R.string.bluetooth_discovered));
                    deviceItemList.add(newDevice);
                    updateListView();
                }
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
