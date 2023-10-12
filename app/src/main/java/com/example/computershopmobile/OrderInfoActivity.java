package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.widget.NestedScrollView;

import com.example.computershopmobile.Models.Good;
import com.example.computershopmobile.Models.GoodId;
import com.example.computershopmobile.Models.Order;
import com.example.computershopmobile.Models.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class OrderInfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    UUID userId;
    UUID user_id;
    String role;
    String orderId;
    TextView textViewOrderName;
    TextView textViewName;
    TextView textViewLastname;
    TextView textViewSumm;
    TextView textViewCity;
    TextView textViewAdress;
    TextView textViewPhone;
    TextView textViewStatus;
    TextView textViewIsPaid;
    Order order;
    User user;
    Spinner spinner;
    Button buttonSave;
    ArrayList<Good> goods;
    NestedScrollView orderInfoNestedScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        LoadData();
    }
    private void LoadData() {
        String patchOrderUrl = IpAdress.getInstance().getIp() + "/order/patchgood";
        String getUserInfoUrl = IpAdress.getInstance().getIp() + "/user/userinfo";
        String getOrderUrl = IpAdress.getInstance().getIp() + "/order/getbyid";
        String getGoodsUrl = IpAdress.getInstance().getIp() + "/good/goodsbyid";
        toolbar = findViewById(R.id.toolBarOrderInfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Заказ");
        Bundle extras = getIntent().getExtras();
        userId = UUID.fromString(extras.getString("id"));
        role = extras.getString("role");
        orderId = extras.getString("orderid");
        user_id = UUID.fromString(extras.getString("userid"));
        textViewOrderName = findViewById(R.id.textViewOrderNameOrderInfo);
        textViewName = findViewById(R.id.textViewNameOrderInfo);
        textViewLastname = findViewById(R.id.textViewLastNameOrderInfo);
        textViewSumm = findViewById(R.id.textViewSummOrderInfo);
        textViewCity = findViewById(R.id.textViewCityOrderInfo);
        textViewAdress = findViewById(R.id.textViewAdressOrderInfo);
        textViewPhone = findViewById(R.id.textViewPhoneOrderInfo);
        textViewStatus = findViewById(R.id.textViewStatusOrderInfo);
        textViewIsPaid = findViewById(R.id.textViewIsPaidOrderInfo);
        spinner = findViewById(R.id.spinnerOrderInfo);
        buttonSave = findViewById(R.id.buttonSaveOrderInfo);
        String[] items = {"Укомплектовывается", "В пути", "Доставлен"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        ConstraintLayout mainLayout = findViewById(R.id.orderInfoConstraintLayout);
        NestedScrollView nestedScrollView = findViewById(R.id.orderInfoNestedScrollView);
        LinearLayout groupLayout;
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Future<User> getUserInfo = executorService.submit(new Callable<User>() {
            @Override
            public User call() throws Exception {
                User response = new User();
                String url = getUserInfoUrl + "?id=" + user_id.toString();
                response = HttpUtils.sendUserInfoGetRequest(url);
                return response;
            }
        });
        try {
            user = getUserInfo.get();
            textViewName.setText(user.getName());
            textViewLastname.setText(user.getLastName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Future <Order> getOrder = executorService.submit(new Callable<Order>() {
            @Override
            public Order call() throws Exception {
                Order response ;
                String url = getOrderUrl + "?id=" + orderId;
                response = HttpUtils.sendOrderGetRequest(url);
                return response;
            }
        });
        try {
            order = getOrder.get();
            //Toast.makeText(OrderInfoActivity.this, order.toString(), Toast.LENGTH_LONG).show();
            textViewOrderName.setText(order.getId().toString());
            textViewSumm.setText(order.getSumm() + " рублей");
            textViewCity.setText(order.getCity());
            textViewAdress.setText(order.getAdress());
            textViewPhone.setText(order.getPhone());
            if (role.equals("admin")) {
                textViewStatus.setVisibility(View.INVISIBLE);
                if (order.getStatus().equals("Укомплектовывается")) {
                    spinner.setSelection(0);
                }
                if (order.getStatus().equals("В пути")) {
                    spinner.setSelection(1);
                }
                if (order.getStatus().equals("Доставлен")) {
                    spinner.setSelection(2);
                }
            } else {
                spinner.setVisibility(View.INVISIBLE);
                buttonSave.setVisibility(View.INVISIBLE);
                textViewStatus.setText(order.getStatus());
            }

            if (order.getIsPaid()) {
                textViewIsPaid.setText("картой");
            } else {
                textViewIsPaid.setText("наличными");
            }
            String[] goodIds = new String[order.getGoods().length];
            UUID[] goodUuids = order.getGoods();
            for (int i = 0; i < order.getGoods().length; i++) {
                goodIds[i] = goodUuids[i].toString();
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
                if (goods.size() > 0) {
                    for (int i = 0; i < goods.size(); i++) {
                        groupLayout = new LinearLayout(this);
                        groupLayout.setLayoutParams(new ViewGroup.LayoutParams(
                                640,
                                160
                        ));
                        groupLayout.setOrientation(LinearLayout.HORIZONTAL);
                        GradientDrawable gradientDrawable = new GradientDrawable();
                        gradientDrawable.setStroke(4, getResources().getColor(R.color.white));
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

                        groupLayout.addView(imageView);
                        groupLayout.addView(textViewName);
                        groupLayout.addView(textViewPrice);


                        mainLayout.addView(groupLayout);
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(mainLayout);
                        constraintSet.connect(groupLayout.getId(), ConstraintSet.TOP, mainLayout.getId(), ConstraintSet.TOP,  i * 190);
                        constraintSet.connect(groupLayout.getId(), ConstraintSet.START, mainLayout.getId(), ConstraintSet.START, 50);
                        constraintSet.applyTo(mainLayout);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        buttonSave.setOnClickListener(v -> {
            Future <String> changeOrder = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String response;
                    JSONObject json = new JSONObject();
                    json.put("id", orderId);
                    json.put("status", spinner.getSelectedItem().toString());
                    response = HttpUtils.sendPatchRequest(patchOrderUrl, json);
                    return response;
                }
            });
            try {
                Toast.makeText(OrderInfoActivity.this, "Статус изменен успешно", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
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
            Intent intent = new Intent(OrderInfoActivity.this, MainActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item2) {
            Intent intent = new Intent(OrderInfoActivity.this, PersonalAreaActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item3) {
            Intent intent = new Intent(OrderInfoActivity.this, CorsinaActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item5) {
            Intent intent = new Intent(OrderInfoActivity.this, OrdersActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (role.equals("admin")) {
            if (id == R.id.menu_item4) {
                Intent intent = new Intent(OrderInfoActivity.this, CreateGoodActivity.class);
                intent.putExtra("id", userId.toString());
                intent.putExtra("role", role);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}