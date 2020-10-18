package com.lubenard.dingos;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class LandingPageFragment extends Fragment {

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ReceiveBtDatas bluetoothDataReceiver;

    private void connectToBluetooth() {
        // Get SharedPreference to see if Bluetooth address is already registered
        SharedPreferences bluetooth_prefs = getActivity().getSharedPreferences("BLUETOOTH_RELATED", Context.MODE_PRIVATE);
        String bluetooth_addr = bluetooth_prefs.getString("BLUETOOTH_ADDR", null);
        if (bluetooth_addr != null) {
            // Bluetooth address already registered, try to connect to it
            Log.d("BLUETOOTH", "Connecting to " + bluetooth_addr);

            bluetoothDataReceiver = new ReceiveBtDatas();
            if (bluetoothDataReceiver.connect(bluetooth_addr) == 1) {
                Toast.makeText(getContext(), getContext().getString(R.string.bluetooth_toast_error), Toast.LENGTH_LONG).show();
            } /*else {
                try {
                    bluetoothDataReceiver.listenForDatas();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
        } else {
            Log.d("BLUETOOTH", "Device not registered, displaying Bluetooth Page...");
            // Bluetooth address not registered, display bluetooth devices
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            BluetoothFragment fragment = new BluetoothFragment();
            fragmentTransaction.replace(android.R.id.content, fragment);
            fragmentTransaction.commit();
        }
    }

    private int isBluetoothTurnedOn() {
        if (mBluetoothAdapter == null) {
            // Bluetooth does not exist on this device
            Log.e("BLUETOOTH","Error. It seems bluetooth does not exist on this device");
            return -1;
        } else if (!mBluetoothAdapter.isEnabled()) {
            // Bluetooth is not activated
            return 1;
        } else {
            // Bluetooth is activated
            return 0;
        }
    }

    private boolean isBluetoothConnected() {
        switch (isBluetoothTurnedOn()) {
            case -1:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle(getContext().getString(R.string.error));
                alertDialogBuilder.setMessage(getContext().getString(R.string.bluetooth_does_not_exist));
                alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case 0:
                connectToBluetooth();
                break;
            case 1:
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
                connectToBluetooth();
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                connectToBluetooth();
            }
            if (resultCode == RESULT_CANCELED) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setPositiveButton(this.getString(R.string.bluetooth_required), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finish();
                    }
                });
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.landing_fragment, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_landing_page, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AboutFragment fragment = new AboutFragment();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button startSession = view.findViewById(R.id.startSessionButton);
        TextView textView = view.findViewById(R.id.welcomeTextView);

        startSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBluetoothConnected();
            }
        });
    }
}
