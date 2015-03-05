package com.thathurleyguy.lightning;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

public class LightningService {
    public interface LightningAPI {
        @GET("/wemo_devices.json")
        Observable<List<WemoDevice>> listWemoDevices();

        @POST("/wemo_devices/{device}/toggle.json")
        Observable<Response> toggleDevice(@Path("device") UUID device);
    }

    private static final String API_URL = "http://thathurleyguy.com:9004";

    private static final Gson gsonHandler = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .create();

    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setConverter(new GsonConverter(gsonHandler))
            .build();

    private static final LightningAPI LIGHTNING_SERVICE = REST_ADAPTER.create(LightningAPI.class);

    public static LightningAPI getService() {
        return LIGHTNING_SERVICE;
    }
}
