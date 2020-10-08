package com.lubenard.dingos;

public class BluetoothElementHandling {
    String deviceName;
    String deviceMacAddr;
    String deviceStatus;
    public BluetoothElementHandling(String deviceName, String macAddr, String deviceStatus) {
        this.deviceName = deviceName;
        this.deviceMacAddr = macAddr;
        this.deviceStatus = deviceStatus;
    }
}
