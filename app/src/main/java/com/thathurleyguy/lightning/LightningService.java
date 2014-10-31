package com.thathurleyguy.lightning;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

public interface LightningService {
    @GET("/wemo_devices.json")
    Observable<List<WemoDevice>> listWemoDevices();

    @POST("/wemo_devices/{device}/toggle.json")
    Observable<Response> toggleDevice(@Path("device") long device);
}
