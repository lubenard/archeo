package com.lubenard.dingos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ReceiveBtDatas bluetoothDataReceiver;
    private Button startSession;
    private TextView textView;

    private void connectToBluetooth() {
        // Get SharedPreference to see if Bluetooth address is already registered
        SharedPreferences bluetooth_prefs = getSharedPreferences("BLUETOOTH_RELATED", Context.MODE_PRIVATE);
        String bluetooth_addr = bluetooth_prefs.getString("BLUETOOTH_ADDR", null);
        if (bluetooth_addr != null) {
            // Bluetooth address already registered, try to connect to it
            Log.d("BLUETOOTH", "Connecting to " + bluetooth_addr);

            bluetoothDataReceiver = new ReceiveBtDatas();
            if (bluetoothDataReceiver.connect(bluetooth_addr) == 1) {
                Toast.makeText(this, this.getString(R.string.bluetooth_toast_error), Toast.LENGTH_LONG).show();
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
            FragmentManager fragmentManager = getSupportFragmentManager();
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
                alertDialogBuilder.setTitle(this.getString(R.string.error));
                alertDialogBuilder.setMessage(this.getString(R.string.bluetooth_does_not_exist));
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
                alertDialogBuilder.setPositiveButton(this.getString(R.string.bluetooth_required), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startSession.setVisibility(GONE);
        textView.setVisibility(GONE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AboutFragment fragment = new AboutFragment();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_landing_page, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_fragment);

        startSession = findViewById(R.id.startSessionButton);
        textView = findViewById(R.id.welcomeTextView);

        startSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSession.setVisibility(GONE);
                textView.setVisibility(GONE);
                isBluetoothConnected();
            }
        });
    }
}