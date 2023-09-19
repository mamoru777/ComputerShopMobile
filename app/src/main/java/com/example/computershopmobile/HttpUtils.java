package com.example.computershopmobile;

import com.example.computershopmobile.Models.ResponseUser;
import com.example.computershopmobile.Models.User;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtils {
    private static final OkHttpClient client = new OkHttpClient();

    public static User sendUserInfoGetRequest(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();
        User user = new User();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String jsonString = responseBody.string();
                JSONObject jsonObject = new JSONObject(jsonString);
                user.setLogin(jsonObject.getString("login"));
                user.setPassword(jsonObject.getString("password"));
                user.setName(jsonObject.getString("name"));
                user.setLastName(jsonObject.getString("lastname"));
                user.setSurName(jsonObject.getString("surname"));
                user.setEmail(jsonObject.getString("email"));
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] sendAvatarGetRequest(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            byte[] imageBytes = response.body().bytes();
            return imageBytes;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static ResponseUser sendGetRequestLogin(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();
        ResponseUser responseUser = new ResponseUser();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String jsonString = responseBody.string();
                JSONObject jsonObject = new JSONObject(jsonString);
                responseUser.setIsExist(jsonObject.getBoolean("isExist"));
                responseUser.setId(UUID.fromString(jsonObject.getString("id")));
                responseUser.setRole(jsonObject.getString("role"));
                return responseUser;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendPatchRequestAvatar(String id, File avatar, String url) throws Exception {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("id", id).addFormDataPart("avatar", avatar.getName(), RequestBody.create(MediaType.parse("image/jpeg"),avatar)).build();
        Request request = new Request.Builder().patch(requestBody).url(url).build();
        try (Response response = client.newCall(request).execute()){
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    public static Boolean sendGetRequest(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String jsonString = responseBody.string();
                JSONObject jsonObject = new JSONObject(jsonString);
                boolean success = jsonObject.getBoolean("isExist");
                return success;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        /*try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }*/
    }

    public static String sendPostRequest(String url, JSONObject json) throws Exception {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(json.toString(), mediaType);
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String sendPutRequest(String url, JSONObject json) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(json.toString(), mediaType);
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().toString();
        }
    }

    public static String sendDeleteRequest(String url, JSONObject json) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(json.toString(), mediaType);
        Request request = new Request.Builder()
                .url(url)
                .delete(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String sendPatchRequest(String url, JSONObject json) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(json.toString(), mediaType);
        Request request = new Request.Builder()
                .url(url)
                .patch(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().toString();
        }
    }
}
