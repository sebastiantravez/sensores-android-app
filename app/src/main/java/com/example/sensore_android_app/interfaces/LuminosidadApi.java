package com.example.sensore_android_app.interfaces;

import com.example.sensore_android_app.data.model.Luminosidad;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface LuminosidadApi {

    @GET("62b240629f7bba2ac2e70a24/data")
    Call<Luminosidad> getLuminosidad(@Header("X-Auth-Token") String token,
                                    @Query("tz") String timeZone,
                                    @Query("start") String start,
                                    @Query("end") String end);
}
