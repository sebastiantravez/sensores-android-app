package com.example.sensore_android_app.ui.home;

import static com.example.sensore_android_app.utils.Const.TIME_ZONE;
import static com.example.sensore_android_app.utils.Const.TOKEN;
import static com.example.sensore_android_app.utils.Const.URL;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sensore_android_app.R;
import com.example.sensore_android_app.data.model.Humedad;
import com.example.sensore_android_app.interfaces.HumedadApi;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

import kotlin.reflect.jvm.internal.impl.protobuf.Internal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private Retrofit retrofit;

    EditText txtFechaInicio = null;
    EditText txtFechaFin = null;
    DatePicker datePicker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaFin = findViewById(R.id.txtFechaFin);

        datePicker = findViewById(R.id.datePicker);

        txtFechaInicio.setText(getFechaInicial());
        txtFechaFin.setText(getFechaInicial());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                txtFechaInicio.setText(getFechaInicial());
                txtFechaFin.setText(getFechaInicial());
                datePicker.setVisibility(View.GONE);
            });
        }
        init();
        getHumedad(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
    }

    private String getFechaInicial() {
        String dia;
        if (datePicker.getDayOfMonth() <= 9) {
            dia = "0" + datePicker.getDayOfMonth();
        } else {
            dia = String.valueOf(datePicker.getDayOfMonth());
        }
        String mes = String.valueOf(datePicker.getMonth() + 1);
        String year = String.valueOf(datePicker.getYear());
        return year + "-" + mes + "-" + dia;
    }

    public void mostrarCalendarioInicio(View view) {
        datePicker.setVisibility(View.VISIBLE);
    }

    public void mostrarCalendarioFin(View view) {
        datePicker.setVisibility(View.VISIBLE);
    }

    private void init() {
        retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(JacksonConverterFactory.create(new ObjectMapper())).build();
    }

    public void aplicarFiltros(View view) {
        getHumedad(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
    }

    public void getHumedad(String fechaInicio, String fechaFin) {
        try {
            Date dateStart = df.parse(fechaInicio);
            Date dateEnd = df.parse(fechaFin);
            long startTime = dateStart.getTime();
            long endTime = dateEnd.getTime();
            HumedadApi humedadApi = retrofit.create(HumedadApi.class);
            Call<Humedad> humedadCall = humedadApi.getHumedad(TOKEN, TIME_ZONE, Long.toString(startTime), Long.toString(endTime));
            humedadCall.enqueue(new Callback<Humedad>() {
                @Override
                public void onResponse(Call<Humedad> call, Response<Humedad> response) {
                    try {
                        if (response.isSuccessful()) {
                            System.out.printf("EXITOOOO/......");
                            System.out.printf(response.body().toString());
                            Toast.makeText(getApplicationContext(), response.body().results.get(0).value.toString(), Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getApplicationContext(), response.body().results.get(0).value.toString(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}