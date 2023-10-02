package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ForgetPassword extends AppCompatActivity {

    Button buttonSendCode;
    Button buttonBackToLogin;
    EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        LoadData();
    }
     private void LoadData() {
         String emailCheckUrl = IpAdress.getInstance().getIp() + "/user/emailcheck";
         String sendConfirmCodeUrl = IpAdress.getInstance().getIp() + "/user/sendconfirmcode";
         buttonSendCode = findViewById(R.id.buttonSendCodeForPas);
         buttonBackToLogin = findViewById(R.id.buttonaBackToLoginForPas);
         editTextEmail = findViewById(R.id.editTextEmailForPas);
         buttonSendCode.setOnClickListener(v -> {
             if (!editTextEmail.getText().toString().trim().isEmpty()) {
                 if (Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString().trim()).matches()) {
                     ExecutorService executorService = Executors.newFixedThreadPool(2);
                     Future<Boolean> isEmailExist = executorService.submit(new Callable<Boolean>() {
                         @Override
                         public Boolean call() throws Exception {
                             Boolean response;
                             String url = emailCheckUrl + "?email=" + editTextEmail.getText().toString();
                             response = HttpUtils.sendGetRequest(url);
                             return response;
                         }
                     });
                     try {
                         Boolean responseEmail = isEmailExist.get();
                         if (responseEmail) {
                             Future<Boolean> sendConfirmCode = executorService.submit(new Callable<Boolean>() {
                                 @Override
                                 public Boolean call() throws Exception {
                                     Boolean response;
                                     String url = sendConfirmCodeUrl + "?email=" + editTextEmail.getText().toString();
                                     response = HttpUtils.sendGetRequest(url);
                                     return response;
                                 }
                             });
                             executorService.shutdown();
                             Intent intent = new Intent(ForgetPassword.this, confirmChangePassword.class);
                             intent.putExtra("email", editTextEmail.getText().toString());
                             startActivity(intent);
                         } else {
                             editTextEmail.requestFocus();
                             editTextEmail.setError("Пользователя с такой почтой не существует");
                         }
                     } catch (Exception e)
                     {
                         e.printStackTrace();
                     }
                 } else {
                     editTextEmail.setError("Введите правильный адрес электронной почты");
                     editTextEmail.requestFocus();
                 }
             } else {
                 editTextEmail.setError("Заполните поле");
                 editTextEmail.requestFocus();
             }
         });
         buttonBackToLogin.setOnClickListener(v -> {
             Intent intent = new Intent(ForgetPassword.this, LoginActivity.class);
             startActivity(intent);
         });
     }

}