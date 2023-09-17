package com.example.computershopmobile;

import androidx.appcompat.app.AppCompatActivity;

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
    TextView textViewLogin;
    TextView textViewName;
    TextView textViewLastName;
    TextView textViewSurName;
    TextView textViewEmail;
    Button buttonChangeData;
    Button buttonAddAvatar;
    ImageView imageViewAvatar;
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_area);
        LoadData();
    }
    private void LoadData() {
        //String getUserInfoUrl = "http://10.0.2.2:13999/user/userinfo";
        String getUserInfoUrl = "http://5.3.79.15:13999/user/userinfo";
        String getAvatarUrl = "http://5.3.79.15:13999/user/getavatar";
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
            //String patchAvatarUrl = "http://10.0.2.2:13999/user/patchavatar";
            String patchAvatarUrl = "http://5.3.79.15:13999/user/patchavatar";
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

}

