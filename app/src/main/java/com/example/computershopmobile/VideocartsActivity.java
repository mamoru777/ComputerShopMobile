package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.example.computershopmobile.Models.Good;
import com.example.computershopmobile.Models.User;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VideocartsActivity extends AppCompatActivity {

    String role;
    UUID userId;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videocarts);
        LoadData();
    }
    private void LoadData() {
        toolbar = findViewById(R.id.toolBarVideo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Видеокарты");
        Bundle extras = getIntent().getExtras();
        userId = UUID.fromString(extras.getString("id"));
        role = extras.getString("role");
        ConstraintLayout mainLayout = findViewById(R.id.videocarrsConstraintLayout);
        String getGoodsUrl = IpAdress.getInstance().getIp() + "/good/goodsbytype";
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<ArrayList<Good>> getGoods = executorService.submit(new Callable<ArrayList<Good>>() {
            @Override
            public ArrayList<Good> call() throws Exception {
                ArrayList response = new ArrayList();
                String url = getGoodsUrl + "?good_type=" + "Видеокарты";
                response = HttpUtils.sendGoodsGetRequest(url);
                return response;
            }
        });
        try {
            ArrayList<Good> goods = getGoods.get();
            for (int i = 0; i < goods.size(); i++) {
                LinearLayout groupLayout = new LinearLayout(this);
                groupLayout.setLayoutParams(new LayoutParams(
                        500,
                        120
                ));
                groupLayout.setOrientation(LinearLayout.HORIZONTAL);

                ConstraintLayout.LayoutParams constraintLayoutAvatar = new ConstraintLayout.LayoutParams(
                        110,
                        110
                );
                constraintLayoutAvatar.topToTop = groupLayout.getId();
                constraintLayoutAvatar.startToStart = groupLayout.getId();
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(constraintLayoutAvatar);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    groupLayout.setId(View.generateViewId());
                }
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(goods.get(i).getAvatar(), 0, goods.get(i).getAvatar().length));
                //imageView.setImageResource();

                TextView textView = new TextView(this);
                textView.setText("Ваш текст здесь");
                ConstraintLayout.LayoutParams constraintLayoutName = new ConstraintLayout.LayoutParams(
                        200,
                        30
                );
                constraintLayoutName.topToTop = groupLayout.getId();
                constraintLayoutName.endToEnd = groupLayout.getId();
                textView.setLayoutParams(constraintLayoutName);

                groupLayout.addView(imageView);
                groupLayout.addView(textView);

                mainLayout.addView(groupLayout);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(mainLayout);
                constraintSet.connect(groupLayout.getId(), ConstraintSet.TOP, mainLayout.getId(), ConstraintSet.TOP, 120 + i*35);
                constraintSet.connect(groupLayout.getId(), ConstraintSet.START, mainLayout.getId(), ConstraintSet.START, 50);
                constraintSet.applyTo(mainLayout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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
            Intent intent = new Intent(VideocartsActivity.this, MainActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item2) {
            Intent intent = new Intent(VideocartsActivity.this, PersonalAreaActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}