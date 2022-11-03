package com.example.sensore_android_app.interfaces;

import com.example.sensore_android_app.data.model.Humedad;
import com.example.sensore_android_app.data.model.HumedadTable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface HumedadApi {
    @GET("62b23ff00844204939c3598b/data")
    Call<Humedad> getHumedad(@Header("X-Auth-Token") String token,
                             @Query("tz") String timeZone,
                             @Query("start") String start,
                             @Query("end") String end);

    @GET("62cf1498e490d90f01cf0b98/data")
    Call<HumedadTable> getHumedadTable(@Header("X-Auth-Token") String token,
                                  @Query("tz") String timeZone,
                                  @Query("start") String start,
                                  @Query("end") String end);
}
