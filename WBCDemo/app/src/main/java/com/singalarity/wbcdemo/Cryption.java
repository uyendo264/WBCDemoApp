package com.singalarity.wbcdemo;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;

import cz.muni.fi.xklinec.whiteboxAES.WBCStringCryption;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.Base64;

public class Cryption {
    private Context context;
    private WBCStringCryption wbcStringEncryption;

    public Cryption(Context context2){
        this.context = context2;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void WBCInit(String deviceID) {
        //ProgressDialog progressDialog = new ProgressDialog(context);

        Log.d("File Path", String.valueOf(this.context.getFilesDir()));

        if (new File(this.context.getFilesDir(), "MobileFile").exists()) {
            Log.d("Check WBC file", "file exist");
            try {
                this.wbcStringEncryption = (WBCStringCryption) new ObjectInputStream(this.context.openFileInput("MobileFile")).readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (ClassNotFoundException e3) {
                e3.printStackTrace();
            }
        } else {
            Log.d("Check WBC file", "create new WBC file");

            String encoded = new RequestAPI().requestWBCString(deviceID);


            Log.d("CheckEncoded", "encoded: " + encoded);
            byte[] decodebytes = Base64.getDecoder().decode(encoded);
            try {
                FileOutputStream fos = this.context.openFileOutput(String.valueOf(new File("MobileFile")), 0);
                fos.write(decodebytes);
                fos.flush();
                fos.close();
                FileInputStream mobileFile = this.context.openFileInput("MobileFile");
                ObjectInputStream mobileIn = new ObjectInputStream(mobileFile);
                this.wbcStringEncryption = (WBCStringCryption) mobileIn.readObject();
                mobileFile.close();
                mobileIn.close();
                Log.d("TEST1", "SerializedWBC: " + this.wbcStringEncryption.getStringCryptionResult("123123213213213"));
            } catch (FileNotFoundException e4) {
                e4.printStackTrace();
            } catch (IOException e5) {
                e5.printStackTrace();
            } catch (ClassNotFoundException e6) {
                e6.printStackTrace();
            }
        }
    }

    public WBCStringCryption getWbcStringEncryption() {
        return this.wbcStringEncryption;
    }

    public String EncryptWBC(String plaintext) {
        WBCStringCryption WBCStringEncryption = this.wbcStringEncryption;
        PrintStream printStream = System.out;
        printStream.println("plaintext String: " + plaintext);
        String encrypted = WBCStringEncryption.getStringCryptionResult(plaintext);
        PrintStream printStream2 = System.out;
        printStream2.println("encrypted String: " + encrypted);
        return encrypted;
    }
}
