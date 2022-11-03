package com.example.sensore_android_app.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

import lombok.Data;

@IgnoreExtraProperties
@Data
public class UserRegister {
    public String email;
    public String password;

    public UserRegister(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
