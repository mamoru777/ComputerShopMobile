package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RegistrationActivity extends AppCompatActivity {

    ManagedChannel channel = ManagedChannelBuilder.forTarget("5.3.65.108:8080").usePlaintext().build();

    //Client.CreateUserRequest
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        loadData();
    }

    public void loadData() {

    }
}