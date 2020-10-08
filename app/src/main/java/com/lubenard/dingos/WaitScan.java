package com.lubenard.dingos;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.io.InputStream;

public class WaitScan extends Fragment {
    private static int itemIndexChoice;
    private static int videoPathChoice;

    private static Boolean isConnectionAlive;
    private static BluetoothSocket socket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.waiting_for_scan, container, false);
    }

    private void commitTransition() {
        // Disable isConnectionAlive to avoid being able to scan during quizz or video
        isConnectionAlive = false;

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static int getItemChoice() {
        return itemIndexChoice;
    }

    public static int getVideoPathChoice() {
        return videoPathChoice;
    }

    private void setItemChoice(int itemIndex, int videoPath){
        itemIndexChoice = itemIndex;
        videoPathChoice = videoPath;
    }

    private void threadReadData(final ReceiveBtDatas bluetoothDataReceiver) {
        new Thread()
        {
            public void run()
            {
                socket = bluetoothDataReceiver.getSocket();
                isConnectionAlive = bluetoothDataReceiver.getConnectionStatus();
                InputStream inputStream = null;
                try {
                    inputStream = socket.getInputStream();
                    while (isConnectionAlive) {
                        int dataRead = inputStream.read();
                        Log.d("BLUETOOTH", "Datas available: " + String.format("%c", dataRead));
                        switch (dataRead) {
                            case 48: // Intro
                                break;
                            case 49: // Avant-bras (1)
                                setItemChoice(1, R.raw.avant_bras);
                                commitTransition();
                                break;
                            case 50: // Coxaux (2)
                                setItemChoice(2, R.raw.coxaux);
                                commitTransition();
                                break;
                            case 51: // Crane (3)
                                break;
                            case 52: // Femur (4)
                                break;
                            case 53: // Humerus (5)
                                setItemChoice(5, R.raw.humerus);
                                commitTransition();
                                break;
                            case 54: // Objet (6)
                                setItemChoice(6, R.raw.objet);
                                commitTransition();
                                break;
                            case 55: // Réduction (7)
                                setItemChoice(7, R.raw.reduction);
                                commitTransition();
                                break;
                            case 56: // Tibia (8)
                                break;
                            case 57: // Outro
                                break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle.getBoolean("launchThread")) {
            ReceiveBtDatas bluetoothDataReceiver = (ReceiveBtDatas) bundle.getSerializable("dataReceiver");
            Log.d("BLUETOOTH", "Is connection still valid after transition :" + bluetoothDataReceiver.getConnectionStatus());
            threadReadData(bluetoothDataReceiver);
        }
        else {
            Log.d("BLUETOOTH", "No need to launch thread AGAIN, isConnectionAlive = " + isConnectionAlive + " setting it to true");
            isConnectionAlive = true;
        }
    }
}
