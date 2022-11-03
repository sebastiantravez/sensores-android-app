package com.example.sensore_android_app.ui.home;

import static com.example.sensore_android_app.utils.Const.DURATION;
import static com.example.sensore_android_app.utils.Const.TEXT_SIZE;
import static com.example.sensore_android_app.utils.Const.VALUE_TEXT_SIZE;

import android.graphics.Color;
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
import com.example.sensore_android_app.data.model.HumedadTable;
import com.example.sensore_android_app.data.model.Luminosidad;
import com.example.sensore_android_app.data.model.Results;
import com.example.sensore_android_app.data.model.Temperatura;
import com.example.sensore_android_app.services.ClientApiImpl;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ClientApiImpl clientApi = new ClientApiImpl();

    EditText txtFechaInicio = null;
    EditText txtFechaFin = null;
    DatePicker datePickerInicio = null;
    DatePicker datePickerFin = null;
    ProgressBar progressBar = null;
    Button btnAplicar = null;

    BarChart barChartHum;
    BarChart barChartTem;
    BarChart barChartLum;

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
        barChartHum = findViewById(R.id.barChartHumedad);
        barChartTem = findViewById(R.id.barChartTemperatura);
        barChartLum = findViewById(R.id.barChartLuminosidad);

        txtFechaInicio.setText(getFechaInicial());
        txtFechaFin.setText(getFechaInicialFin());
        txtFechaInicio.setEnabled(false);
        txtFechaFin.setEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePickerInicio.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                txtFechaInicio.setText(getFechaInicial());
                datePickerInicio.setVisibility(View.GONE);
            });
            datePickerFin.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                txtFechaFin.setText(getFechaInicialFin());
                datePickerFin.setVisibility(View.GONE);
            });
        }
        getHumedad(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
        getTemperatura(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
        getLuminosidad(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
        getHumedadTable(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
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
        barChartHum.setVisibility(View.GONE);
        barChartTem.setVisibility(View.GONE);
        barChartLum.setVisibility(View.GONE);
        datePickerInicio.setVisibility(View.VISIBLE);
    }

    public void mostrarCalendarioFin(View view) {
        barChartHum.setVisibility(View.GONE);
        barChartTem.setVisibility(View.GONE);
        barChartLum.setVisibility(View.GONE);
        datePickerFin.setVisibility(View.VISIBLE);
    }

    public void aplicarFiltros(View view) {
        btnAplicar.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        datePickerInicio.setVisibility(View.GONE);
        datePickerFin.setVisibility(View.GONE);
        getHumedad(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
        getTemperatura(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
        getLuminosidad(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
        getHumedadTable(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
    }

    public void getHumedad(String fechaInicio, String fechaFin) {
        try {
            Call<Humedad> humedadCall = clientApi.getHumedad(fechaInicio, fechaFin);
            humedadCall.enqueue(new Callback<Humedad>() {
                @Override
                public void onResponse(Call<Humedad> call, Response<Humedad> response) {
                    try {
                        if (response.isSuccessful()) {
                            getBarCharHumedad(response.body().results);
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
            Call<Temperatura> temperaturaCall = clientApi.getTemperatura(fechaInicio, fechaFin);
            temperaturaCall.enqueue(new Callback<Temperatura>() {
                @Override
                public void onResponse(Call<Temperatura> call, Response<Temperatura> response) {
                    try {
                        if (response.isSuccessful()) {
                             getBarCharTemperatura(response.body().results);
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

    public void getLuminosidad(String fechaInicio, String fechaFin) {
        try {
            Call<Luminosidad> luminosidadCall = clientApi.getLuminosidad(fechaInicio, fechaFin);
            luminosidadCall.enqueue(new Callback<Luminosidad>() {
                @Override
                public void onResponse(Call<Luminosidad> call, Response<Luminosidad> response) {
                    try {
                        if (response.isSuccessful()) {
                            getBarCharLuminosidad(response.body().results);
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

    private void getBarCharHumedad(List<Results> results) {
        List<BarEntry> barEntries = new ArrayList<>();
        if (results.isEmpty() || results.get(0).value == null) {
            barChartHum.setData(null);
            Toast.makeText(getApplicationContext(), "Datos seleccionados sin resultados", Toast.LENGTH_SHORT).show();
            return;
        }
        barChartHum.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            results.forEach(data -> {
                BarEntry entry = new BarEntry(0, data.value);
                BarEntry entry2 = new BarEntry(1, 100);
                barEntries.add(entry);
                barEntries.add(entry2);
            });
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Plantacion de Cacao - Humedad");
        barDataSet.setColors(Color.parseColor("#FFAE58"));
        barDataSet.setValueTextSize(VALUE_TEXT_SIZE);
        barChartHum.setData(new BarData(barDataSet));
        barChartHum.animateY(DURATION);
        barChartHum.getDescription().setText("");
        barChartHum.getDescription().setTextSize(TEXT_SIZE);
        barChartHum.getDescription().setTextColor(Color.BLACK);
    }

    private void getBarCharTemperatura(List<Results> results) {
        List<BarEntry> barEntries = new ArrayList<>();
        if (results.isEmpty() || results.get(0).value == null) {
            barChartTem.setData(null);
            Toast.makeText(getApplicationContext(), "Datos seleccionados sin resultados", Toast.LENGTH_SHORT).show();
            return;
        }
        barChartTem.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            results.forEach(data -> {
                BarEntry entry = new BarEntry(0, data.value);
                BarEntry entry2 = new BarEntry(1, 100);
                barEntries.add(entry);
                barEntries.add(entry2);
            });
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Plantacion de Cacao - Temperatura");
        barDataSet.setColors(Color.parseColor("#FFAE58"));
        barDataSet.setValueTextSize(VALUE_TEXT_SIZE);
        barChartTem.setData(new BarData(barDataSet));
        barChartTem.animateY(DURATION);
        barChartTem.getDescription().setText("");
        barChartTem.getDescription().setTextSize(TEXT_SIZE);
        barChartTem.getDescription().setTextColor(Color.BLACK);
    }

    private void getBarCharLuminosidad(List<Results> results) {
        List<BarEntry> barEntries = new ArrayList<>();
        if (results.isEmpty() || results.get(0).value == null) {
            barChartLum.setData(null);
            Toast.makeText(getApplicationContext(), "Datos seleccionados sin resultados", Toast.LENGTH_SHORT).show();
            return;
        }
        barChartLum.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            results.forEach(data -> {
                BarEntry entry = new BarEntry(0, data.value);
                BarEntry entry2 = new BarEntry(1, 100);
                barEntries.add(entry);
                barEntries.add(entry2);
            });
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Plantacion de Cacao - Luminosidad");
        barDataSet.setColors(Color.parseColor("#FFAE58"));
        barDataSet.setValueTextSize(VALUE_TEXT_SIZE);
        barChartLum.setData(new BarData(barDataSet));
        barChartLum.animateY(DURATION);
        barChartLum.getDescription().setText("");
        barChartLum.getDescription().setTextSize(TEXT_SIZE);
        barChartLum.getDescription().setTextColor(Color.BLACK);
    }


    public void getHumedadTable(String fechaInicio, String fechaFin) {
        try {
            Call<HumedadTable> humedadTableCall = clientApi.getHumedadTable(fechaInicio, fechaFin);
            humedadTableCall.enqueue(new Callback<HumedadTable>() {
                @Override
                public void onResponse(Call<HumedadTable> call, Response<HumedadTable> response) {
                    try {
                        if (response.isSuccessful()) {
                            System.out.printf("");
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
                public void onFailure(Call<HumedadTable> call, Throwable t) {
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