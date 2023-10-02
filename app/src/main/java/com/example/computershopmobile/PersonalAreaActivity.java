package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.computershopmobile.Models.ResponseUser;
import com.example.computershopmobile.Models.User;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PersonalAreaActivity extends AppCompatActivity {
    UUID userId;
    String role;
    TextView textViewLogin;
    TextView textViewName;
    TextView textViewLastName;
    TextView textViewSurName;
    TextView textViewEmail;
    Button buttonChangeData;
    Button buttonAddAvatar;
    ImageView imageViewAvatar;
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_area);
        LoadData();
    }
    private void LoadData() {
        String getUserInfoUrl = IpAdress.getInstance().getIp() + "/user/userinfo";
        //String getUserInfoUrl = "http://5.3.79.15:13999/user/userinfo";
        //String getAvatarUrl = "http://5.3.79.15:13999/user/getavatar";
        String getAvatarUrl = IpAdress.getInstance().getIp() + "/user/getavatar";
        toolbar = findViewById(R.id.toolBarPA);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Личное кабинет");
        textViewLogin = findViewById(R.id.textViewLogin);
        textViewName = findViewById(R.id.textViewName);
        textViewLastName = findViewById(R.id.textViewLastName);
        textViewSurName = findViewById(R.id.textViewSurName);
        textViewEmail = findViewById(R.id.textViewEmail);
        buttonChangeData = findViewById(R.id.buttonChangeData);
        buttonAddAvatar = findViewById(R.id.buttonAddAvatar);
        imageViewAvatar = findViewById(R.id.imageView);
        Bundle extras = getIntent().getExtras();
        userId = UUID.fromString(extras.getString("id"));
        role = extras.getString("role");
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
        Future<byte[]> getAvatar = executorService.submit(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                byte[] avatar;
                String url = getAvatarUrl + "?id=" + userId.toString();
                avatar = HttpUtils.sendAvatarGetRequest(url);
                return avatar;
            }
        });
        try {
            User user = getUserInfo.get();
            user.setAvatar(getAvatar.get());
            textViewLogin.setText(user.getLogin());
            textViewEmail.setText(user.getEmail());
            if (user.getName() == null) {
                textViewName.setText("отсутствует");
            } else {
                textViewName.setText(user.getName());
            }
            if (user.getLastName() == null) {
                textViewLastName.setText("отсутствует");
            } else {
                textViewLastName.setText(user.getLastName());
            }
            if (user.getSurName() == null) {
                textViewSurName.setText("отсутствует");
            } else {
                textViewSurName.setText(user.getSurName());
            }
            Drawable drawable = getResources().getDrawable(R.drawable.avatar);
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            imageViewAvatar.setImageBitmap(BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length));
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        buttonAddAvatar.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        });
        buttonChangeData.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalAreaActivity.this, ChangeDataActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            File imageFile;
            if (selectedImageUri != null) {
                imageFile = new File(getRealPathFromURI(selectedImageUri));
            } else
            {
                imageFile = null;
            }
            String patchAvatarUrl = IpAdress.getInstance().getIp() + "/user/patchavatar";
            //String patchAvatarUrl = "http://5.3.79.15:13999/user/patchavatar";
            try {
                ExecutorService executorService = Executors.newFixedThreadPool(1);
                Future<String> patchAvatar = executorService.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        String response;
                        response = HttpUtils.sendPatchRequestAvatar(userId.toString(), imageFile, patchAvatarUrl);
                        return response;
                    }
                });
                executorService.shutdown();
                finish();
                startActivity(getIntent());
                LoadData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
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
            Intent intent = new Intent(PersonalAreaActivity.this, MainActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        if (id == R.id.menu_item2) {
            Intent intent = new Intent(PersonalAreaActivity.this, PersonalAreaActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

