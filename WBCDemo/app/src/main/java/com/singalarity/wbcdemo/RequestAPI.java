package com.singalarity.wbcdemo;


import android.util.Log;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestAPI {
    private String url;

    public RequestAPI(String url2) {
        this.url = url2;
    }

    public RequestAPI() {
        this.url = "http://192.168.43.229:8081";
    }

    public String sendDeviceID(String deviceID) {
        return sendInitRequest(new RequestConstant().initWBCPath, new RequestObject(deviceID).InitObject());
    }

    public String sendLoginData(String deviceID, String userInfoEncoded) {
        return sendRequest(new RequestConstant().loginPath, new RequestObject(deviceID).LogInObject(userInfoEncoded));
    }

    public String sendRegisterData(String deviceID, String userInfoEncoded) {
        return sendRequest(new RequestConstant().registerPath, new RequestObject(deviceID).RegisterObject(userInfoEncoded));
    }

    public String sendInitRequest(String path, JSONObject jsonObject) {
        Log.d("sendRequest", this.url + path);
        final String[] result = new String[1];
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(this.url + path).post(body).build();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                result[0] = e.getMessage();
                Log.d("Test", "Error" + e.getMessage());
                countDownLatch.countDown();
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        result[0] = new JSONObject(response.body().string()).getString("wbcFileString");
                        Log.d("TEST", "result: " + result[0]);
                        countDownLatch.countDown();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        result[0] = e.getMessage();
                        countDownLatch.countDown();
                    }
                }
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("Return result", "result = " + result[0]);
        return result[0];
    }

    public String sendRequest(String path, JSONObject jsonObject) {
        Log.d("sendRequest", this.url + path);
        final String[] result = new String[1];
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(this.url + path).post(body).build();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                result[0] = e.getMessage();
                Log.d("Test", "Error" + e.getMessage());
                countDownLatch.countDown();
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String myResponse = response.body().string();
                        Log.d("sendRequest", "myResponse = " + myResponse);
                        result[0] = RequestAPI.this.getResponseCode(new JSONObject(myResponse));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();
                    return;
                }
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("Return result", "result = " + result[0]);
        return result[0];
    }

    public String getResponseCode(JSONObject jsonObject) {
        String code = "";
        try {
            String jsonArrayCode = jsonObject.getString("code");
            code = jsonArrayCode;
            Log.d("getResponseCode", jsonArrayCode);
            return code;
        } catch (JSONException e) {
            e.printStackTrace();
            return code;
        }
    }
}