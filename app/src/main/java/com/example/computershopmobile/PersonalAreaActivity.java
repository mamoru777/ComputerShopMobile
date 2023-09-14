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
        //Toast.makeText(PersonalAreaActivity.this, userId.toString(), Toast.LENGTH_LONG).show();
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

            //Toast.makeText(PersonalAreaActivity.this, avatarBytes, Toast.LENGTH_LONG).show();
            //imageViewAvatar.setImageResource(R.drawable.avatar);
            Drawable drawable = getResources().getDrawable(R.drawable.avatar);
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String avatarBytes = Arrays.toString(byteArray);
            //Toast.makeText(PersonalAreaActivity.this, avatarBytes, Toast.LENGTH_LONG).show();
            imageViewAvatar.setImageBitmap(BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length));
            //imageViewAvatar.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
            //imageViewAvatar.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length), imageViewAvatar.getWidth(), imageViewAvatar.getHeight(), false));
            //imageViewAvatar.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(user.getAvatar(), 0, user.getAvatar().length), imageViewAvatar.getWidth(), imageViewAvatar.getHeight(), false));
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

                /*InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                //byte[] imageBytes = getBytesFromInputStream(inputStream);
                String imageBytesToString = Arrays.toString(byteArray);
                Toast.makeText(PersonalAreaActivity.this, imageBytesToString, Toast.LENGTH_LONG).show();*/
                ExecutorService executorService = Executors.newFixedThreadPool(1);
                Future<String> patchAvatar = executorService.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        String response;
                        //JSONObject json = new JSONObject();
                        //json.put("avatar", imageBytesToString);
                        //json.put("id", userId);
                        //response = HttpUtils.sendPatchRequest(patchAvatarUrl, json);
                        response = HttpUtils.sendPatchRequestAvatar(userId.toString(), imageFile, patchAvatarUrl);
                        return response;
                    }
                });
                executorService.shutdown();
                LoadData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Здесь вы можете использовать библиотеку для загрузки изображения, например Glide или Picasso, и отображать его или сохранять путь к файлу для использования позже в качестве аватарки
            // Пример использования Glide:

        }
    }
    private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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
    private File saveBitmapToFile(Bitmap bitmap) {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = new File(storageDir, imageFileName);

        try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile;
    }
}

