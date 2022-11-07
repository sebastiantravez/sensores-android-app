package com.example.sensore_android_app.interfaces;

import static com.example.sensore_android_app.utils.Const.BAR_LUMINOSIDAD_NAME;
import static com.example.sensore_android_app.utils.Const.LINE_LUMINOSIDAD_NAME;

import com.example.sensore_android_app.data.model.Luminosidad;
import com.example.sensore_android_app.data.model.LuminosidadTable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface LuminosidadApi {

    @GET(BAR_LUMINOSIDAD_NAME + "/data")
    Call<Luminosidad> getLuminosidad(@Header("X-Auth-Token") String token,
                                     @Query("tz") String timeZone,
                                     @Query("start") String start,
                                     @Query("end") String end);

    @GET(LINE_LUMINOSIDAD_NAME + "/data")
    Call<LuminosidadTable> getLuminosidadTable(@Header("X-Auth-Token") String token,
                                               @Query("tz") String timeZone,
                                               @Query("start") String start,
                                               @Query("end") String end);
}
