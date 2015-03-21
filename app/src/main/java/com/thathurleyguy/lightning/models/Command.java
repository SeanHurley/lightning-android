package com.thathurleyguy.lightning.models;

public class Command {
    public static final String TV_POWER = "tv_power";
    public static final String SOUND_POWER = "sound_power";
    public static final String VOLUME_UP = "volume_up";
    public static final String VOLUME_DOWN = "volume_down";

    private String command;

    public Command() {

    }

    public Command(String command) {
        this.command = command;
    }
}
