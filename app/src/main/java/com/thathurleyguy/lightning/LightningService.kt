package com.thathurleyguy.lightning

import android.util.Base64

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.bind.DateTypeAdapter
import com.thathurleyguy.lightning.models.Command
import com.thathurleyguy.lightning.models.InfraredDevice
import com.thathurleyguy.lightning.models.WemoDevice
import com.thathurleyguy.lightning.models.XbmcDevice

import java.util.Date
import java.util.UUID

import retrofit.RequestInterceptor
import retrofit.RestAdapter
import retrofit.client.Response
import retrofit.converter.GsonConverter
import retrofit.http.Body
import retrofit.http.GET
import retrofit.http.POST
import retrofit.http.Path
import rx.Observable

public object LightningService {
    public interface LightningAPI {
        GET("/wemo_devices.json")
        public fun listWemoDevices(): Observable<List<WemoDevice>>

        POST("/wemo_devices/{device}/toggle.json")
        public fun toggleDevice(Path("device") device: Int): Observable<Response>

        GET("/infrared_devices.json")
        public fun listInfraredDevices(): Observable<List<InfraredDevice>>

        POST("/infrared_devices/{device}/commands.json")
        public fun sendIRCommand(Path("device") device: Int?, Body command: Command): Observable<Response>

        GET("/xbmc_devices.json")
        public fun listXbmcDevices(): Observable<List<XbmcDevice>>

        POST("/xbmc_devices/{device}/commands.json")
        public fun sendXbmcCommand(Path("device") device: Int?, Body command: Command): Observable<Response>
    }

    private val API_URL = "https://thathurleyguy.com/lightning"

    private val gsonHandler = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).registerTypeAdapter(javaClass<Date>(), DateTypeAdapter()).create()

    private val REST_ADAPTER = RestAdapter.Builder().setEndpoint(API_URL).setLogLevel(RestAdapter.LogLevel.NONE).setConverter(GsonConverter(gsonHandler)).setRequestInterceptor(object : RequestInterceptor {
        override fun intercept(request: RequestInterceptor.RequestFacade) {
            request.addHeader("Authorization", "Basic ${BuildConfig.LIGHTNING_PASS}")
        }
    }).build()

    public val service: LightningAPI = REST_ADAPTER.create<LightningAPI>(javaClass<LightningAPI>())
}
