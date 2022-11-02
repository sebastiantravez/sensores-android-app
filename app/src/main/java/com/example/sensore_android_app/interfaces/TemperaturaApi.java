package com.example.sensore_android_app.interfaces;

import com.example.sensore_android_app.data.model.Temperatura;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TemperaturaApi {
    @GET("62b24027d066dc3ac96bdb3e/data")
    Call<Temperatura> getTemperatura(@Header("X-Auth-Token") String token,
                                     @Query("tz") String timeZone,
                                     @Query("start") String start,
                                     @Query("end") String end);
}
