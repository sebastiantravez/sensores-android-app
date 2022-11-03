package com.example.sensore_android_app.interfaces;

import com.example.sensore_android_app.data.model.Luminosidad;
import com.example.sensore_android_app.data.model.LuminosidadTable;

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

    @GET("62cf1514e490d90beb348fb7/data")
    Call<LuminosidadTable> getLuminosidadTable(@Header("X-Auth-Token") String token,
                                               @Query("tz") String timeZone,
                                               @Query("start") String start,
                                               @Query("end") String end);
}
