package com.example.sensore_android_app.ui.home;

import static com.example.sensore_android_app.utils.Const.DURATION;
import static com.example.sensore_android_app.utils.Const.TEXT_SIZE;
import static com.example.sensore_android_app.utils.Const.VALUE_TEXT_SIZE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sensore_android_app.R;
import com.example.sensore_android_app.data.model.Results;
import com.example.sensore_android_app.data.model.Temperatura;
import com.example.sensore_android_app.data.model.TemperaturaTable;
import com.example.sensore_android_app.services.ClientApiImpl;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemperaturaActivity extends AppCompatActivity {

    private ClientApiImpl clientApi = new ClientApiImpl();

    EditText txtFechaInicio = null;
    EditText txtFechaFin = null;
    DatePicker datePickerInicio = null;
    DatePicker datePickerFin = null;
    ProgressBar progressBar = null;
    Button btnAplicar = null;

    BarChart barChartTem;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperatura);

        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaFin = findViewById(R.id.txtFechaFin);

        datePickerInicio = findViewById(R.id.datePickerInicio);
        datePickerFin = findViewById(R.id.datePickerFin);
        progressBar = findViewById(R.id.loading);
        btnAplicar = findViewById(R.id.btnAplicar);
        barChartTem = findViewById(R.id.barChartTemperatura);

        txtFechaInicio.setText(getFechaInicial());
        txtFechaFin.setText(getFechaInicialFin());
        txtFechaInicio.setEnabled(false);
        txtFechaFin.setEnabled(false);

        datePickerInicio.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            txtFechaInicio.setText(getFechaInicial());
            datePickerInicio.setVisibility(View.GONE);
        });
        datePickerFin.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            txtFechaFin.setText(getFechaInicialFin());
            datePickerFin.setVisibility(View.GONE);
        });

        getTemperatura(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
        getTemperaturaTable(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
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
        barChartTem.setVisibility(View.GONE);
        datePickerInicio.setVisibility(View.VISIBLE);
    }

    public void mostrarCalendarioFin(View view) {
        barChartTem.setVisibility(View.GONE);
        datePickerFin.setVisibility(View.VISIBLE);
    }

    public void aplicarFiltros(View view) {
        btnAplicar.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        datePickerInicio.setVisibility(View.GONE);
        datePickerFin.setVisibility(View.GONE);
        getTemperatura(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
        getTemperaturaTable(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
    }

    public void getTemperatura(String fechaInicio, String fechaFin) {
        try {
            Call<Temperatura> temperaturaCall = clientApi.getTemperatura(fechaInicio, fechaFin);
            temperaturaCall.enqueue(new Callback<Temperatura>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getBarCharTemperatura(List<Results> results) {
        List<BarEntry> barEntries = new ArrayList<>();
        if (results.isEmpty() || results.get(0).value == null) {
            barChartTem.setData(null);
            return;
        }
        barChartTem.setVisibility(View.VISIBLE);
        results.forEach(data -> {
            BarEntry entry = new BarEntry(0, data.value);
            BarEntry entry2 = new BarEntry(1, 100);
            barEntries.add(entry);
            barEntries.add(entry2);
        });
        BarDataSet barDataSet = new BarDataSet(barEntries, "Plantacion de Cacao - Temperatura");
        barDataSet.setColors(Color.parseColor("#FFAE58"));
        barDataSet.setValueTextSize(VALUE_TEXT_SIZE);
        barChartTem.setData(new BarData(barDataSet));
        barChartTem.animateY(DURATION);
        barChartTem.getDescription().setText("");
        barChartTem.getDescription().setTextSize(TEXT_SIZE);
        barChartTem.getDescription().setTextColor(Color.BLACK);
    }

    public void getTemperaturaTable(String fechaInicio, String fechaFin) {
        try {
            Call<TemperaturaTable> temperaturaTableCall = clientApi.getTemperaturaTable(fechaInicio, fechaFin);
            temperaturaTableCall.enqueue(new Callback<TemperaturaTable>() {
                @Override
                public void onResponse(Call<TemperaturaTable> call, Response<TemperaturaTable> response) {
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
                public void onFailure(Call<TemperaturaTable> call, Throwable t) {
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