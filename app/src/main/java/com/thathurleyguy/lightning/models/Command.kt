package com.thathurleyguy.lightning.models

public class Command(private val command: String) {

    companion object {
        public val POWER: String = "power"
        public val VOLUME_UP: String = "volume_up"
        public val VOLUME_DOWN: String = "volume_down"
        public val INPUT_GAME: String = "game"
        public val INPUT_XBMC: String = "video1"
        public val XBMC_UP: String = "up"
        public val XBMC_DOWN: String = "down"
        public val XBMC_LEFT: String = "left"
        public val XBMC_RIGHT: String = "right"
        public val XBMC_SELECT: String = "select"

        public val XBMC_PAUSE: String = "pause"
        public val XBMC_BACK: String = "back"
        public val XBMC_MENU: String = "osd"
    }
}
