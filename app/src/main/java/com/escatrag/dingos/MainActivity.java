package com.escatrag.dingos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private ReceiveBtDatas bluetoothDataReceiver;


    private void connectToBluetooth() {
        // Get SharedPreference to see if Bluetooth address is already registered
        SharedPreferences bluetooth_prefs = getSharedPreferences("BLUETOOTH_RELATED", Context.MODE_PRIVATE);
        String bluetooth_addr = bluetooth_prefs.getString("BLUETOOTH_ADDR", null);
        if (bluetooth_addr != null) {
            // Bluetooth address already registered, try to connect to it
            Log.d("BLUETOOTH", "Connecting to " + bluetooth_addr);

            bluetoothDataReceiver = new ReceiveBtDatas();
            if (bluetoothDataReceiver.connect(bluetooth_addr) == 1) {
                Toast.makeText(this, "We could not connect to device. Are you sure the device is turned on ?", Toast.LENGTH_LONG).show();
            } else {
                try {
                    bluetoothDataReceiver.listenForDatas();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("BLUETOOTH", "Device not registered, displaying Bluetooth Page...");
            // Bluetooth address not registered, display bluetooth devices
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Error!");
                alertDialogBuilder.setMessage("Sorry, bluetooth does not exist on this device. Exiting...");
                alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                connectToBluetooth();
            }
            if (resultCode == RESULT_CANCELED) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setPositiveButton("Bluetooth is needed for this app, exiting now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        isBluetoothConnected();

        /*FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        QuizzFragment fragment = new QuizzFragment();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();*/

        /*if (isBluetoothConnected()) {
            // If permission is granted
            WaitScan fragment = new WaitScan();
            fragmentTransaction.replace(android.R.id.content, fragment);
        } else {
            // If permissions not granted

        }
        fragmentTransaction.commit();*/
    }
}