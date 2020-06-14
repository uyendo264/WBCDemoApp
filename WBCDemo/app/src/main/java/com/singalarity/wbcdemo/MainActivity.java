package com.singalarity.wbcdemo;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_PHONE_STATE = 0;
    private String deviceID;
    private View mLayout;
    private SharedViewModel viewModel;
    private Cryption wbcCryption;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainACTIVITY", "okey");
        this.viewModel = (SharedViewModel) ViewModelProviders.of((FragmentActivity) this).get(SharedViewModel.class);
        InitApp();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
        Log.d("Check device ID", "get deviceid");
        if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") == 0) {
            this.deviceID = telephonyManager.getDeviceId();
            InitWBC();
        }
    }

    private void InitApp() {
        if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") == 0) {
            Log.d("Permission", "granted");
            Log.d("Check device ID", "get deviceid");
            this.deviceID = ((TelephonyManager) getSystemService("phone")).getDeviceId();
            Log.d("Check device ID", "Device ID = " + this.deviceID);
            InitWBC();
            return;
        }
        Log.d("Permission", "not granted");
        requestPermissions();
    }

    private void InitWBC() {
        setContentView((int) R.layout.activity_main);
        Cryption cryption = new Cryption(getApplicationContext());
        this.wbcCryption = cryption;
        cryption.WBCInit(this.deviceID);
        this.viewModel.setWBC(this.wbcCryption);
    }

    private void requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_PHONE_STATE")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_PHONE_STATE"}, 1);
            Log.d("Permission", "request permission");
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_PHONE_STATE"}, 1);
    }

    public String getDeviceID() {
        return this.deviceID;
    }
}
