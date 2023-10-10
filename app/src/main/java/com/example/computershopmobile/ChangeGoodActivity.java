package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.computershopmobile.Models.Good;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ChangeGoodActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    Button buttonAddAvatar;
    Button buttonChangeGood;
    Button buttonBackToMain;
    EditText editTextName;
    EditText editTextDescription;
    TextView textViewTypeCrGood;
    TextView textViewAvatarCrGood;
    Spinner spinnerType;
    ImageView imageViewAvatar;
    EditText editTextPrice;
    UUID userId;
    String role;
    UUID goodId;
    Good good;
    File avatar;
    String firstGoodName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_good);
        LoadData();
    }
    private void LoadData() {
        String goodCreateUrl = IpAdress.getInstance().getIp() + "/good/create";
        String checkGoodUrl = IpAdress.getInstance().getIp() + "/good/goodcheck";
        String getGoodUrl = IpAdress.getInstance().getIp() + "/good/getgood";
        Bundle extras = getIntent().getExtras();
        userId = UUID.fromString(extras.getString("id"));
        role = extras.getString("role");
        goodId = UUID.fromString(extras.getString("goodId"));
        textViewTypeCrGood = findViewById(R.id.textViewTypeChGood);
        buttonAddAvatar = findViewById(R.id.buttonAddAvatarChGood);
        buttonChangeGood = findViewById(R.id.buttonChangeChGood);
        buttonBackToMain = findViewById(R.id.buttonCancelChGood);
        editTextName = findViewById(R.id.editTextNameChGood);
        editTextDescription = findViewById(R.id.editTextDescriptionChGood);
        imageViewAvatar = findViewById(R.id.imageViewChGood);
        spinnerType = findViewById(R.id.spinnerChGood);
        editTextPrice = findViewById(R.id.editTextPriceChGood);
        textViewAvatarCrGood = findViewById(R.id.textViewAvatarChGood);
        String[] items = {"Видеокарты", "Процессоры", "Материнские платы", "Оперативная память"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        Future<Good> getGood = executorService.submit(new Callable<Good>() {
            @Override
            public Good call() throws Exception {
                Good response;
                String url = getGoodUrl + "?good_id=" + goodId.toString();
                response = HttpUtils.sendGoodGetRequest(url);
                return response;
            }
        });
        try {
            good = getGood.get();
            imageViewAvatar.setImageBitmap(BitmapFactory.decodeByteArray(good.getAvatar(), 0, good.getAvatar().length));
            editTextName.setText(good.getName());
            editTextDescription.setText(good.getDescription());
            editTextPrice.setText(String.valueOf(good.getPrice()));
            firstGoodName = good.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        buttonChangeGood.setOnClickListener(v -> {
            if (imageViewAvatar.getDrawable() != null) {
                if (!editTextName.getText().toString().trim().isEmpty()) {
                    if (!editTextDescription.getText().toString().trim().isEmpty()) {
                        if (!editTextPrice.getText().toString().trim().isEmpty()) {
                            if (spinnerType.getSelectedItem() != null) {
                                Future<Boolean> isEmailExist = executorService.submit(new Callable<Boolean>() {
                                    @Override
                                    public Boolean call() throws Exception {
                                        Boolean response;
                                        String url = checkGoodUrl + "?name=" + editTextName.getText().toString();
                                        response = HttpUtils.sendGetRequest(url);
                                        return response;
                                    }
                                });
                                try {
                                    if (isEmailExist.get() && !editTextName.equals(firstGoodName)) {
                                        editTextName.setError("Товар с таким названием уже существует");
                                        editTextName.requestFocus();
                                    } else {
                                        Future<String> postGood = executorService.submit(new Callable<String>() {
                                            @Override
                                            public String call() throws Exception {
                                                String response;
                                                response = HttpUtils.sendPostRequestGood(spinnerType.getSelectedItem().toString() ,editTextName.getText().toString(), editTextDescription.getText().toString(), editTextPrice.getText().toString(), avatar ,goodCreateUrl);
                                                return response;
                                            }
                                        });
                                        try {
                                            Thread.sleep(3000);
                                            executorService.shutdown();
                                            Toast.makeText(CreateGoodActivity.this, "Товар успешно создан", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(CreateGoodActivity.this, MainActivity.class);
                                            intent.putExtra("id", userId.toString());
                                            intent.putExtra("role", role);
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                textViewTypeCrGood.setError("Выберите тип товара");
                                spinnerType.requestFocus();
                            }
                        } else {
                            editTextPrice.setError("Добавте цену товара");
                            editTextPrice.requestFocus();
                        }
                    } else {
                        editTextDescription.setError("Добавте описание товара");
                        editTextDescription.requestFocus();
                    }
                } else {
                    editTextName.setError("Добавте название товара");
                    editTextName.requestFocus();
                }
            } else {
                //textViewAvatarCrGood.setError("Добавте изображение для товара");
                //textViewAvatarCrGood.requestFocus();
                Toast.makeText(ChangeGoodActivity.this, "Добавте изображение для товара", Toast.LENGTH_LONG).show();
            }
        });
    }
}