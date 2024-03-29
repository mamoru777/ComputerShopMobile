package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;

public class ConfirmEmailActivity extends AppCompatActivity {

    EditText editTextConfirm;
    Button buttonEnter;
    Button buttonReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_email);
        LoadData();
    }
    private void LoadData() {
        OkHttpClient client = new OkHttpClient();
        String registrUrl = IpAdress.getInstance().getIp() + "/user/registration";
        String confirmEmailUrl = IpAdress.getInstance().getIp() + "/user/confirmemail";
        //String registrUrl = "http://5.3.79.15:13999/user/registration";
        //String confirmEmailUrl = "http://5.3.79.15:13999/user/confirmemail";
        editTextConfirm = findViewById(R.id.editTextConfirmConReg);
        buttonEnter = findViewById(R.id.buttonEnterConReg);
        buttonReg = findViewById(R.id.buttonRegistrConReg);
        Bundle extras = getIntent().getExtras();
        String login = extras.getString("login");
        String password = extras.getString("password");
        String email = extras.getString("email");

        buttonEnter.setOnClickListener(v -> {
            if (!editTextConfirm.getText().toString().trim().isEmpty()) {
                ExecutorService executorService = Executors.newFixedThreadPool(2);
                Future<Boolean> confirmEmail = executorService.submit(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        Boolean response;
                        String url = confirmEmailUrl + "?code=" + editTextConfirm.getText().toString() + "&email=" + email;
                        response = HttpUtils.sendGetRequest(url);
                        return response;
                    }
                });
                try {
                    boolean isMatch = confirmEmail.get();
                    if (isMatch) {
                        Future<String> registr = executorService.submit(new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                String response;
                                JSONObject json = new JSONObject();
                                json.put("login", login);
                                json.put("password", password);
                                json.put("email", email);
                                json.put("role", "user");
                                response = HttpUtils.sendPostRequest(registrUrl, json);
                                return response;
                            }
                        });
                        try {
                            Thread.sleep(2000); // замораживаем выполнение кода на 2 секунды
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        executorService.shutdown();
                        Toast.makeText(ConfirmEmailActivity.this, "Пользователь успешно создан", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ConfirmEmailActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        editTextConfirm.setError("Неверный код");
                        editTextConfirm.requestFocus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                editTextConfirm.setError("Заполните поле");
                editTextConfirm.requestFocus();
            }

        });
        buttonReg.setOnClickListener(v -> {
            Intent intent = new Intent(ConfirmEmailActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }
}