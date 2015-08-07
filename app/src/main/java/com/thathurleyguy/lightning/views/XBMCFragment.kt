package com.thathurleyguy.lightning.views

import android.app.Activity

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast

import com.thathurleyguy.lightning.LightningService
import com.thathurleyguy.lightning.R
import com.thathurleyguy.lightning.models.Command
import com.thathurleyguy.lightning.models.XbmcDevice

import butterknife.ButterKnife
import butterknife.OnClick
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

import kotlinx.android.synthetic.fragment_xbmc.*

public class XBMCFragment : Fragment() {
    private var vibrator: Vibrator? = null
    private var xbmcDevice: XbmcDevice? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_xbmc, container, false)
        getXbmcDevice()

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super<Fragment>.onViewCreated(view, savedInstanceState)
        this.vibrator = getActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
    }

    override fun onDetach() {
        super.onDetach()
    }

    public fun getXbmcDevice() {
        LightningService.service.listXbmcDevices().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<List<XbmcDevice>> {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onNext(xbmcDevices: List<XbmcDevice>) {
                xbmcDevice = xbmcDevices.get(0)
            }
        })
    }

    private fun setupListeners() {
        btn_up.setOnClickListener { sendXbmcCommand(Command.XBMC_UP) }
        btn_back.setOnClickListener { sendXbmcCommand(Command.XBMC_BACK) }
        btn_down.setOnClickListener { sendXbmcCommand(Command.XBMC_DOWN) }
        btn_left.setOnClickListener { sendXbmcCommand(Command.XBMC_LEFT) }
        btn_menu.setOnClickListener { sendXbmcCommand(Command.XBMC_MENU) }
        btn_pause.setOnClickListener { sendXbmcCommand(Command.XBMC_PAUSE) }
        btn_right.setOnClickListener { sendXbmcCommand(Command.XBMC_RIGHT) }
        btn_select.setOnClickListener { sendXbmcCommand(Command.XBMC_SELECT) }
    }

    public fun sendXbmcCommand(command: String) {
        this.vibrator!!.vibrate(50)
        LightningService.service.sendXbmcCommand(xbmcDevice!!.id, Command(command)).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }
}
