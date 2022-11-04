package com.example.sensore_android_app.ui.register;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sensore_android_app.R;
import com.example.sensore_android_app.data.model.UserRegister;
import com.example.sensore_android_app.databinding.ActivityRegisterBinding;
import com.example.sensore_android_app.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

   private FirebaseAuth authentication = null;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference connection = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        authentication = FirebaseAuth.getInstance();
        Button registerButton = findViewById(R.id.btnRegisterUser);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerButton.setEnabled(true);
                EditText emailText = (EditText) findViewById(R.id.txtEmail);
                EditText passwordText = (EditText) findViewById(R.id.txtPassword);
                if(emailText.getText().toString().isEmpty() || passwordText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Debe completar los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                registerUser(emailText.getText().toString(), passwordText.getText().toString());
                registerButton.setEnabled(false);
            }
        });
    }

    private void registerUser(String email, String password){
        authentication.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    UserRegister user = new UserRegister(email,password);
                    connection.child("users").child(uid).setValue(user);
                    Toast.makeText(getApplicationContext(), "Bienvenido el usuario se ha creado correctamente", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Error no registrado con Firebase", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error creando usuario con Firebase " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}