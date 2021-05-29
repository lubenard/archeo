package com.lubenard.dingos;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class WaitScan extends Fragment {
    private static ArrayList<Integer> elementDiscoveredArray = new ArrayList<>();
    private static View curView;
    private static Activity curActivity;
    private static Context curContext;

    private static FragmentManager fragmentManager;

    private static int itemIndexChoice;
    private static int videoPathChoice;
    private static int shouldQuizzLaunch;

    private static Boolean isConnectionAlive;
    private static BluetoothSocket socket;
    private static ReceiveBtDatas bluetoothDataReceiver;

    private static int elementDiscoveredCounter = 0;

    private static Thread runningThread;

    private static int isInDebugMode;

    private static boolean hasIntroBeenScanned = false;
    private static boolean error = false;
    private static boolean hasFinalQuizzBeenDone = false;

    private final static String TAG = "WaitScan";

    private static final int[] resArray = new int[] {R.raw.intro, R.raw.avant_bras, R.raw.coxaux,
            R.raw.crane, R.raw.femur, R.raw.humerus, R.raw.chronologie, R.raw.contenant, R.raw.tibia};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.waiting_for_scan_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        curView = view;
        curActivity = getActivity();
        curContext = getContext();
        fragmentManager = getFragmentManager();

        Toolbar toolbar = view.findViewById(R.id.wait_scan_toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ListVideo fragment = new ListVideo();
            fragmentTransaction.replace(android.R.id.content, fragment);
            fragmentTransaction.commit();
            return false;
        });

        ((TextView) curView.findViewById(R.id.element_discovered)).setText(elementDiscoveredArray.size() + "/8");

        loadProgress();

        if (elementDiscoveredArray.size() == 0)
            ((TextView) view.findViewById(R.id.wait_scan_main_message)).setText(getContext().getString(R.string.launch_intro));
        else if (elementDiscoveredCounter == 8)
            ((TextView)view.findViewById(R.id.wait_scan_main_message)).setText(getContext().getString(R.string.launch_photo));

        if (isInDebugMode == 0) {
            bluetoothDataReceiver = BluetoothFragment.getBluetoothDataReceiver();
            Log.d(TAG, "Is connection still valid after transition :" + bluetoothDataReceiver.getConnectionStatus());
            Log.d(TAG, "isConnectionAlive = " + isConnectionAlive + " setting it to true");
            isConnectionAlive = true;
            threadReadData();
        }
    }

    private void loadProgress() {
        String userProgress = getActivity().getPreferences(Context.MODE_PRIVATE).getString("DISCOVERED_PROGRESS", null);
        if (userProgress != null) {
            Log.d(TAG, "User Progress has been found! " + userProgress);
            Type listType = new TypeToken<ArrayList<Integer>>(){}.getType();
            elementDiscoveredArray = new Gson().fromJson(userProgress, listType);
            if (elementDiscoveredArray.contains(0)) {
                hasIntroBeenScanned = true;
            }
            if (elementDiscoveredArray.size() > 1)
                elementDiscoveredCounter = elementDiscoveredArray.size() - 1;
            if (elementDiscoveredArray.size() > 8)
                elementDiscoveredCounter = 8;
            // Update textView
            ((TextView) curView.findViewById(R.id.element_discovered)).setText(elementDiscoveredCounter + "/8");
        }
    }

    private static void commitTransition() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    public static int getItemChoice() {
        return itemIndexChoice;
    }

    public static int getVideoPathChoice() {
        return videoPathChoice;
    }

    public static ArrayList<Integer> getElementDiscoveredArray() {
        return elementDiscoveredArray;
    }

    public static int[] getResArray() {
        return resArray;
    }

    public static int getShouldQuizzLaunch() { return shouldQuizzLaunch;}

    public static void setShouldQuizzLaunch(int newValue) {shouldQuizzLaunch = newValue;}

    public static void setIsConnectionAlive(boolean newValue) { isConnectionAlive = newValue; }

    public static void setDebugMode(int newStatus) {
        isInDebugMode = newStatus;
    }
    public static int getDebugMode() {
        return isInDebugMode;
    }


    public static void interruptThread() {
        if (runningThread != null) {
            runningThread.interrupt();
        }
    }

    public static void setItemChoice(int itemIndex, int videoPath){
        itemIndexChoice = itemIndex;
        videoPathChoice = videoPath;
    }

    public static ReceiveBtDatas getBluetoothDataReceiver() {
        return bluetoothDataReceiver;
    }

    private static void saveProgress() {
        Gson gson = new Gson();
        String elementDiscoveredJson = gson.toJson(elementDiscoveredArray);
        SharedPreferences prefs = curActivity.getPreferences(Context.MODE_PRIVATE);
        prefs.edit().putString("DISCOVERED_PROGRESS", elementDiscoveredJson).apply();
    }

    private void toastInsideThread(final String text) {
        curActivity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(curContext, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    // SCANS ARE IN THE FOLLOWING ORDER:
    // 0 - Intro
    // [1 -> 8] - Discovery videos
    // 9 - Final quizz
    // 10 - Pause/Resume videoPlayer
    private void threadReadData() {
        runningThread = new Thread() {
            public void run() {
                if (runningThread.isInterrupted()) {
                    Log.d(TAG, "Thread has been stopped");
                    try {
                        throw new InterruptedException("Thread stopped");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                socket = bluetoothDataReceiver.getSocket();
                isConnectionAlive = bluetoothDataReceiver.getConnectionStatus();
                InputStream inputStream = null;
                try {
                    inputStream = socket.getInputStream();
                    while (isConnectionAlive) {
                        int dataRead = inputStream.read();
                        Log.d(TAG, "Looking for datas");
                        Log.d(TAG, "Datas available: " + String.format("%c", dataRead));
                        if (dataRead >= 48 && dataRead <= 58 && !VideoPlayerFragment.getIsInsideVideo()) {
                            int elementRead = dataRead - 48;
                            Log.d(TAG, "Valid card! elementRead = " + elementRead);
                            // The first card HAS TO BE intro
                            if (!hasIntroBeenScanned && elementRead != 0) {
                                Log.d(TAG, "This is not the right card to pass right now");
                                toastInsideThread(curContext.getString(R.string.not_right_card));
                                error = true;
                            } else if (elementRead == 0) {
                                hasIntroBeenScanned = true;
                                error = false;
                                setShouldQuizzLaunch(1);
                            } else if (elementRead == 9) {
                                Log.d(TAG, "Element read is 10");
                                if (elementDiscoveredArray.size() == 9) {
                                    Log.d(TAG, "Element read is 10 and size of elementArraydiscovered is 9");
                                    // Transition to Final Quizz
                                    setIsConnectionAlive(false);
                                    hasFinalQuizzBeenDone = true;
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    FinalQuizz fragment = new FinalQuizz();
                                    fragmentTransaction.replace(android.R.id.content, fragment);
                                    fragmentTransaction.commit();
                                } else {
                                    toastInsideThread(curContext.getString(R.string.not_all_video_scanned));
                                    error = true;
                                }
                            } else if (elementRead > 0 && elementRead < 9) {
                                setShouldQuizzLaunch(1);
                                error = false;
                            }
                            if (elementRead != 10) {
                                if (!error && !VideoPlayerFragment.getIsInsideVideo()) {
                                    if (!elementDiscoveredArray.contains(elementRead)) {
                                        Log.d(TAG, "Element is not contained into element already discovered");
                                        // Add discovered element into array
                                        elementDiscoveredArray.add(elementRead);
                                        //Save the new array into pref
                                        saveProgress();
                                        // Update counter only if it belong to quizz questions
                                        if (getShouldQuizzLaunch() == 1) {
                                            Log.d(TAG, "Updating elementDiscoveredCounter for elementRead " + elementRead);
                                            ((TextView) curView.findViewById(R.id.element_discovered)).setText(++elementDiscoveredCounter + "/8");
                                        }
                                        Log.d(TAG, "set elementRead = " + elementRead);
                                        //Prepare elements for video + quizz only of not quizz
                                        setItemChoice(elementRead, resArray[elementRead]);
                                        commitTransition();
                                    } else {
                                        toastInsideThread(curContext.getString(R.string.already_discovered_elemment));
                                    }
                                }
                            }
                        } else if (dataRead == 58) {
                            if (VideoPlayerFragment.getIsInsideVideo()) {
                                Log.d(TAG, "I should set pause/unpause on video");
                                VideoPlayerFragment.setVideoPlayerStatus();
                            } else
                                toastInsideThread(curContext.getString(R.string.currently_inside_video));
                        } else {
                            if (VideoPlayerFragment.getIsInsideVideo())
                                Log.d(TAG, "The card is passed during video and is not play/pause");
                            else
                                Log.d(TAG, "This card is not between 48 and 57. It's code actually is " + dataRead);
                            toastInsideThread(curContext.getString(R.string.bad_card_code));
                        }
                    }
                } catch (InterruptedIOException e) {
                    Log.d(TAG, "Thead has been stopped / Interrupted");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        runningThread.start();
    }
}