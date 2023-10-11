package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.computershopmobile.Database.Storage.GoodIdStorage;
import com.example.computershopmobile.Models.Good;
import com.example.computershopmobile.Models.GoodId;
import com.example.computershopmobile.Models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OrderActivity extends AppCompatActivity {

    TextView textViewName;
    TextView textViewSurName;
    EditText editTextCity;
    EditText editTextAdress;
    EditText editTextPhone;
    RadioButton buttonNal;
    RadioButton buttonCard;
    RadioGroup radioGroup;
    Button buttonPay;
    Button buttonBackToCorsina;
    TextView textViewNumGoods;
    TextView textViewSumm;
    GoodIdStorage goodIdStorage;
    UUID userId;
    String role;
    float summ;
    int goodSize;
    User user;
    Boolean isCard = false;
    ArrayList<Good> goods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        LoadData();
    }
    private void LoadData() {
        String getUserInfoUrl = IpAdress.getInstance().getIp() + "/user/userinfo";
        String createOrderUrl = IpAdress.getInstance().getIp() + "/order/create";
        Bundle extras = getIntent().getExtras();
        userId = UUID.fromString(extras.getString("id"));
        role = extras.getString("role");
        summ = extras.getFloat("summ");
        goodSize = extras.getInt("number");
        radioGroup = findViewById(R.id.radioGroupOrder);
        textViewName = findViewById(R.id.textViewNameOrder);
        textViewSurName = findViewById(R.id.textViewSurNameOrder);
        editTextCity = findViewById(R.id.editTextCityOrder);
        editTextAdress = findViewById(R.id.editTextAdressOrder);
        editTextPhone = findViewById(R.id.editTextPhoneNumberOrder);
        buttonNal = findViewById(R.id.buttonNalOrder);
        buttonCard = findViewById(R.id.buttonCardOrder);
        buttonPay = findViewById(R.id.buttonPayOrder);
        buttonBackToCorsina = findViewById(R.id.buttonBackToCorsinaOrder);
        textViewNumGoods = findViewById(R.id.textViewCollGoodsOrder);
        textViewSumm = findViewById(R.id.textViewSummOrder);
        textViewNumGoods.setText("Товаров: " + goodSize);
        textViewSumm.setText("Итого: " + summ);
        goodIdStorage = new GoodIdStorage(getBaseContext());
        ArrayList<GoodId> goodId = goodIdStorage.getFullList(userId.toString());
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<User> getUserInfo = executorService.submit(new Callable<User>() {
            @Override
            public User call() throws Exception {
                User response = new User();
                String url = getUserInfoUrl + "?id=" + userId.toString();
                response = HttpUtils.sendUserInfoGetRequest(url);
                return response;
            }
        });
        try {
            user = getUserInfo.get();
            textViewName.setText(user.getName());
            textViewSurName.setText(user.getLastName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        buttonNal.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Обработка выбора радиокнопок
                if (checkedId == R.id.buttonNalOrder) {
                    // Выбрана радиокнопка 1
                    isCard = false;
                    // Ваш код здесь
                } else if (checkedId == R.id.buttonCardOrder) {
                    // Выбрана радиокнопка 2
                    // Ваш код здесь
                    isCard = true;
                }
            }
        });
        buttonBackToCorsina.setOnClickListener(v -> {
            executorService.shutdown();
            Intent intent = new Intent(OrderActivity.this, CorsinaActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        });
        buttonPay.setOnClickListener(v -> {
            if (!editTextCity.getText().toString().trim().isEmpty()) {
                if (!editTextAdress.getText().toString().trim().isEmpty()) {
                    if (!editTextPhone.getText().toString().trim().isEmpty()) {
                        if (!textViewName.getText().toString().trim().isEmpty()) {
                            if (!textViewSurName.getText().toString().trim().isEmpty()) {
                                if (isCard) {

                                } else {
                                    JSONArray goodIdArray = new JSONArray();
                                    for (int i = 0; i < goodId.size(); i++) {
                                        goodIdArray.put(goodId.get(i).getGoodid());
                                    }
                                    //Toast.makeText(OrderActivity.this, goodIds.toString(), Toast.LENGTH_LONG).show();
                                    Future<String> createOrder = executorService.submit(new Callable<String>() {
                                        @Override
                                        public String call() throws Exception {
                                            String response;
                                            JSONObject jsonObject = new JSONObject();

                                            jsonObject.put("summ", summ);
                                            jsonObject.put("city", editTextCity.getText().toString());
                                            jsonObject.put("adress", editTextAdress.getText().toString());
                                            jsonObject.put("phone", editTextPhone.getText().toString());
                                            jsonObject.put("ispaid", false);
                                            jsonObject.put("goodid", goodIdArray);
                                            jsonObject.put("userid", userId.toString());
                                            response = HttpUtils.sendPostRequest(createOrderUrl, jsonObject);
                                            return response;
                                        }
                                    });
                                    try {
                                        Thread.sleep(3000);
                                        executorService.shutdown();
                                        Toast.makeText(OrderActivity.this, "Заказ успешно создан", Toast.LENGTH_LONG).show();
                                        for (int i = 0; i < goodSize; i++) {
                                            goodIdStorage.delete(goodId.get(i));
                                        }
                                        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                                        intent.putExtra("id", userId.toString());
                                        intent.putExtra("role", role);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                Toast.makeText(OrderActivity.this, "Заполните фамилию в профиле", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(OrderActivity.this, "Заполните имя в профиле", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        editTextPhone.setError("Заполните поле номер телефона");
                        editTextPhone.requestFocus();
                    }
                } else {
                    editTextAdress.setError("Заполните поле адрес");
                    editTextAdress.requestFocus();
                }
            } else {
                editTextCity.setError("Заполните поле город");
                editTextCity.requestFocus();
            }
        });

    }
}