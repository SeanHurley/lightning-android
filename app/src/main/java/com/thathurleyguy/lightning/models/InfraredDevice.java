package com.thathurleyguy.lightning.models;

import java.util.UUID;

public class InfraredDevice {
    private UUID id;

    public InfraredDevice(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
