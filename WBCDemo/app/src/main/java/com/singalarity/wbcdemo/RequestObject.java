package com.singalarity.wbcdemo;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestObject {
    private String deviceID;

    public RequestObject(String deviceID2) {
        this.deviceID = deviceID2;
    }

    public JSONObject InitWBCObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deviceID", this.deviceID);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TEST", "Exception raised while attempting to create JSON payload for upload.");
        }
        return jsonObject;
    }

    public JSONObject LogInObject(String userInfoEncode) {
        JSONObject jsonObject = new JSONObject();
        Log.d("Login Object", "userInfoEncode" + userInfoEncode);
        try {
            jsonObject.put("deviceID", this.deviceID);
            jsonObject.put("userInfoEncode", userInfoEncode);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TEST", "Exception raised while attempting to create JSON payload for upload.");
        }
        return jsonObject;
    }

    public JSONObject RegisterObject(String userInfoRegisterEncode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deviceID", this.deviceID);
            jsonObject.put("userInfoEncode", userInfoRegisterEncode);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TEST", "Exception raised while attempting to create JSON payload for upload.");
        }
        return jsonObject;
    }
}