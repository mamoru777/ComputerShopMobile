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

public class ChangePassword extends AppCompatActivity {

    Button buttonConfrim;
    Button buttonBackToLogin;
    EditText editTextPass;
    EditText editTextConPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        LoadData();
    }
    private void LoadData() {
        buttonConfrim = findViewById(R.id.buttonConfirmChPass);
        buttonBackToLogin = findViewById(R.id.buttonBackToLoginChPass);
        editTextPass = findViewById(R.id.editTextPasswordChPass);
        editTextConPass = findViewById(R.id.editTextPasswordConfirmChPass);
        Bundle extras = getIntent().getExtras();
        String email = extras.getString("email");
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        String confirmChPassUrl = IpAdress.getInstance().getIp() + "/user/changepassword";
        buttonConfrim.setOnClickListener(v -> {
            if (!editTextPass.getText().toString().trim().isEmpty()) {
                if (!editTextConPass.getText().toString().trim().isEmpty()) {
                    if (editTextConPass.getText().toString().trim().equals(editTextPass.getText().toString().trim())) {
                        Future<String> confirmChPass = executorService.submit(new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                String response;
                                String url = confirmChPassUrl;
                                JSONObject json = new JSONObject();
                                json.put("email", email);
                                json.put("password", editTextPass.getText().toString());
                                response = HttpUtils.sendPatchRequest(url, json);
                                return response;
                            }
                        });
                        try {
                            Thread.sleep(2000);
                            executorService.shutdown();
                            Toast.makeText(ChangePassword.this, "Пароль изменен", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ChangePassword.this, LoginActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        editTextConPass.setError("Пароли не совпадают");
                        editTextConPass.requestFocus();
                    }
                } else {
                 editTextConPass.setError("Заполните поле подтвердите пароль");
                 editTextConPass.requestFocus();
                }
            } else {
                editTextPass.setError("Заполните поле пароль");
                editTextConPass.requestFocus();
            }


        });
        buttonBackToLogin.setOnClickListener(v -> {
            executorService.shutdown();
            Intent intent = new Intent(ChangePassword.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}