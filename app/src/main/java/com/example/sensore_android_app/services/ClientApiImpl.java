package com.example.sensore_android_app.services;

import static com.example.sensore_android_app.utils.Const.TIME_ZONE;
import static com.example.sensore_android_app.utils.Const.TOKEN;
import static com.example.sensore_android_app.utils.Const.URL;

import com.example.sensore_android_app.data.model.Humedad;
import com.example.sensore_android_app.data.model.HumedadTable;
import com.example.sensore_android_app.data.model.Luminosidad;
import com.example.sensore_android_app.data.model.LuminosidadTable;
import com.example.sensore_android_app.data.model.Temperatura;
import com.example.sensore_android_app.data.model.TemperaturaTable;
import com.example.sensore_android_app.interfaces.HumedadApi;
import com.example.sensore_android_app.interfaces.LuminosidadApi;
import com.example.sensore_android_app.interfaces.TemperaturaApi;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ClientApiImpl {

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Retrofit retrofit;

    public ClientApiImpl() {
        retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(JacksonConverterFactory.create(new ObjectMapper())).build();
    }

    public Call<Humedad> getHumedad(String fechaInicio, String fechaFin) {
        try {
            Date dateStart = df.parse(fechaInicio + " 00:00:00");
            Date dateEnd = df.parse(fechaFin + " 23:59:00");
            long startTime = dateStart.getTime();
            long endTime = dateEnd.getTime();
            HumedadApi humedadApi = retrofit.create(HumedadApi.class);
            Call<Humedad> humedadCall = humedadApi.getHumedad(TOKEN, TIME_ZONE, Long.toString(startTime), Long.toString(endTime));
            return humedadCall;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Call<Temperatura> getTemperatura(String fechaInicio, String fechaFin) {
        try {
            Date dateStart = df.parse(fechaInicio + " 00:00:00");
            Date dateEnd = df.parse(fechaFin + " 23:59:00");
            long startTime = dateStart.getTime();
            long endTime = dateEnd.getTime();
            TemperaturaApi temperaturaApi = retrofit.create(TemperaturaApi.class);
            Call<Temperatura> temperaturaCall = temperaturaApi.getTemperatura(TOKEN, TIME_ZONE, Long.toString(startTime), Long.toString(endTime));
            return temperaturaCall;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Call<Luminosidad> getLuminosidad(String fechaInicio, String fechaFin) {
        try {
            Date dateStart = df.parse(fechaInicio + " 00:00:00");
            Date dateEnd = df.parse(fechaFin + " 23:59:00");
            long startTime = dateStart.getTime();
            long endTime = dateEnd.getTime();
            LuminosidadApi luminosidadApi = retrofit.create(LuminosidadApi.class);
            Call<Luminosidad> luminosidadCall = luminosidadApi.getLuminosidad(TOKEN, TIME_ZONE, Long.toString(startTime), Long.toString(endTime));
            return luminosidadCall;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Call<HumedadTable> getHumedadTable(String fechaInicio, String fechaFin) {
        try {
            Date dateStart = df.parse(fechaInicio + " 00:00:00");
            Date dateEnd = df.parse(fechaFin + " 23:59:00");
            long startTime = dateStart.getTime();
            long endTime = dateEnd.getTime();
            HumedadApi humedadApi = retrofit.create(HumedadApi.class);
            Call<HumedadTable> humedadTableCall = humedadApi.getHumedadTable(TOKEN, TIME_ZONE, Long.toString(startTime), Long.toString(endTime));
            return humedadTableCall;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Call<TemperaturaTable> getTemperaturaTable(String fechaInicio, String fechaFin) {
        try {
            Date dateStart = df.parse(fechaInicio + " 00:00:00");
            Date dateEnd = df.parse(fechaFin + " 23:59:00");
            long startTime = dateStart.getTime();
            long endTime = dateEnd.getTime();
            TemperaturaApi temperaturaApi = retrofit.create(TemperaturaApi.class);
            Call<TemperaturaTable> temperaturaTableCall= temperaturaApi.getTemperaturaTable(TOKEN, TIME_ZONE, Long.toString(startTime), Long.toString(endTime));
            return temperaturaTableCall;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Call<LuminosidadTable> getLuminosidadTable(String fechaInicio, String fechaFin) {
        try {
            Date dateStart = df.parse(fechaInicio + " 00:00:00");
            Date dateEnd = df.parse(fechaFin + " 23:59:00");
            long startTime = dateStart.getTime();
            long endTime = dateEnd.getTime();
            LuminosidadApi luminosidadApi = retrofit.create(LuminosidadApi.class);
            Call<LuminosidadTable> luminosidadTableCall = luminosidadApi.getLuminosidadTable(TOKEN, TIME_ZONE, Long.toString(startTime), Long.toString(endTime));
            return luminosidadTableCall;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
