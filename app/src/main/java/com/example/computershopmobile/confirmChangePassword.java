package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class confirmChangePassword extends AppCompatActivity {

    Button buttonBackToForPass;
    Button buttonEnter;
    EditText editTextCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_change_password);
        LoadData();
    }
    private void LoadData() {
        String confirmEmailUrl = "http://5.3.79.15:13999/user/confirmemail";
        buttonBackToForPass = findViewById(R.id.buttonBackToForPassConChPass);
        buttonEnter = findViewById(R.id.buttonEnterConChPass);
        editTextCode = findViewById(R.id.editTextConfirmConChPass);
        Bundle extras = getIntent().getExtras();
        String email = extras.getString("email");
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        buttonEnter.setOnClickListener(v -> {
            if (!editTextCode.getText().toString().trim().isEmpty()) {
                Future<Boolean> confirmEmail = executorService.submit(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        Boolean response;
                        String url = confirmEmailUrl + "?code=" + editTextCode.getText().toString() + "&email=" + email;
                        response = HttpUtils.sendGetRequest(url);
                        return response;
                    }
                });
                try {
                    Boolean isMatch = confirmEmail.get();
                    if (isMatch) {
                        executorService.shutdown();
                        Intent intent = new Intent(confirmChangePassword.this, ChangePassword.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        editTextCode.setError("Неверный код");
                        editTextCode.requestFocus();
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            } else {
                editTextCode.setError("Заполните поле");
                editTextCode.requestFocus();
            }
        });
        buttonBackToForPass.setOnClickListener(v -> {
            executorService.shutdown();
            Intent intent = new Intent(confirmChangePassword.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}