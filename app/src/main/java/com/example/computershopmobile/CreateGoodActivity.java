package com.example.computershopmobile;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CreateGoodActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_IMAGE = 1;
    Button buttonAddAvatar;
    Button buttonCreateGood;
    Button buttonBackToMain;
    EditText editTextName;
    EditText editTextDescription;
    TextView textViewTypeCrGood;
    TextView textViewAvatarCrGood;
    Spinner spinnerType;
    ImageView imageViewAvatar;
    EditText editTextPrice;
    UUID userId;
    String role;

    File avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_good);
        LoadData();
    }
    private void LoadData() {
        String goodCreateUrl = IpAdress.getInstance().getIp() + "/good/create";
        String checkGoodUrl = IpAdress.getInstance().getIp() + "/good/goodcheck";
        Bundle extras = getIntent().getExtras();
        userId = UUID.fromString(extras.getString("id"));
        role = extras.getString("role");
        textViewTypeCrGood = findViewById(R.id.textViewTypeCrGood);
        buttonAddAvatar = findViewById(R.id.buttonAddAvatarCrGood);
        buttonCreateGood = findViewById(R.id.buttonAddGoodCrGood);
        buttonBackToMain = findViewById(R.id.buttonCancelCrGood);
        editTextName = findViewById(R.id.editTextNameCrGood);
        editTextDescription = findViewById(R.id.editTextDescriptionCrGood);
        imageViewAvatar = findViewById(R.id.imageViewCrGood);
        spinnerType = findViewById(R.id.spinnerCrGood);
        editTextPrice = findViewById(R.id.editTextPriceCrGood);
        textViewAvatarCrGood = findViewById(R.id.textViewAvatarCrGood);
        String[] items = {"Видеокарты", "Процессоры", "Материнские платы", "Оперативная память"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        /*spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

        });*/
        buttonAddAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        });
        buttonBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(CreateGoodActivity.this, MainActivity.class);
            intent.putExtra("id", userId.toString());
            intent.putExtra("role", role);
            startActivity(intent);
        });
        buttonCreateGood.setOnClickListener(v -> {
            if (imageViewAvatar.getDrawable() != null) {
                if (!editTextName.getText().toString().trim().isEmpty()) {
                    if (!editTextDescription.getText().toString().trim().isEmpty()) {
                        if (!editTextPrice.getText().toString().trim().isEmpty()) {
                            if (spinnerType.getSelectedItem() != null) {
                                Future<Boolean> isEmailExist = executorService.submit(new Callable<Boolean>() {
                                    @Override
                                    public Boolean call() throws Exception {
                                        Boolean response;
                                        String url = checkGoodUrl + "?name=" + editTextName.getText().toString();
                                        response = HttpUtils.sendGetRequest(url);
                                        return response;
                                    }
                                });
                                try {
                                    if (isEmailExist.get()) {
                                        editTextName.setError("Товар с таким названием уже существует");
                                        editTextName.requestFocus();
                                    } else {
                                        Future<String> postGood = executorService.submit(new Callable<String>() {
                                            @Override
                                            public String call() throws Exception {
                                                String response;
                                                response = HttpUtils.sendPostRequestGood(spinnerType.getSelectedItem().toString() ,editTextName.getText().toString(), editTextDescription.getText().toString(), editTextPrice.getText().toString(), avatar ,goodCreateUrl);
                                                return response;
                                            }
                                        });
                                        try {
                                            Thread.sleep(3000);
                                            executorService.shutdown();
                                            Toast.makeText(CreateGoodActivity.this, "Товар успешно создан", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(CreateGoodActivity.this, MainActivity.class);
                                            intent.putExtra("id", userId.toString());
                                            intent.putExtra("role", role);
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                textViewTypeCrGood.setError("Выберите тип товара");
                                spinnerType.requestFocus();
                            }
                        } else {
                            editTextPrice.setError("Добавте цену товара");
                            editTextPrice.requestFocus();
                        }
                    } else {
                        editTextDescription.setError("Добавте описание товара");
                        editTextDescription.requestFocus();
                    }
                } else {
                    editTextName.setError("Добавте название товара");
                    editTextName.requestFocus();
                }
            } else {
                //textViewAvatarCrGood.setError("Добавте изображение для товара");
                //textViewAvatarCrGood.requestFocus();
                Toast.makeText(CreateGoodActivity.this, "Добавте изображение для товара", Toast.LENGTH_LONG).show();
            }
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
                avatar = imageFile;
            } else
            {
                imageFile = null;
            }
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imageViewAvatar.setImageBitmap(bitmap);
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