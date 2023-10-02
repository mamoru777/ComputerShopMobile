package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
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

import java.util.UUID;

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

        LinearLayout groupLayout = new LinearLayout(this);
        groupLayout.setLayoutParams(new LayoutParams(
                360,
                120
        ));
        groupLayout.setOrientation(LinearLayout.HORIZONTAL);
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LayoutParams(
                110,
                110
        ));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            groupLayout.setId(View.generateViewId());
        }
        imageView.setImageResource(R.drawable.avatar);
        ConstraintLayout mainLayout = findViewById(R.id.videocarrsConstraintLayout);
        mainLayout.addView(groupLayout);
        // Создайте TextView для текста
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LayoutParams(
                200,
                30
        ));
        textView.setText("Ваш текст здесь");

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);
        constraintSet.connect(groupLayout.getId(), ConstraintSet.TOP, mainLayout.getId(), ConstraintSet.TOP, 120);
        constraintSet.connect(groupLayout.getId(), ConstraintSet.START, mainLayout.getId(), ConstraintSet.START, 50);
        constraintSet.applyTo(mainLayout);
        // Добавьте ImageView и TextView в groupLayout
        groupLayout.addView(imageView);
        groupLayout.addView(textView);

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