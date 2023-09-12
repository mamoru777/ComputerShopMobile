package com.example.computershopmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    UUID userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoadData();
    }
    public void LoadData() {
        toolbar = findViewById(R.id.toolBarMain);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        userId = UUID.fromString(extras.getString("id"));
        Toast.makeText(MainActivity.this, userId.toString(), Toast.LENGTH_LONG).show();
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
        /*switch (item.getItemId()) {
            case MENU_ITEM_1:
                // Действия, связанные с элементом меню 1
                return true;
            case MENU_ITEM_2:
                // Действия, связанные с элементом меню 2
                Intent intent = new Intent(MainActivity.this, PersonalAreaActivity.class);
                startActivity(intent);
                return true;
            case MENU_ITEM_3:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
        if (id == R.id.menu_item1) {

        }
        if (id == R.id.menu_item2) {
            Intent intent = new Intent(MainActivity.this, PersonalAreaActivity.class);
            intent.putExtra("id", userId.toString());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}