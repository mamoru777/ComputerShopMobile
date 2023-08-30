package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import java.io.IOException;


public class RegistrationActivity extends AppCompatActivity {



    //Client.CreateUserRequest request = Client.CreateUserRequest.newBuilder().
    //Client.CreateUserRequest

    Button buttonRegistr;
    EditText EditTestLogin;
    EditText EditTextPassword;
    EditText EditTextEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        loadData();
    }

    public void loadData() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:13999/users";
        EditTestLogin = findViewById(R.id.editTextLoginReg);
        EditTextPassword = findViewById(R.id.editTextPasswordReg);
        EditTextEmail = findViewById(R.id.editTextEmailReg);
        buttonRegistr = findViewById(R.id.buttonRegistrReg);

        buttonRegistr.setOnClickListener(v -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("login", EditTestLogin.getText());
                        json.put("password", EditTextPassword.getText());
                        json.put("email", EditTextEmail.getText());
                        HttpUtils.sendPostRequest(url, json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Toast.makeText(RegistrationActivity.this, "waiting for connection", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        });
    }
}