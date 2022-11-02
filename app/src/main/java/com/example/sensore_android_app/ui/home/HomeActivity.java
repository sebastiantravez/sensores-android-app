package com.example.sensore_android_app.ui.home;

import static com.example.sensore_android_app.utils.Const.TIME_ZONE;
import static com.example.sensore_android_app.utils.Const.TOKEN;
import static com.example.sensore_android_app.utils.Const.URL;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sensore_android_app.R;
import com.example.sensore_android_app.data.model.Humedad;
import com.example.sensore_android_app.data.model.Luminosidad;
import com.example.sensore_android_app.data.model.Temperatura;
import com.example.sensore_android_app.interfaces.HumedadApi;
import com.example.sensore_android_app.interfaces.LuminosidadApi;
import com.example.sensore_android_app.interfaces.TemperaturaApi;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    DatePicker datePickerInicio = null;
    DatePicker datePickerFin = null;
    ProgressBar progressBar = null;
    Button btnAplicar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaFin = findViewById(R.id.txtFechaFin);

        datePickerInicio = findViewById(R.id.datePickerInicio);
        datePickerFin = findViewById(R.id.datePickerFin);
        progressBar = findViewById(R.id.loading);
        btnAplicar = findViewById(R.id.btnAplicar);

        txtFechaInicio.setText(getFechaInicial());
        txtFechaFin.setText(getFechaInicialFin());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePickerInicio.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                txtFechaInicio.setText(getFechaInicial());
                datePickerInicio.setVisibility(View.GONE);
            });
            datePickerFin.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                txtFechaFin.setText(getFechaInicial());
                datePickerFin.setVisibility(View.GONE);
            });
        }
        init();
        getHumedad(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
        getTemperatura(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
        getLuminosidad(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
    }

    private String getFechaInicial() {
        String dia;
        if (datePickerInicio.getDayOfMonth() <= 9) {
            dia = "0" + datePickerInicio.getDayOfMonth();
        } else {
            dia = String.valueOf(datePickerInicio.getDayOfMonth());
        }
        String mes = String.valueOf(datePickerInicio.getMonth() + 1);
        String year = String.valueOf(datePickerInicio.getYear());
        return year + "-" + mes + "-" + dia;
    }

    private String getFechaInicialFin() {
        String dia;
        if (datePickerFin.getDayOfMonth() <= 9) {
            dia = "0" + datePickerFin.getDayOfMonth();
        } else {
            dia = String.valueOf(datePickerFin.getDayOfMonth());
        }
        String mes = String.valueOf(datePickerFin.getMonth() + 1);
        String year = String.valueOf(datePickerFin.getYear());
        return year + "-" + mes + "-" + dia;
    }

    public void mostrarCalendarioInicio(View view) {
        datePickerInicio.setVisibility(View.VISIBLE);
    }

    public void mostrarCalendarioFin(View view) {
        datePickerFin.setVisibility(View.VISIBLE);
    }

    private void init() {
        retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(JacksonConverterFactory.create(new ObjectMapper())).build();
    }

    public void aplicarFiltros(View view) {
        btnAplicar.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        getHumedad(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
        getTemperatura(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
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
                            Toast.makeText(getApplicationContext(), "Humedad: "+response.body().results.get(0).value.toString(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                        btnAplicar.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        btnAplicar.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Humedad> call, Throwable t) {
                    btnAplicar.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            btnAplicar.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void getTemperatura(String fechaInicio, String fechaFin) {
        try {
            Date dateStart = df.parse(fechaInicio);
            Date dateEnd = df.parse(fechaFin);
            long startTime = dateStart.getTime();
            long endTime = dateEnd.getTime();
            TemperaturaApi temperaturaApi = retrofit.create(TemperaturaApi.class);
            Call<Temperatura> temperaturaCall = temperaturaApi.getTemperatura(TOKEN, TIME_ZONE, Long.toString(startTime), Long.toString(endTime));
            temperaturaCall.enqueue(new Callback<Temperatura>() {
                @Override
                public void onResponse(Call<Temperatura> call, Response<Temperatura> response) {
                    try {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Tempertura: "+response.body().results.get(0).value.toString(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                        btnAplicar.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        btnAplicar.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Temperatura> call, Throwable t) {
                    btnAplicar.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            btnAplicar.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void getLuminosidad(String fechaInicio, String fechaFin){
        try {
            Date dateStart = df.parse(fechaInicio);
            Date dateEnd = df.parse(fechaFin);
            long startTime = dateStart.getTime();
            long endTime = dateEnd.getTime();
            LuminosidadApi luminosidadApi = retrofit.create(LuminosidadApi.class);
            Call<Luminosidad> luminosidadCall = luminosidadApi.getLuminosidad(TOKEN, TIME_ZONE, Long.toString(startTime), Long.toString(endTime));
            luminosidadCall.enqueue(new Callback<Luminosidad>() {
                @Override
                public void onResponse(Call<Luminosidad> call, Response<Luminosidad> response) {
                    try {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Luminosidad: "+response.body().results.get(0).value.toString(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                        btnAplicar.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        btnAplicar.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Luminosidad> call, Throwable t) {
                    btnAplicar.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            btnAplicar.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}