package com.thathurleyguy.lightning

import android.app.Activity

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast

import com.thathurleyguy.lightning.models.Command
import com.thathurleyguy.lightning.models.InfraredDevice
import com.thathurleyguy.lightning.models.WemoDevice
import com.thathurleyguy.lightning.views.WemoButton

import java.util.HashMap
import java.util.UUID

import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

import kotlinx.android.synthetic.activity_main.*


public class MainActivity : Activity() {
    private var vibrator: Vibrator? = null
    private val switches = HashMap<Int, WemoButton>()
    private var infraredDevice: InfraredDevice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        getWemoDevices()
        getInfraredDevice()

        btn_game.setOnClickListener { sendInfraredCommand(Command.INPUT_GAME) }
        btn_movie.setOnClickListener { sendInfraredCommand(Command.INPUT_XBMC) }
        btn_power.setOnClickListener { sendInfraredCommand(Command.POWER) }
        btn_something.setOnClickListener { sendInfraredCommand(Command.VOLUME_DOWN) }
        btn_volume_down.setOnClickListener { sendInfraredCommand(Command.VOLUME_DOWN) }
        btn_volume_up.setOnClickListener { sendInfraredCommand(Command.VOLUME_UP) }

        btn_mute.setOnClickListener { sendInfraredCommand(Command.VOLUME_DOWN) }
        btn_tv.setOnClickListener { sendInfraredCommand(Command.INPUT_XBMC) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.getItemId()
        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addSwitch(device: WemoDevice): WemoButton {
        val button = WemoButton(this@MainActivity, device)
        wemo_dock.addView(button)
        return button
    }

    private fun getWemoDevices() {
        LightningService.service.listWemoDevices().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe(object : Observer<List<WemoDevice>> {
            override fun onNext(wemoDevices: List<WemoDevice>) {
                for (device in wemoDevices) {
                    println(device.name + " - " + device.id + " = " + device.poweredOn)
                    val button = addSwitch(device)
                    switches.put(device.id, button)
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }

    public fun getInfraredDevice() {
        LightningService.service.listInfraredDevices().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<List<InfraredDevice>> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onNext(infraredDevices: List<InfraredDevice>) {
                infraredDevice = infraredDevices.get(0)
            }
        })
    }

    public fun sendInfraredCommand(command: String) {
        this.vibrator!!.vibrate(50)
        LightningService.service.sendIRCommand(infraredDevice!!.id, Command(command)).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }
}
