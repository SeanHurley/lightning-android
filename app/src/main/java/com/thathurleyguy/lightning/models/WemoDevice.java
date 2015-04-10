package com.thathurleyguy.lightning.models;

import java.util.UUID;

public class WemoDevice {
    private int id;
    private String name;
    private boolean poweredOn;
    private boolean ipAddress;

    public WemoDevice(int id, String name, boolean poweredOn, boolean ipAddress) {
        this.id = id;
        this.name = name;
        this.poweredOn = poweredOn;
        this.ipAddress = ipAddress;
    }

    public boolean isIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(boolean ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPoweredOn() {
        return poweredOn;
    }

    public void setPoweredOn(boolean poweredOn) {
        this.poweredOn = poweredOn;
    }
}
