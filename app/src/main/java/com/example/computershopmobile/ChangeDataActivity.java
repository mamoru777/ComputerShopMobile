package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ChangeDataActivity extends AppCompatActivity {
    Button buttonBackToPA;
    Button buttonChangeData;
    EditText editTextLogin;
    EditText editTextName;
    EditText editTextLastName;
    EditText editTextSurName;
    UUID userId;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data);
        LoadData();
    }
    private void LoadData() {
        buttonBackToPA = findViewById(R.id.buttonBackToPAChData);
        buttonChangeData = findViewById(R.id.buttonChangeChData);
        editTextLogin = findViewById(R.id.editTextLoginChData);
        editTextName = findViewById(R.id.editTextNameChData);
        editTextLastName = findViewById(R.id.editTextLastNameChData);
        editTextSurName = findViewById(R.id.editTextSurNameChData);
        Bundle extras = getIntent().getExtras();
        userId = UUID.fromString(extras.getString("id"));
        role = extras.getString("role");
        String changeDataUrl = IpAdress.getInstance().getIp() + "/user/changedata";
        Toast.makeText(ChangeDataActivity.this, userId.toString(), Toast.LENGTH_LONG).show();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        buttonChangeData.setOnClickListener(v -> {
            Future<String> changeData = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String response;
                    String url = changeDataUrl;
                    JSONObject json = new JSONObject();
                    json.put("id", userId.toString());
                    json.put("login", editTextLogin.getText().toString());
                    json.put("name", editTextName.getText().toString());
                    json.put("lastname", editTextLastName.getText().toString());
                    json.put("surname", editTextSurName.getText().toString());
                    response = HttpUtils.sendPatchRequest(url, json);
                    return response;
                }
            });
            try {
                Thread.sleep(2000);
                executorService.shutdown();
                Toast.makeText(ChangeDataActivity.this, "Данные изменены", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ChangeDataActivity.this, PersonalAreaActivity.class);
                intent.putExtra("id", userId.toString());
                intent.putExtra("role", role);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        buttonBackToPA.setOnClickListener(v -> {
            Intent intent = new Intent(ChangeDataActivity.this, PersonalAreaActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        });
    }

}