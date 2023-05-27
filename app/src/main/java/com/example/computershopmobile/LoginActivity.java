package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    Button buttonLogin;
    Button buttonRegistr;

    EditText editTextPassword;
    EditText editTestLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoadData();
    }
    public void LoadData()
    {
        buttonLogin = findViewById(R.id.buttonLoginLog);
        buttonRegistr = findViewById(R.id.buttonRegistrLog);
        editTextPassword = findViewById(R.id.editTextPasswordLog);
        editTestLogin = findViewById(R.id.editTextLoginLog);



        buttonRegistr.setOnClickListener(v ->
        {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }


}