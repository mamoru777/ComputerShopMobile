package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.computershopmobile.Database.Storage.GoodIdStorage;
import com.example.computershopmobile.Models.Good;
import com.example.computershopmobile.Models.GoodId;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CorsinaActivity extends AppCompatActivity {

    Toolbar toolbar;
    UUID userId;
    String role;
    GoodIdStorage goodIdStorage;
    int Coord;
    float sum = 0;
    ArrayList<Good> goods;
    int goodSize;
    String[] goodIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corsina);
        LoadData();
    }
    private void LoadData() {
        String getCorsinaUrl = IpAdress.getInstance().getIp() + "/corsina/getcorsina";
        String getGoodsUrl = IpAdress.getInstance().getIp() + "/good/goodsbyid";
        String deleteGoodFromCorsina = IpAdress.getInstance().getIp() + "/order/deletegood";
        toolbar = findViewById(R.id.toolBarCorsina);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Корзина");
        Bundle extras = getIntent().getExtras();
        userId = UUID.fromString(extras.getString("id"));
        role = extras.getString("role");
        goodIdStorage = new GoodIdStorage(getBaseContext());
        //ArrayList<GoodId> goodId = goodIdStorage.getFullList(userId.toString());
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ConstraintLayout mainLayout = findViewById(R.id.corsinaConstraintLayout);
        LinearLayout groupLayout;
        Toast.makeText(CorsinaActivity.this, userId.toString(), Toast.LENGTH_LONG).show();
        Future<String[]> getCorsina = executorService.submit(new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {
                String[] response ;
                String url = getCorsinaUrl + "?user_id=" + userId.toString();
                response = HttpUtils.sendCorsinaGetRequest(url);
                return response;
            }
        });
        try {
            goodIds = getCorsina.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Future<ArrayList<Good>> getGoods = executorService.submit(new Callable<ArrayList<Good>>() {
            @Override
            public ArrayList<Good> call() throws Exception {
                ArrayList response ;
                String url = "";
                url = getGoodsUrl + "?";
                for (String value : goodIds) {
                    url += "ides=" + value + "&";
                }
                response = HttpUtils.sendGoodsGetRequest(url);
                return response;
            }
        });
        try {
            goods = getGoods.get();
            goodSize = goods.size();
            if (goods.size() > 0) {
                for (int i = 0; i < goods.size(); i++) {
                    sum += goods.get(i).getPrice();
                    groupLayout = new LinearLayout(this);
                    groupLayout.setLayoutParams(new ViewGroup.LayoutParams(
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

                    Button button = new Button(this);
                    button.setText("-");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        button.setId(View.generateViewId());
                    }
                    ConstraintLayout.LayoutParams layoutParamsButton = new ConstraintLayout.LayoutParams(
                            80,
                            80
                    );
                    layoutParamsButton.topToTop = groupLayout.getId();
                    layoutParamsButton.endToEnd = groupLayout.getId();
                    button.setLayoutParams(layoutParamsButton);

                    groupLayout.addView(imageView);
                    groupLayout.addView(textViewName);
                    groupLayout.addView(textViewPrice);
                    groupLayout.addView(button);

                    mainLayout.addView(groupLayout);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(mainLayout);
                    constraintSet.connect(groupLayout.getId(), ConstraintSet.TOP, mainLayout.getId(), ConstraintSet.TOP, 120 + i*190);
                    constraintSet.connect(groupLayout.getId(), ConstraintSet.START, mainLayout.getId(), ConstraintSet.START, 50);
                    constraintSet.applyTo(mainLayout);
                    int j = i;
                    button.setOnClickListener(v -> {
                        //goodIdStorage.delete(goodIdStorage.get(goodId.get(j).getId()));
                        finish();
                        startActivity(getIntent());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        LoadData();
                    });
                    Coord = i;
                }
                TextView textViewPrice = new TextView(this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    textViewPrice.setId(View.generateViewId());
                }
                textViewPrice.setText("Сумма: " + sum + " рублей");
                textViewPrice.setTextSize(16);
                ConstraintLayout.LayoutParams constraintLayoutPrice = new ConstraintLayout.LayoutParams(
                        640,
                        60
                );
                textViewPrice.setLayoutParams(constraintLayoutPrice);
                mainLayout.addView(textViewPrice);

                Button button = new Button(this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    button.setId(View.generateViewId());
                }
                button.setText("Оформить заказ");
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT, // Ширина кнопки (может быть WRAP_CONTENT или MATCH_PARENT)
                        ConstraintLayout.LayoutParams.WRAP_CONTENT // Высота кнопки (может быть WRAP_CONTENT или MATCH_PARENT)
                );
                button.setLayoutParams(layoutParams);

                mainLayout.addView(button);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(mainLayout);

                constraintSet.connect(button.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                constraintSet.connect(button.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
                constraintSet.connect(button.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 400 + Coord*190);
                constraintSet.connect(button.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                constraintSet.connect(textViewPrice.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 310 + Coord*190);
                constraintSet.connect(textViewPrice.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 50);
                constraintSet.applyTo(mainLayout);

                button.setOnClickListener(v -> {
                    executorService.shutdown();
                    Intent intent = new Intent(CorsinaActivity.this, OrderActivity.class);
                    intent.putExtra("id", userId.toString());
                    intent.putExtra("role", role);
                    intent.putExtra("summ", sum);
                    intent.putExtra("number", goodSize);
                    startActivity(intent);
                });
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        if (goodSize == 0) {
            TextView textViewEmpty = new TextView(this);
            textViewEmpty.setText("Корзина пуста");
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
            Intent intent = new Intent(CorsinaActivity.this, MainActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item2) {
            Intent intent = new Intent(CorsinaActivity.this, PersonalAreaActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item3) {

        }
        if (id == R.id.menu_item5) {
            Intent intent = new Intent(CorsinaActivity.this, OrdersActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (role.equals("admin")) {
            if (id == R.id.menu_item4) {
                Intent intent = new Intent(CorsinaActivity.this, CreateGoodActivity.class);
                intent.putExtra("id", userId.toString());
                intent.putExtra("role", role);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}