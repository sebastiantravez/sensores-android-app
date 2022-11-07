package com.example.sensore_android_app.interfaces;

import static com.example.sensore_android_app.utils.Const.BAR_HUMEDAD_NAME;
import static com.example.sensore_android_app.utils.Const.LINE_HUMEDAD_NAME;

import com.example.sensore_android_app.data.model.Humedad;
import com.example.sensore_android_app.data.model.HumedadTable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface HumedadApi {
    @GET(BAR_HUMEDAD_NAME + "/data")
    Call<Humedad> getHumedad(@Header("X-Auth-Token") String token,
                             @Query("tz") String timeZone,
                             @Query("start") String start,
                             @Query("end") String end);

    @GET(LINE_HUMEDAD_NAME + "/data")
    Call<HumedadTable> getHumedadTable(@Header("X-Auth-Token") String token,
                                       @Query("tz") String timeZone,
                                       @Query("start") String start,
                                       @Query("end") String end);

}
