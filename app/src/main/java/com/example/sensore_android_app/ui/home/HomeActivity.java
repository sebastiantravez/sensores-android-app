package com.example.sensore_android_app.ui.home;

import static com.example.sensore_android_app.utils.Const.TIME_ZONE;
import static com.example.sensore_android_app.utils.Const.TOKEN;
import static com.example.sensore_android_app.utils.Const.URL;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sensore_android_app.R;
import com.example.sensore_android_app.data.model.Humedad;
import com.example.sensore_android_app.interfaces.HumedadApi;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getHumedad();
    }

    public void getHumedad() {
        try {
            String start = "2020-11-02 01:15:00";
            String end = "2022-11-24 01:04:00";
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateStart = df.parse(start);
            Date dateEnd = df.parse(end);
            long startTime = dateStart.getTime();
            long endTime = dateEnd.getTime();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(JacksonConverterFactory.create(new ObjectMapper())).build();
            HumedadApi humedadApi = retrofit.create(HumedadApi.class);
            Call<Humedad> humedadCall = humedadApi.getHumedad(TOKEN, TIME_ZONE, Long.toString(startTime), Long.toString(endTime));
            humedadCall.enqueue(new Callback<Humedad>() {
                @Override
                public void onResponse(Call<Humedad> call, Response<Humedad> response) {
                    try {
                        if (response.isSuccessful()) {
                            System.out.printf("EXITOOOO/......");
                            System.out.printf(response.body().toString());
                            Toast.makeText(getApplicationContext(), response.body().getResults().get(0).getValue().toString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Humedad> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {

        }
    }
}