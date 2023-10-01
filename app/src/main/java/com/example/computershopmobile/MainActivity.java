package com.example.computershopmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    UUID userId;
    String role;
    ImageView imageViewVideo;
    ImageView imageViewProc;
    ImageView imageViewMother;
    ImageView imageViewMemory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoadData();
    }
    public void LoadData() {
        imageViewMemory = findViewById(R.id.imageViewMemory);
        imageViewMother = findViewById(R.id.imageViewMother);
        imageViewProc = findViewById(R.id.imageViewProc);
        imageViewVideo = findViewById(R.id.imageViewVideo);
        toolbar = findViewById(R.id.toolBarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Главное меню");
        Bundle extras = getIntent().getExtras();
        userId = UUID.fromString(extras.getString("id"));
        role = extras.getString("role");
        //Toast.makeText(MainActivity.this, userId.toString(), Toast.LENGTH_LONG).show();
        imageViewVideo.setImageResource(R.drawable.videocarts);
        imageViewProc.setImageResource(R.drawable.procs);
        imageViewMother.setImageResource(R.drawable.mother);
        imageViewMemory.setImageResource(R.drawable.memory);
        imageViewVideo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VideocartsActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        });
        imageViewProc.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProcActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        });
        imageViewMemory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MemoryActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        });
        imageViewMother.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MotherActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
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

        }
        if (id == R.id.menu_item2) {
            Intent intent = new Intent(MainActivity.this, PersonalAreaActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item3) {

        }
        if (role.equals("admin")) {
            if (id == R.id.menu_item4) {
                Intent intent = new Intent(MainActivity.this, CreateGoodActivity.class);
                intent.putExtra("id", userId.toString());
                intent.putExtra("role", role);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);


    }
}