package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.computershopmobile.Models.Good;
import com.example.computershopmobile.Models.Order;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OrdersActivity extends AppCompatActivity {

    Toolbar toolbar;
    String role;
    UUID userId;
    int ordersSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        LoadData();
    }
    private void LoadData() {
        String getOrdersUrl = IpAdress.getInstance().getIp() + "/order/getbyuserid";
        String getAllOrdersUrl = IpAdress.getInstance().getIp() + "/order/getall";
        toolbar = findViewById(R.id.toolBarOrders);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Заказы");
        Bundle extras = getIntent().getExtras();
        userId = UUID.fromString(extras.getString("id"));
        role = extras.getString("role");
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ConstraintLayout mainLayout = findViewById(R.id.ordersConstraintLayout);
        LinearLayout groupLayout;
        ArrayList<Order> orders = new ArrayList<>();
        if (role.equals("admin")) {
            Future<ArrayList<Order>> getOrders = executorService.submit(new Callable<ArrayList<Order>>() {
                @Override
                public ArrayList<Order> call() throws Exception {
                    ArrayList response ;
                    String url = getAllOrdersUrl;
                    response = HttpUtils.sendOrdersGetRequest(url);
                    return response;
                }
            });
            try {
                orders = getOrders.get();
                ordersSize = orders.size();
                for (int i = 0; i < orders.size(); i++) {
                    groupLayout = new LinearLayout(this);
                    groupLayout.setLayoutParams(new ViewGroup.LayoutParams(
                            640,
                            160
                    ));
                    groupLayout.setOrientation(LinearLayout.HORIZONTAL);
                    GradientDrawable gradientDrawable = new GradientDrawable();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        groupLayout.setId(View.generateViewId());
                    }
                    gradientDrawable.setStroke(4, getResources().getColor(R.color.white));
                    groupLayout.setBackground(gradientDrawable);

                    TextView textViewId = new TextView(this);
                    textViewId.setText("Заказ: " + orders.get(i).getId().toString());
                    textViewId.setTextSize(16);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        textViewId.setId(View.generateViewId());
                    }
                    ConstraintLayout.LayoutParams constraintLayoutId = new ConstraintLayout.LayoutParams(
                            170,
                            160
                    );
                    constraintLayoutId.topToTop = groupLayout.getId();
                    constraintLayoutId.endToEnd = groupLayout.getId();
                    textViewId.setLayoutParams(constraintLayoutId);

                    TextView textViewSumm = new TextView(this);
                    textViewSumm.setText("Сумма:  " + orders.get(i).getSumm() + " рублей");
                    textViewSumm.setTextSize(16);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        textViewSumm.setId(View.generateViewId());
                    }
                    ConstraintLayout.LayoutParams constraintLayoutSumm = new ConstraintLayout.LayoutParams(
                            170,
                            160
                    );
                    constraintLayoutSumm.topToTop = groupLayout.getId();
                    constraintLayoutSumm.endToEnd = groupLayout.getId();
                    textViewSumm.setLayoutParams(constraintLayoutSumm);

                    TextView textViewStatus = new TextView(this);
                    textViewStatus.setText("Статус заказа :  " + orders.get(i).getStatus());
                    textViewStatus.setTextSize(16);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        textViewStatus.setId(View.generateViewId());
                    }
                    ConstraintLayout.LayoutParams constraintLayoutStatus = new ConstraintLayout.LayoutParams(
                            170,
                            160
                    );
                    constraintLayoutStatus.topToTop = groupLayout.getId();
                    constraintLayoutStatus.endToEnd = groupLayout.getId();
                    textViewStatus.setLayoutParams(constraintLayoutStatus);

                    mainLayout.addView(groupLayout);

                    groupLayout.addView(textViewId);
                    groupLayout.addView(textViewSumm);
                    groupLayout.addView(textViewStatus);


                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(mainLayout);
                    constraintSet.connect(groupLayout.getId(), ConstraintSet.TOP, mainLayout.getId(), ConstraintSet.TOP, 120 + i * 190);
                    constraintSet.connect(groupLayout.getId(), ConstraintSet.START, mainLayout.getId(), ConstraintSet.START, 50);
                    constraintSet.applyTo(mainLayout);

                    String orderId = orders.get(i).getId().toString();
                    String user_id = orders.get(i).getUserId().toString();
                    groupLayout.setOnClickListener(v -> {
                        executorService.shutdown();
                        Intent intent = new Intent(OrdersActivity.this, OrderInfoActivity.class);
                        intent.putExtra("id", userId.toString());
                        intent.putExtra("role", role);
                        intent.putExtra("orderid", orderId);
                        intent.putExtra("userid", user_id);
                        startActivity(intent);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Future<ArrayList<Order>> getOrders = executorService.submit(new Callable<ArrayList<Order>>() {
                @Override
                public ArrayList<Order> call() throws Exception {
                    ArrayList response ;
                    String url = getOrdersUrl + "?user_id=" + userId.toString();
                    response = HttpUtils.sendOrdersGetRequest(url);
                    return response;
                }
            });
            try {
                orders = getOrders.get();
                ordersSize = orders.size();
                for (int i = 0; i < orders.size(); i++) {
                    groupLayout = new LinearLayout(this);
                    groupLayout.setLayoutParams(new ViewGroup.LayoutParams(
                            640,
                            160
                    ));
                    groupLayout.setOrientation(LinearLayout.HORIZONTAL);
                    GradientDrawable gradientDrawable=new GradientDrawable();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        groupLayout.setId(View.generateViewId());
                    }
                    gradientDrawable.setStroke(4,getResources().getColor(R.color.white));
                    groupLayout.setBackground(gradientDrawable);

                    TextView textViewId = new TextView(this);
                    textViewId.setText("Заказ: " + orders.get(i).getId().toString());
                    textViewId.setTextSize(16);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        textViewId.setId(View.generateViewId());
                    }
                    ConstraintLayout.LayoutParams constraintLayoutId = new ConstraintLayout.LayoutParams(
                            170,
                            160
                    );
                    constraintLayoutId.topToTop = groupLayout.getId();
                    constraintLayoutId.endToEnd = groupLayout.getId();
                    textViewId.setLayoutParams(constraintLayoutId);

                    TextView textViewSumm = new TextView(this);
                    textViewSumm.setText("Сумма:  " + orders.get(i).getSumm() + " рублей");
                    textViewSumm.setTextSize(16);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        textViewSumm.setId(View.generateViewId());
                    }
                    ConstraintLayout.LayoutParams constraintLayoutSumm = new ConstraintLayout.LayoutParams(
                            170,
                            160
                    );
                    constraintLayoutSumm.topToTop = groupLayout.getId();
                    constraintLayoutSumm.endToEnd = groupLayout.getId();
                    textViewSumm.setLayoutParams(constraintLayoutSumm);

                    TextView textViewStatus = new TextView(this);
                    textViewStatus.setText("Статус заказа :  " + orders.get(i).getStatus());
                    textViewStatus.setTextSize(16);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        textViewStatus.setId(View.generateViewId());
                    }
                    ConstraintLayout.LayoutParams constraintLayoutStatus = new ConstraintLayout.LayoutParams(
                            170,
                            160
                    );
                    constraintLayoutStatus.topToTop = groupLayout.getId();
                    constraintLayoutStatus.endToEnd = groupLayout.getId();
                    textViewStatus.setLayoutParams(constraintLayoutStatus);

                    mainLayout.addView(groupLayout);

                    groupLayout.addView(textViewId);
                    groupLayout.addView(textViewSumm);
                    groupLayout.addView(textViewStatus);


                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(mainLayout);
                    constraintSet.connect(groupLayout.getId(), ConstraintSet.TOP, mainLayout.getId(), ConstraintSet.TOP, 120 + i*190);
                    constraintSet.connect(groupLayout.getId(), ConstraintSet.START, mainLayout.getId(), ConstraintSet.START, 50);
                    constraintSet.applyTo(mainLayout);

                    String orderId = orders.get(i).getId().toString();
                    String user_id = orders.get(i).getUserId().toString();
                    groupLayout.setOnClickListener(v -> {
                        executorService.shutdown();
                        Intent intent = new Intent(OrdersActivity.this, OrderInfoActivity.class);
                        intent.putExtra("id", userId.toString());
                        intent.putExtra("role", role);
                        intent.putExtra("orderid", orderId);
                        intent.putExtra("userid", user_id);
                        startActivity(intent);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            Intent intent = new Intent(OrdersActivity.this, MainActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item2) {
            Intent intent = new Intent(OrdersActivity.this, PersonalAreaActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item3) {
            Intent intent = new Intent(OrdersActivity.this, CorsinaActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item5) {

        }
        if (role.equals("admin")) {
            if (id == R.id.menu_item4) {
                Intent intent = new Intent(OrdersActivity.this, CreateGoodActivity.class);
                intent.putExtra("id", userId.toString());
                intent.putExtra("role", role);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}