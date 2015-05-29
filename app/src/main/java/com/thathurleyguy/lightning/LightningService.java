package com.thathurleyguy.lightning;

import android.util.Base64;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.thathurleyguy.lightning.models.Command;
import com.thathurleyguy.lightning.models.InfraredDevice;
import com.thathurleyguy.lightning.models.WemoDevice;
import com.thathurleyguy.lightning.models.XbmcDevice;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

public class LightningService {
    public interface LightningAPI {
        @GET("/wemo_devices.json")
        Observable<List<WemoDevice>> listWemoDevices();

        @POST("/wemo_devices/{device}/toggle.json")
        Observable<Response> toggleDevice(@Path("device") int device);

        @GET("/infrared_devices.json")
        Observable<List<InfraredDevice>> listInfraredDevices();

        @POST("/infrared_devices/{device}/commands.json")
        Observable<Response> sendIRCommand(@Path("device") Integer device, @Body Command command);

        @GET("/xbmc_devices.json")
        Observable<List<XbmcDevice>> listXbmcDevices();

        @POST("/xbmc_devices/{device}/commands.json")
        Observable<Response> sendXbmcCommand(@Path("device") Integer device, @Body Command command);
    }

    private static final String API_URL = "https://thathurleyguy.com/lightning";

    private static final Gson gsonHandler = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .create();

    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .setLogLevel(RestAdapter.LogLevel.NONE)
            .setConverter(new GsonConverter(gsonHandler))
            .setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Authorization", "Basic aHVybGV5OmhhaGFwYXNzd29yZA==\\n");
                }
            })
            .build();

    private static final LightningAPI LIGHTNING_SERVICE = REST_ADAPTER.create(LightningAPI.class);

    public static LightningAPI getService() {
        return LIGHTNING_SERVICE;
    }
}
