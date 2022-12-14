package com.example.sensore_android_app.ui.home;

import static com.example.sensore_android_app.utils.Const.BAR_HUMEDAD_NAME;
import static com.example.sensore_android_app.utils.Const.COLOR_THEME;
import static com.example.sensore_android_app.utils.Const.DURATION;
import static com.example.sensore_android_app.utils.Const.LINE_HUMEDAD_NAME;
import static com.example.sensore_android_app.utils.Const.TEXT_SIZE;
import static com.example.sensore_android_app.utils.Const.VALUE_TEXT_SIZE;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sensore_android_app.R;
import com.example.sensore_android_app.data.model.Humedad;
import com.example.sensore_android_app.data.model.HumedadTable;
import com.example.sensore_android_app.data.model.Results;
import com.example.sensore_android_app.services.ClientApiImpl;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HumedadActivity extends AppCompatActivity {

    private ClientApiImpl clientApi = new ClientApiImpl();

    EditText txtFechaInicio = null;
    EditText txtFechaFin = null;
    DatePicker datePickerInicio = null;
    DatePicker datePickerFin = null;
    ProgressBar progressBar = null;
    Button btnAplicar = null;

    BarChart barChartHum;
    LineChart lineChartHumedad;

    String API = "";

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humedad);

        Intent intent = this.getIntent();
        API = intent.getStringExtra("id");

        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaFin = findViewById(R.id.txtFechaFin);

        datePickerInicio = findViewById(R.id.datePickerInicio);
        datePickerFin = findViewById(R.id.datePickerFin);
        progressBar = findViewById(R.id.loading);
        btnAplicar = findViewById(R.id.btnAplicar);
        barChartHum = findViewById(R.id.barChartHumedad);
        lineChartHumedad = findViewById(R.id.lineChartHumedad);

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

        barChartHum.setVisibility(View.GONE);
        lineChartHumedad.setVisibility(View.GONE);

        getHumedad(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
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
        lineChartHumedad.setVisibility(View.GONE);
        datePickerInicio.setVisibility(View.VISIBLE);
        datePickerFin.setVisibility(View.GONE);
    }

    public void mostrarCalendarioFin(View view) {
        barChartHum.setVisibility(View.GONE);
        lineChartHumedad.setVisibility(View.GONE);
        datePickerFin.setVisibility(View.VISIBLE);
        datePickerInicio.setVisibility(View.GONE);
    }

    public void aplicarFiltros(View view) throws ParseException {
        btnAplicar.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        datePickerInicio.setVisibility(View.GONE);
        datePickerFin.setVisibility(View.GONE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = simpleDateFormat.parse(txtFechaInicio.getText().toString());
        Date end = simpleDateFormat.parse(txtFechaFin.getText().toString());
        if (end.before(start)) {
            Toast.makeText(getApplicationContext(), "Fecha de inicio no puede ser mayor a fecha fin", Toast.LENGTH_LONG).show();
            btnAplicar.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            return;
        }
        getHumedad(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
        getHumedadTable(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
    }

    public void getHumedad(String fechaInicio, String fechaFin) {
        try {
            Call<Humedad> humedadCall = clientApi.getHumedad(fechaInicio, fechaFin, API);
            humedadCall.enqueue(new Callback<Humedad>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<Humedad> call, Response<Humedad> response) {
                    try {
                        if (response.isSuccessful()) {
                            getBarCharHumedad(response.body().results);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error " + response.message(), Toast.LENGTH_SHORT).show();
                            return;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getBarCharHumedad(List<Results> results) {
        List<BarEntry> barEntries = new ArrayList<>();
        if (results.isEmpty() || results.get(0).value == null) {
            barChartHum.setData(null);
            Toast.makeText(getApplicationContext(), "Datos seleccionados sin resultados", Toast.LENGTH_SHORT).show();
            return;
        }
        barChartHum.setVisibility(View.VISIBLE);
        results.forEach(data -> {
            BarEntry entry = new BarEntry(0, Float.parseFloat(data.value.toString()));
            BarEntry entry2 = new BarEntry(1, 100);
            barEntries.add(entry);
            barEntries.add(entry2);
        });
        BarDataSet barDataSet = new BarDataSet(barEntries, "Plantacion de Cacao - Humedad");
        barDataSet.setColors(Color.parseColor("#FFAE58"));
        barDataSet.setValueTextSize(VALUE_TEXT_SIZE);
        barChartHum.setData(new BarData(barDataSet));
        barChartHum.animateY(DURATION);
        barChartHum.getDescription().setText("");
        barChartHum.getDescription().setTextSize(TEXT_SIZE);
        barChartHum.getDescription().setTextColor(Color.BLACK);
        sincronizarBarChartDataFirebase(results);
    }

    public void getHumedadTable(String fechaInicio, String fechaFin) {
        try {
            Call<HumedadTable> humedadTableCall = clientApi.getHumedadTable(fechaInicio, fechaFin, API);
            humedadTableCall.enqueue(new Callback<HumedadTable>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<HumedadTable> call, Response<HumedadTable> response) {
                    try {
                        if (response.isSuccessful()) {
                            Map<Date, Long> data = new TreeMap<>();
                            Integer value = API.equals("BOX1") ? 0 : 1;
                            response.body().results.get(value).data.forEach(val -> {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String fechaUnica = dateFormat.format(new Date(Long.parseLong(val.get(0).toString())));
                                try {
                                    Date fechaFinal = dateFormat.parse(fechaUnica);
                                    if (data.isEmpty()) {
                                        data.put(fechaFinal, val.get(1).longValue());
                                    } else {
                                        Long valor = data.get(fechaFinal);
                                        if (valor == null) {
                                            valor = val.get(1).longValue();
                                            data.put(fechaFinal, valor);
                                        } else if (val.get(1) > valor) {
                                            data.put(fechaFinal, val.get(1).longValue());
                                        } else {
                                            data.put(fechaFinal, valor);
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            });
                            getLineChartHumedad(data, response.body());
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getLineChartHumedad(Map<Date, Long> results, HumedadTable humedadTable) {
        if (results.isEmpty()) {
            barChartHum.setData(null);
            return;
        }
        lineChartHumedad.setVisibility(View.VISIBLE);
        Map<Date, Entry> mapa = new TreeMap<>();
        AtomicReference<Integer> count = new AtomicReference<>(0);
        results.forEach((key, value) -> {
            Integer x = count.getAndSet(count.get() + 1);
            mapa.put(key, new Entry(x, value));
        });

        List<LineDataSet> lineDataSets = new ArrayList<>();
        Map<String, List<Entry>> listMap = new TreeMap<>();
        mapa.forEach((key, value) -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String fecha = dateFormat.format(key);
            if (listMap.isEmpty()) {
                List<Entry> entries = new ArrayList<>();
                entries.add(value);
                listMap.put(fecha, entries);
            } else {
                List<Entry> lista = listMap.get(fecha);
                if (lista == null) {
                    lista = new ArrayList<>();
                }
                lista.add(value);
                listMap.put(fecha, lista);
            }
        });

        listMap.forEach((key, value) -> {
            LineDataSet lineDataSet = new LineDataSet(value, key);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setColors(Color.parseColor(COLOR_THEME));
            lineDataSets.add(lineDataSet);
        });

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.addAll(lineDataSets);
        LineData lineData = new LineData(dataSets);
        String typeHumedad = API.equals("BOX1") ? "Humedad" : "Humedad 2";
        lineChartHumedad.getDescription().setText(typeHumedad);
        lineChartHumedad.getDescription().setTextSize(TEXT_SIZE);
        lineChartHumedad.animateY(DURATION);
        lineChartHumedad.setData(lineData);
        sincronizarChartLineDataFirebase(humedadTable);
    }

    private void sincronizarBarChartDataFirebase(List<Results> results) {
        Results humedadRegistro = new Results();
        humedadRegistro.createdAt = results.get(0).timestamp;
        humedadRegistro.resultValue = Float.valueOf(results.get(0).value.toString());
        databaseReference.child(BAR_HUMEDAD_NAME).setValue(humedadRegistro).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Registro creado");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sincronizarChartLineDataFirebase(HumedadTable humedadTable) {
        databaseReference.child(LINE_HUMEDAD_NAME).setValue(humedadTable).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Registro creado");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}