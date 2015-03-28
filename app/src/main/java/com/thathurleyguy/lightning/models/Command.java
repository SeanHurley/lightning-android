package com.thathurleyguy.lightning.models;

public class Command {
    public static final String POWER = "power";
    public static final String VOLUME_UP = "volume_up";
    public static final String VOLUME_DOWN = "volume_down";
    public static final String INPUT_GAME = "game";
    public static final String INPUT_XBMC = "video1";
    public static final String XBMC_UP = "up";
    public static final String XBMC_DOWN = "down";
    public static final String XBMC_LEFT = "left";
    public static final String XBMC_RIGHT = "right";
    public static final String XBMC_SELECT = "select";

    public static final String XBMC_PAUSE = "pause";
    public static final String XBMC_BACK = "back";
    public static final String XBMC_MENU = "osd";

    private String command;

    public Command() {

    }

    public Command(String command) {
        this.command = command;
    }
}
