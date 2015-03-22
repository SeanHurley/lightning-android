package com.thathurleyguy.lightning.models;

public class Command {
    public static final String POWER = "power";
    public static final String VOLUME_UP = "volume_up";
    public static final String VOLUME_DOWN = "volume_down";
    public static final String INPUT_GAME = "game";
    public static final String INPUT_XBMC = "video1";

    private String command;

    public Command() {

    }

    public Command(String command) {
        this.command = command;
    }
}
