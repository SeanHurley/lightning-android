package com.thathurleyguy.lightning.models;

public class Command {
    public static final String POWER = "power";
    public static final String VOLUME_UP = "volume_up";
    public static final String VOLUME_DOWN = "volume_down";

    private String command;

    public Command() {

    }

    public Command(String command) {
        this.command = command;
    }
}
