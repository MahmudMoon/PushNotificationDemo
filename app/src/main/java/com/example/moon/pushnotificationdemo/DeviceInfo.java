package com.example.moon.pushnotificationdemo;

public class DeviceInfo {
    private String deviceName;
    private String deviceID;
    private String token;

    public DeviceInfo() {

    }

    public DeviceInfo(String deviceName, String deviceID, String token) {
        this.deviceName = deviceName;
        this.deviceID = deviceID;
        this.token = token;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
