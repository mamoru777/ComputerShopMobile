package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Phaser;

public class LoginActivity extends AppCompatActivity {
    Button buttonLogin;
    Button buttonRegistr;
    EditText editTextPassword;
    EditText editTextLogin;
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
        editTextLogin = findViewById(R.id.editTextLoginLog);
        String loginUrl = "http://10.0.2.2:13999/user/autho";
        buttonLogin.setOnClickListener(v -> {
            if (!editTextLogin.getText().toString().trim().isEmpty()) {
                if (!editTextPassword.getText().toString().trim().isEmpty()) {
                    ExecutorService executorService = Executors.newFixedThreadPool(1);
                    Future<ResponseUser> isLoginExist = executorService.submit(new Callable<ResponseUser>() {
                        @Override
                        public ResponseUser call() throws Exception {
                            ResponseUser response = new ResponseUser();
                            String url = loginUrl + "?login=" + editTextLogin.getText() + "&password=" + editTextPassword.getText();
                            response = HttpUtils.sendGetRequestLogin(url);
                            return response;
                        }
                    });
                    try {
                        ResponseUser responseUser = isLoginExist.get();
                        Boolean isExist = responseUser.getIsExist();
                        UUID id = responseUser.getId();
                        if (!isExist) {
                            Toast.makeText(LoginActivity.this, "Такого пользователя не существует", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("id", id.toString());
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    editTextPassword.setError("Заполните поле пароль");
                    editTextPassword.requestFocus();
                }
            } else {
                editTextLogin.setError("Заполните поле логин");
                editTextLogin.requestFocus();
            }
        });

        buttonRegistr.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }


}