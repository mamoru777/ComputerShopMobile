package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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
        ManagedChannel channel = ManagedChannelBuilder.forTarget("5.3.65.108:8080").usePlaintext().build();
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