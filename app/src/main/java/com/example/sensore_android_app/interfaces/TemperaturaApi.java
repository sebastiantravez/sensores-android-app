package com.example.sensore_android_app.interfaces;

import static com.example.sensore_android_app.utils.Const.BAR_TEMPERATURA_NAME;
import static com.example.sensore_android_app.utils.Const.LINE_TEMPERATURA_NAME;

import com.example.sensore_android_app.data.model.Temperatura;
import com.example.sensore_android_app.data.model.TemperaturaTable;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TemperaturaApi {
    @GET(BAR_TEMPERATURA_NAME + "/data")
    Call<Temperatura> getTemperatura(@Header("X-Auth-Token") String token,
                                     @Query("tz") String timeZone,
                                     @Query("start") String start,
                                     @Query("end") String end);

    @GET(LINE_TEMPERATURA_NAME + "/data")
    Call<TemperaturaTable> getTemperaturaTable(@Header("X-Auth-Token") String token,
                                               @Query("tz") String timeZone,
                                               @Query("start") String start,
                                               @Query("end") String end);
}
