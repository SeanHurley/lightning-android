package com.thathurleyguy.lightning.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView

import com.thathurleyguy.lightning.LightningService
import com.thathurleyguy.lightning.R
import com.thathurleyguy.lightning.models.WemoDevice

import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

public class WemoButton(context: Context, device: WemoDevice) : LinearLayout(context) {
    init {
        View.inflate(context, R.layout.wemo_button, this)
        this.setLayoutParams(LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f))
        val toggleSwitch = findViewById(R.id.switch_button) as Switch
        val label = findViewById(R.id.text_label) as TextView

        label.setText(device.name)
        toggleSwitch.setChecked(device.poweredOn)

        toggleSwitch.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                LightningService.service.toggleDevice(device.id).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe()
            }
        })
    }
}
