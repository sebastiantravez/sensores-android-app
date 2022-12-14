package com.example.sensore_android_app.ui.login;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sensore_android_app.R;
import com.example.sensore_android_app.databinding.ActivityLoginBinding;
import com.example.sensore_android_app.ui.home.HomeActivity;
import com.example.sensore_android_app.ui.home.ProfileActivity;
import com.example.sensore_android_app.ui.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;

    EditText txtPassword = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        final TextInputLayout usernameEditText = findViewById(R.id.username);
        final TextInputLayout passwordEditText = findViewById(R.id.password);
        final Button loginButton = binding.login;
        final Button registerButton = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;
        loginButton.setEnabled(true);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = usernameEditText.getEditText().getText().toString().trim();
                String password = passwordEditText.getEditText().getText().toString().trim();
                if (userEmail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Error ingrese usuario y clave", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
                firebaseAuth.signInWithEmailAndPassword(userEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingProgressBar.setVisibility(View.GONE);
                            loginButton.setEnabled(true);
                            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                            finish();
                            Toast.makeText(getApplicationContext(), "Bienvenido " + usernameEditText.getEditText().getText().toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error autenticando con Firebase", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingProgressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        Toast.makeText(getApplicationContext(), "Error autenticando con Firebase " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                registerButton.setEnabled(false);
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                loadingProgressBar.setVisibility(View.GONE);
                registerButton.setEnabled(true);
            }
        });
    }
}