package com.escatrag.dingos;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ReceiveBtDatas extends Thread {
    private Boolean isConnectionAlive = true;
    private BluetoothSocket socket;

    private String myUUID = "801e2db8-2910-4429-8283-494393b6c337";

    public ReceiveBtDatas (BluetoothDevice device) {
        // UUID uuid = UUID.fromString(myUUID);
        UUID uuid = UUID.randomUUID();
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void listenForDatas() throws IOException {
        InputStream inputStream = socket.getInputStream();

        try {
            while (isConnectionAlive) {
                if (inputStream.available() > 0) {
                    Log.d("DEBUG", String.format("%c", inputStream.read()));
                } else {
                    Thread.sleep( 100 );
                }
            }
        } catch( Exception exception ) {
            Log.e( "DEBUG", "Cannot read data", exception );
            closeConnection();
        }
    }

    public void closeConnection() {
        isConnectionAlive = false;
    }
}
