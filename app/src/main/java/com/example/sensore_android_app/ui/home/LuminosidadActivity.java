package com.example.sensore_android_app.ui.home;

import static com.example.sensore_android_app.utils.Const.BAR_LUMINOSIDAD_NAME;
import static com.example.sensore_android_app.utils.Const.COLOR_THEME;
import static com.example.sensore_android_app.utils.Const.DURATION;
import static com.example.sensore_android_app.utils.Const.LINE_LUMINOSIDAD_NAME;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sensore_android_app.R;
import com.example.sensore_android_app.data.model.Luminosidad;
import com.example.sensore_android_app.data.model.LuminosidadTable;
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

public class LuminosidadActivity extends AppCompatActivity {

    private ClientApiImpl clientApi = new ClientApiImpl();

    EditText txtFechaInicio = null;
    EditText txtFechaFin = null;
    DatePicker datePickerInicio = null;
    DatePicker datePickerFin = null;
    ProgressBar progressBar = null;
    Button btnAplicar = null;

    BarChart barChartLum;
    LineChart lineChartLum;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luminosidad);

        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaFin = findViewById(R.id.txtFechaFin);

        datePickerInicio = findViewById(R.id.datePickerInicio);
        datePickerFin = findViewById(R.id.datePickerFin);
        progressBar = findViewById(R.id.loading);
        btnAplicar = findViewById(R.id.btnAplicar);
        barChartLum = findViewById(R.id.barChartLuminosidad);
        lineChartLum = findViewById(R.id.lineChartLuminosidad);

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

        barChartLum.setVisibility(View.GONE);
        lineChartLum.setVisibility(View.GONE);

        getLuminosidad(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
        getLuminosidadTable(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
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
        barChartLum.setVisibility(View.GONE);
        lineChartLum.setVisibility(View.GONE);
        datePickerInicio.setVisibility(View.VISIBLE);
        datePickerFin.setVisibility(View.GONE);
    }

    public void mostrarCalendarioFin(View view) {
        barChartLum.setVisibility(View.GONE);
        lineChartLum.setVisibility(View.GONE);
        datePickerFin.setVisibility(View.VISIBLE);
        datePickerInicio.setVisibility(View.GONE);
    }

    public void aplicarFiltros(View view) {
        btnAplicar.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        datePickerInicio.setVisibility(View.GONE);
        datePickerFin.setVisibility(View.GONE);
        getLuminosidad(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
        getLuminosidadTable(txtFechaInicio.getText().toString(), txtFechaFin.getText().toString());
    }

    public void getLuminosidad(String fechaInicio, String fechaFin) {
        try {
            Call<Luminosidad> luminosidadCall = clientApi.getLuminosidad(fechaInicio, fechaFin);
            luminosidadCall.enqueue(new Callback<Luminosidad>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<Luminosidad> call, Response<Luminosidad> response) {
                    try {
                        if (response.isSuccessful()) {
                            getBarCharLuminosidad(response.body().results);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getBarCharLuminosidad(List<Results> results) {
        List<BarEntry> barEntries = new ArrayList<>();
        if (results.isEmpty() || results.get(0).value == null) {
            barChartLum.setData(null);
            Toast.makeText(getApplicationContext(), "Datos seleccionados sin resultados", Toast.LENGTH_SHORT).show();
            return;
        }
        barChartLum.setVisibility(View.VISIBLE);
        results.forEach(data -> {
            BarEntry entry = new BarEntry(0, data.value);
            BarEntry entry2 = new BarEntry(1, 100);
            barEntries.add(entry);
            barEntries.add(entry2);
        });

        BarDataSet barDataSet = new BarDataSet(barEntries, "Plantacion de Cacao - Luminosidad");
        barDataSet.setColors(Color.parseColor(COLOR_THEME));
        barDataSet.setValueTextSize(VALUE_TEXT_SIZE);
        barChartLum.setData(new BarData(barDataSet));
        barChartLum.animateY(DURATION);
        barChartLum.getDescription().setText("");
        barChartLum.getDescription().setTextSize(TEXT_SIZE);
        barChartLum.getDescription().setTextColor(Color.BLACK);
        sincronizarBarChartDataFirebase(results);
    }

    public void getLuminosidadTable(String fechaInicio, String fechaFin) {
        try {
            Call<LuminosidadTable> luminosidadTableCall = clientApi.getLuminosidadTable(fechaInicio, fechaFin);
            luminosidadTableCall.enqueue(new Callback<LuminosidadTable>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<LuminosidadTable> call, Response<LuminosidadTable> response) {
                    try {
                        if (response.isSuccessful()) {
                            Map<Date, Long> data = new TreeMap<>();
                            response.body().results.forEach(val -> {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String fechaUnica = dateFormat.format(new Date(Long.parseLong(val.get(0).toString())));
                                try {
                                    Date fechaFinal = dateFormat.parse(fechaUnica);
                                    if (data.isEmpty()) {
                                        data.put(fechaFinal, val.get(1));
                                    } else {
                                        Long valor = data.get(fechaFinal);
                                        if (valor == null) {
                                            valor = val.get(1);
                                            data.put(fechaFinal, valor);
                                        } else if (val.get(1) > valor) {
                                            data.put(fechaFinal, val.get(1));
                                        } else {
                                            data.put(fechaFinal, valor);
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            });
                            getLineChartLuminosidad(data, response.body());
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
                public void onFailure(Call<LuminosidadTable> call, Throwable t) {
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
    public void getLineChartLuminosidad(Map<Date, Long> results, LuminosidadTable luminosidadTable) {
        if (results.isEmpty()) {
            lineChartLum.setData(null);
            return;
        }
        lineChartLum.setVisibility(View.VISIBLE);
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
        lineChartLum.getDescription().setText("Luminosidad");
        lineChartLum.getDescription().setTextSize(TEXT_SIZE);
        lineChartLum.animateY(DURATION);
        lineChartLum.setData(lineData);
        sincronizarChartLineDataFirebase(luminosidadTable);
    }

    private void sincronizarBarChartDataFirebase(List<Results> results) {
        Results humedadRegistro = new Results();
        humedadRegistro.createdAt = results.get(0).createdAt;
        humedadRegistro.value = results.get(0).value;
        databaseReference.child(BAR_LUMINOSIDAD_NAME).setValue(humedadRegistro).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void sincronizarChartLineDataFirebase(LuminosidadTable luminosidadTable) {
        databaseReference.child(LINE_LUMINOSIDAD_NAME).setValue(luminosidadTable).addOnSuccessListener(new OnSuccessListener<Void>() {
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