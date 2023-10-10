package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.computershopmobile.Database.Storage.GoodIdStorage;
import com.example.computershopmobile.Models.GoodId;
import com.example.computershopmobile.Models.Good;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GoodActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button buttonAddToCorsina;
    Button buttonBackToGoods;
    ImageView imageViewAvatar;
    TextView textViewName;
    TextView textViewPrice;
    TextView textViewDescr;
    String role;
    UUID userId;
    UUID goodId;
    GoodIdStorage goodIdStorage;
    Good good;
    Button buttonChange;
    Button buttonDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good);
        LoadData();
    }
    private void LoadData(){
        goodIdStorage = new GoodIdStorage(getBaseContext());
        String getGoodUrl = IpAdress.getInstance().getIp() + "/good/getgood";
        toolbar = findViewById(R.id.toolBarGood);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Товар");
        buttonAddToCorsina = findViewById(R.id.buttonAddGoodCorsina);
        buttonBackToGoods = findViewById(R.id.buttonBackToGoods);
        buttonChange = findViewById(R.id.buttonChangeGood);
        buttonDelete = findViewById(R.id.buttonDeleteGood);

        imageViewAvatar = findViewById(R.id.imageViewGood);
        textViewName = findViewById(R.id.textViewNameGood);
        textViewDescr = findViewById(R.id.textViewDescriptionGood);
        textViewPrice = findViewById(R.id.textViewPriceGood);
        Bundle extras = getIntent().getExtras();
        userId = UUID.fromString(extras.getString("id"));
        role = extras.getString("role");
        goodId = UUID.fromString(extras.getString("goodId"));
        if (role.equals("admin")) {
            buttonChange.setVisibility(View.VISIBLE);
            buttonDelete.setVisibility(View.VISIBLE);
        } else {
            buttonChange.setVisibility(View.GONE);
            buttonDelete.setVisibility(View.GONE);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future <Good> getGood = executorService.submit(new Callable<Good>() {
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
            textViewName.setText(good.getName());
            textViewPrice.setText("Цена: " + String.valueOf(good.getPrice()) + " рублей");
            textViewDescr.setText(good.getDescription());
            imageViewAvatar.setImageBitmap(BitmapFactory.decodeByteArray(good.getAvatar(), 0, good.getAvatar().length));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        buttonChange.setOnClickListener(v -> {

        });
        buttonAddToCorsina.setOnClickListener(v -> {
            GoodId goodId = new GoodId();
            goodId.setGoodid(good.getId().toString());
            goodId.setUserid(userId.toString());
            goodIdStorage.create(goodId);
            Toast.makeText(GoodActivity.this, "Товар добавлен в корзину", Toast.LENGTH_LONG).show();
        });
        buttonBackToGoods.setOnClickListener(v -> {
            executorService.shutdown();
            if (good.getGoodType().equals("Видеокарты")) {
                Intent intent = new Intent(GoodActivity.this, GoodsActivity.class);
                intent.putExtra("id", userId.toString());
                intent.putExtra("role", role);
                intent.putExtra("good_type", "Видеокарты");
                startActivity(intent);
            }
            if (good.getGoodType().equals("Процессоры")) {
                Intent intent = new Intent(GoodActivity.this, GoodsActivity.class);
                intent.putExtra("id", userId.toString());
                intent.putExtra("role", role);
                intent.putExtra("good_type", "Процессоры");
                startActivity(intent);
            }
            if (good.getGoodType().equals("Оперативная память")) {
                Intent intent = new Intent(GoodActivity.this, GoodsActivity.class);
                intent.putExtra("id", userId.toString());
                intent.putExtra("role", role);
                intent.putExtra("good_type", "Оперативная память");
                startActivity(intent);
            }
            if (good.getGoodType().equals("Материнские платы")) {
                Intent intent = new Intent(GoodActivity.this, GoodsActivity.class);
                intent.putExtra("id", userId.toString());
                intent.putExtra("role", role);
                intent.putExtra("good_type", "Материнские платы");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem adminMenuItem = menu.findItem(R.id.menu_item4);
        if (role.equals("admin")) {
            adminMenuItem.setVisible(true);
        } else {
            adminMenuItem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int MENU_ITEM_1 = 1000007;
        final int MENU_ITEM_2 = 1000006;
        final int MENU_ITEM_3 = 1000019;
        // Обработка нажатий на элементы меню
        int id = item.getItemId();
        if (id == R.id.menu_item1) {
            Intent intent = new Intent(GoodActivity.this, MainActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item2) {
            Intent intent = new Intent(GoodActivity.this, PersonalAreaActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item3) {
            Intent intent = new Intent(GoodActivity.this, CorsinaActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (role.equals("admin")) {
            if (id == R.id.menu_item4) {
                Intent intent = new Intent(GoodActivity.this, CreateGoodActivity.class);
                intent.putExtra("id", userId.toString());
                intent.putExtra("role", role);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

}