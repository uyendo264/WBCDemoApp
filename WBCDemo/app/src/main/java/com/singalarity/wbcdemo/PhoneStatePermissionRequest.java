package com.singalarity.wbcdemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;


import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import cz.muni.fi.xklinec.whiteboxAES.App;


public class PhoneStatePermissionRequest {
    private Context context;
    private Activity activity;
    private Boolean isPermissionGranted = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public PhoneStatePermissionRequest(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        if (ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == 0) {
            isPermissionGranted = true;
        }
        else {
            requestPermissions();
        }
    }
    //@Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");

        if (ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == 0) {
            //this.deviceID = telephonyManager.getDeviceId();
            isPermissionGranted = true;

        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.READ_PHONE_STATE"}, 1);
        if (ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == 0) {
            isPermissionGranted = true;
        }
        else {
            requestPermissions();
        }
    }
    public String getDeviceID(){
        if (isPermissionGranted == true){
            return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        }
        return  null;
    }
    public Boolean getIsPermissionGranted(){
        return  this.isPermissionGranted;
    }

}
