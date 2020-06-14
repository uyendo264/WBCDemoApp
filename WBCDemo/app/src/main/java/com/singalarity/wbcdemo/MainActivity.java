package com.singalarity.wbcdemo;

import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainACTIVITY", "okey");
        this.viewModel = (SharedViewModel) ViewModelProviders.of((FragmentActivity) this).get(SharedViewModel.class);

        requestPermission();
        if (!requestPermission()){
            return;
        }
        requestWBC();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Boolean requestPermission() {
        PhoneStatePermissionRequest phoneStatePermissionRequest = new PhoneStatePermissionRequest(getApplicationContext(),this);
        if (phoneStatePermissionRequest.getIsPermissionGranted()) {
            this.deviceID = phoneStatePermissionRequest.getDeviceID();
            return true;
        }
        return false;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void requestWBC() {
        setContentView((int) R.layout.activity_main);
        Cryption cryption = new Cryption(getApplicationContext());
        this.wbcCryption = cryption;
        cryption.WBCInit(this.deviceID);
        this.viewModel.setWBC(this.wbcCryption);
    }

    public String getDeviceID() {
        return this.deviceID;
    }
}
