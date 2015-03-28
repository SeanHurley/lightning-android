package com.thathurleyguy.lightning.models;

import java.util.UUID;

public class XbmcDevice {
    private UUID id;

    public XbmcDevice(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
