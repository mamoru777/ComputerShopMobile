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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


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
        String baseUrl = "http://10.0.2.2:13999/registration";
        EditTestLogin = findViewById(R.id.editTextLoginReg);
        EditTextPassword = findViewById(R.id.editTextPasswordReg);
        EditTextEmail = findViewById(R.id.editTextEmailReg);
        buttonRegistr = findViewById(R.id.buttonRegistrReg);

        buttonRegistr.setOnClickListener(v -> {
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    Boolean response;
                    String url = baseUrl + "?login=" + EditTestLogin.getText();
                    response = HttpUtils.sendGetRequest(url);
                    return response;
                }
            });
            /*ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<String> future = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String response;
                    JSONObject json = new JSONObject();
                    json.put("login", EditTestLogin.getText());
                    json.put("password", EditTextPassword.getText());
                    json.put("email", EditTextEmail.getText());
                    response = HttpUtils.sendPostRequest(baseUrl, json);
                    return response;
                }
            });*/
            try {
                Boolean response = future.get();
                if (response == true) {
                    Toast.makeText(RegistrationActivity.this, "Пользователь под данным логином или почтой уже зарегистрирован, попробуйте другой логин, почту или авторизируйтесь", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(RegistrationActivity.this, response.toString(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            executorService.shutdown();
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        });
    }
}