package com.example.sensore_android_app.interfaces;

import com.example.sensore_android_app.data.model.Temperatura;
import com.example.sensore_android_app.data.model.TemperaturaTable;


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

    @GET("62cf14c9e490d90f007f4225/data")
    Call<TemperaturaTable> getTemperaturaTable(@Header("X-Auth-Token") String token,
                                               @Query("tz") String timeZone,
                                               @Query("start") String start,
                                               @Query("end") String end);
}
