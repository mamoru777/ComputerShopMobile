package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoadData();
    }
    public void LoadData() {
        Bundle extras = getIntent().getExtras();
        UUID id = UUID.fromString(extras.getString("id"));
        Toast.makeText(MainActivity.this, id.toString(), Toast.LENGTH_LONG).show();
    }
}