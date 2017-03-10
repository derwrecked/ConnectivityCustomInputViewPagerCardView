package com.derwrecked.connectivitycustominputviewpagercardview.MainActivity.Views;

/**
 * Created by Derek on 3/6/2017.
 */

public class BluetoothRecyclerItem {
    private String deviceName;
    private String deviceUUID;
    private String deviceInfo;

    public BluetoothRecyclerItem(String deviceName, String deviceUUID, String deviceInfo) {
        this.deviceName = deviceName;
        this.deviceUUID = deviceUUID;
        this.deviceInfo = deviceInfo;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
