package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.example.computershopmobile.Models.Good;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GoodsActivity extends AppCompatActivity {

    String role;
    UUID userId;
    Toolbar toolbar;
    String goodType;
    int goodSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        LoadData();
    }
    private void LoadData() {
        toolbar = findViewById(R.id.toolBarVideo);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        userId = UUID.fromString(extras.getString("id"));
        role = extras.getString("role");
        goodType = extras.getString("good_type");
        if (goodType.equals("Видеокарты")) {
            getSupportActionBar().setTitle("Видеокарты");
        }
        if (goodType.equals("Процессоры")) {
            getSupportActionBar().setTitle("Процессоры");
        }
        if (goodType.equals("Оперативная память")) {
            getSupportActionBar().setTitle("Оперативная память");
        }
        if (goodType.equals("Материнские платы")) {
            getSupportActionBar().setTitle("Материнские платы");
        }
        ConstraintLayout mainLayout = findViewById(R.id.videocarrsConstraintLayout);
        String getGoodsUrl = IpAdress.getInstance().getIp() + "/good/goodsbytype";
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        LinearLayout groupLayout = new LinearLayout(this);;
        Future<ArrayList<Good>> getGoods = executorService.submit(new Callable<ArrayList<Good>>() {
            @Override
            public ArrayList<Good> call() throws Exception {
                ArrayList response ;
                String url = "";
                if (goodType.equals("Видеокарты")) {
                    url = getGoodsUrl + "?good_type=" + "Видеокарты";
                }
                if (goodType.equals("Процессоры")) {
                    url = getGoodsUrl + "?good_type=" + "Процессоры";
                }
                if (goodType.equals("Оперативная память")) {
                    url = getGoodsUrl + "?good_type=" + "Оперативная память";
                }
                if (goodType.equals("Материнские платы")) {
                    url = getGoodsUrl + "?good_type=" + "Материнские платы";
                }
                response = HttpUtils.sendGoodsGetRequest(url);
                return response;
            }
        });
        try {
            ArrayList<Good> goods = getGoods.get();
            goodSize = goods.size();
            for (int i = 0; i < goods.size(); i++) {
                groupLayout = new LinearLayout(this);
                groupLayout.setLayoutParams(new LayoutParams(
                        640,
                        160
                ));
                groupLayout.setOrientation(LinearLayout.HORIZONTAL);
                GradientDrawable gradientDrawable=new GradientDrawable();
                gradientDrawable.setStroke(4,getResources().getColor(R.color.white));
                groupLayout.setBackground(gradientDrawable);
                ConstraintLayout.LayoutParams constraintLayoutAvatar = new ConstraintLayout.LayoutParams(
                        160,
                        160
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

                TextView textViewDescription = new TextView(this);
                textViewDescription.setText(goods.get(i).getDescription());
                textViewDescription.setTextSize(16);
                ConstraintLayout.LayoutParams constraintLayoutDescription = new ConstraintLayout.LayoutParams(
                        170,
                        160
                );
                constraintLayoutDescription.topToTop = groupLayout.getId();
                constraintLayoutDescription.endToEnd = groupLayout.getId();
                textViewDescription.setLayoutParams(constraintLayoutDescription);

                TextView textViewName = new TextView(this);
                textViewName.setText(goods.get(i).getName());
                textViewName.setTextSize(16);
                ConstraintLayout.LayoutParams constraintLayoutName = new ConstraintLayout.LayoutParams(
                        170,
                        160
                );
                constraintLayoutName.topToTop = groupLayout.getId();
                constraintLayoutName.endToEnd = groupLayout.getId();
                textViewName.setLayoutParams(constraintLayoutName);

                TextView textViewPrice = new TextView(this);
                textViewPrice.setText(String.valueOf(goods.get(i).getPrice()) + " руб.");
                textViewPrice.setTextSize(16);
                ConstraintLayout.LayoutParams constraintLayoutPrice = new ConstraintLayout.LayoutParams(
                        140,
                        160
                );
                constraintLayoutPrice.topToTop = groupLayout.getId();
                constraintLayoutPrice.endToEnd = groupLayout.getId();
                textViewPrice.setLayoutParams(constraintLayoutPrice);

                groupLayout.addView(imageView);
                groupLayout.addView(textViewName);
                groupLayout.addView(textViewDescription);
                groupLayout.addView(textViewPrice);

                mainLayout.addView(groupLayout);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(mainLayout);
                constraintSet.connect(groupLayout.getId(), ConstraintSet.TOP, mainLayout.getId(), ConstraintSet.TOP, 120 + i*190);
                constraintSet.connect(groupLayout.getId(), ConstraintSet.START, mainLayout.getId(), ConstraintSet.START, 50);
                constraintSet.applyTo(mainLayout);

                String goodId = goods.get(i).getId().toString();

                groupLayout.setOnClickListener(v -> {
                    executorService.shutdown();
                    Intent intent = new Intent(GoodsActivity.this, GoodActivity.class);
                    intent.putExtra("id", userId.toString());
                    intent.putExtra("role", role);
                    intent.putExtra("goodId", goodId);
                    startActivity(intent);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*groupLayout.setOnClickListener(v -> {
            groupLayout.getId();
        });*/
        if (goodSize == 0) {
            TextView textViewEmpty = new TextView(this);
            textViewEmpty.setText("Товары отсутствуют");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textViewEmpty.setId(View.generateViewId());
            }
            textViewEmpty.setTextSize(30);
            ConstraintLayout.LayoutParams constraintLayoutDescription = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT, // Ширина кнопки (может быть WRAP_CONTENT или MATCH_PARENT)
                    ConstraintLayout.LayoutParams.WRAP_CONTENT // Высота кнопки (может быть WRAP_CONTENT или MATCH_PARENT)
            );

            //constraintLayoutDescription.topToTop = mainLayout.getId();
            // constraintLayoutDescription.endToEnd = mainLayout.getId();
            textViewEmpty.setLayoutParams(constraintLayoutDescription);
            mainLayout.addView(textViewEmpty);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(mainLayout);
            constraintSet.connect(textViewEmpty.getId(), ConstraintSet.START, mainLayout.getId(), ConstraintSet.START);
            constraintSet.connect(textViewEmpty.getId(), ConstraintSet.END, mainLayout.getId(), ConstraintSet.END);
            constraintSet.connect(textViewEmpty.getId(), ConstraintSet.TOP, mainLayout.getId(), ConstraintSet.TOP, 600);
            constraintSet.connect(textViewEmpty.getId(), ConstraintSet.BOTTOM, mainLayout.getId(), ConstraintSet.BOTTOM);
            constraintSet.applyTo(mainLayout);

        }
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
            Intent intent = new Intent(GoodsActivity.this, MainActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item2) {
            Intent intent = new Intent(GoodsActivity.this, PersonalAreaActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item3) {
            Intent intent = new Intent(GoodsActivity.this, CorsinaActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item5) {
            Intent intent = new Intent(GoodsActivity.this, OrdersActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (role.equals("admin")) {
            if (id == R.id.menu_item4) {
                Intent intent = new Intent(GoodsActivity.this, CreateGoodActivity.class);
                intent.putExtra("id", userId.toString());
                intent.putExtra("role", role);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}