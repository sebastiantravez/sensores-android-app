package com.example.sensore_android_app.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.sensore_android_app.R;
import com.example.sensore_android_app.databinding.ActivityHomeBinding;
import com.example.sensore_android_app.ui.login.LoginActivity;

import lombok.NonNull;


public class HomeActivity extends AppCompatActivity {

    AppCompatImageView imageButtonHum;
    AppCompatImageView imageButtonTem;
    AppCompatImageView imageButtonLum;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        imageButtonHum = findViewById(R.id.btnHumedad);
        imageButtonTem = findViewById(R.id.btnTemperatura);
        imageButtonLum = findViewById(R.id.btnLuminosidad);

        imageButtonHum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, HumedadActivity.class));
            }
        });

        imageButtonTem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, TemperaturaActivity.class));
            }
        });

        imageButtonLum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, LuminosidadActivity.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnClose:
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}