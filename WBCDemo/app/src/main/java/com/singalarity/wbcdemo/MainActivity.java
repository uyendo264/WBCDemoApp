package com.singalarity.wbcdemo;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_PHONE_STATE = 0;
    private String deviceID;

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
        else {

            requestWBC();
        }
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
        //progressDialog.show();
        setContentView((int) R.layout.activity_main);
        Cryption cryption = new Cryption(getApplicationContext());
        this.wbcCryption = cryption;
        cryption.WBCInit(deviceID);
        this.viewModel.setWBC(this.wbcCryption);
        //progressDialog.dismiss();
    }

    public String getDeviceID() {
        return this.deviceID;
    }




    /*public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {

            super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

*/

}
