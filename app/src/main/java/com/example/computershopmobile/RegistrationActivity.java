package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import com.example.computershopmobile.proto.Client;
import com.example.computershopmobile.proto.UserServiceGrpc;


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
        //Toast.makeText(RegistrationActivity.this, io.grpc.Version.get, Toast.LENGTH_LONG).show();
        //ManagedChannel channel = ManagedChannelBuilder.forTarget("5.3.65.108:7070").usePlaintext().build();
        //ManagedChannel channel = ManagedChannelBuilder.forAddress("5.3.65.108",7070).usePlaintext().build();
        //ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:7070").usePlaintext().build();
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",7070).usePlaintext().build();
        if (channel.isTerminated()) {
            Toast.makeText(RegistrationActivity.this, "Клиент подключен к серверу", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(RegistrationActivity.this, "Клиент не подключен к серверу", Toast.LENGTH_LONG).show();
        }
        UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc.newBlockingStub(channel);
        EditTestLogin = findViewById(R.id.editTextLoginReg);
        EditTextPassword = findViewById(R.id.editTextPasswordReg);
        EditTextEmail = findViewById(R.id.editTextEmailReg);
        buttonRegistr = findViewById(R.id.buttonRegistrReg);

        buttonRegistr.setOnClickListener(v -> {
            Client.CreateUserRequest request = Client.CreateUserRequest.newBuilder().setLogin(EditTestLogin.getText().toString()).setPassword(EditTextPassword.getText().toString()).setEmail(EditTextEmail.getText().toString()).build();
            stub.createUser(request);
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                }

        );
    }
}