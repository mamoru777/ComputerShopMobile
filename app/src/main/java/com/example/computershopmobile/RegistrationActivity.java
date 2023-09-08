package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONObject;

import okhttp3.OkHttpClient;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class RegistrationActivity extends AppCompatActivity {



    //Client.CreateUserRequest request = Client.CreateUserRequest.newBuilder().
    //Client.CreateUserRequest

    Button buttonRegistr;

    Button buttonBackToLogin;
    EditText EditTextLogin;
    EditText EditTextPassword;
    EditText EditTextEmail;
    EditText EditTextPasswordConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        loadData();
    }

    public void loadData() {

        OkHttpClient client = new OkHttpClient();
        String sendConfirmCodeUrl = "http://10.0.2.2:13999/user/sendconfirmcode";
        String loginCheckUrl = "http://10.0.2.2:13999/user/logincheck";
        String emailCheckUrl = "http://10.0.2.2:13999/user/emailcheck";
        EditTextLogin = findViewById(R.id.editTextLoginReg);
        EditTextPassword = findViewById(R.id.editTextPasswordReg);
        EditTextEmail = findViewById(R.id.editTextEmailReg);
        EditTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirmReg);
        buttonRegistr = findViewById(R.id.buttonRegistrReg);
        buttonBackToLogin = findViewById(R.id.buttonBackToLogin);

        buttonRegistr.setOnClickListener(v -> {
            if (!EditTextLogin.getText().toString().trim().isEmpty()) {
                if (!EditTextPassword.getText().toString().trim().isEmpty()) {
                    if (!EditTextPasswordConfirm.getText().toString().trim().isEmpty()) {
                        if (EditTextPasswordConfirm.getText().toString().trim().equals(EditTextPassword.getText().toString().trim())) {
                            if (!EditTextEmail.getText().toString().trim().isEmpty()) {
                                ExecutorService executorService = Executors.newFixedThreadPool(3);
                                Future<Boolean> isLoginExist = executorService.submit(new Callable<Boolean>() {
                                    @Override
                                    public Boolean call() throws Exception {
                                        Boolean response;
                                        String url = loginCheckUrl + "?login=" + EditTextLogin.getText();
                                        response = HttpUtils.sendGetRequest(url);
                                        return response;
                                    }
                                });
                                Future<Boolean> isEmailExist = executorService.submit(new Callable<Boolean>() {
                                    @Override
                                    public Boolean call() throws Exception {
                                        Boolean response;
                                        String url = emailCheckUrl + "?email=" + EditTextEmail.getText();
                                        response = HttpUtils.sendGetRequest(url);
                                        return response;
                                    }
                                });
                                try {
                                    Boolean responseLogin = isLoginExist.get();
                                    Boolean responseEmail = isEmailExist.get();
                                    if (responseLogin || responseEmail) {
                                        Toast.makeText(RegistrationActivity.this, "Пользователь под данным логином или почтой уже зарегистрирован", Toast.LENGTH_LONG).show();
                                        EditTextLogin.requestFocus();
                                        EditTextEmail.requestFocus();
                                    } else {
                                        Future<String> sendConfirmCode = executorService.submit(new Callable<String>() {
                                            @Override
                                            public String call() throws Exception {
                                                String response;
                                                JSONObject json = new JSONObject();
                                                json.put("email", EditTextEmail.getText());
                                                response = HttpUtils.sendPostRequest(sendConfirmCodeUrl, json);
                                                return response;
                                            }
                                        });
                                        executorService.shutdown();
                                        Intent intent = new Intent(RegistrationActivity.this, ConfirmEmailActivity.class);
                                        intent.putExtra("login", EditTextLogin.getText());
                                        intent.putExtra("password", EditTextPassword.getText());
                                        intent.putExtra("email", EditTextEmail.getText());
                                        startActivity(intent);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                executorService.shutdown();
                                //Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            } else {
                                EditTextEmail.setError("Заполните поле почты");
                                EditTextEmail.requestFocus();
                            }
                        } else {
                            EditTextPasswordConfirm.setError("Пароли не совпадают");
                            EditTextPasswordConfirm.requestFocus();
                        }
                    } else {
                        EditTextPasswordConfirm.setError("Заполните поле повторите пароль");
                        EditTextPasswordConfirm.requestFocus();
                    }
                } else {
                    EditTextPassword.setError("Заполните поле пароль");
                    EditTextPassword.requestFocus();
                }
            } else {
                EditTextLogin.setError("Заполните поле логин");
                EditTextLogin.requestFocus();
            }
            //Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        });


        buttonBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }
}