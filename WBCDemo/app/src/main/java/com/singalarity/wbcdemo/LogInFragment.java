package com.singalarity.wbcdemo;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import com.singalarity.serverLib.UserInfo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class LogInFragment extends Fragment {
    /* access modifiers changed from: private */
    public Cryption cryption;
    EditText usernameText;
    /* access modifiers changed from: private */
    public SharedViewModel viewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
        this.usernameText = (EditText) view.findViewById(R.id.loginUsername_editText);
        final EditText passwordText = (EditText) view.findViewById(R.id.loginPassword_editText);
        SharedViewModel sharedViewModel = (SharedViewModel) ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        this.viewModel = sharedViewModel;
        sharedViewModel.getWBC().observe(getViewLifecycleOwner(), new Observer<Cryption>() {
            public void onChanged(Cryption cryption1) {
                Cryption unused = LogInFragment.this.cryption = cryption1;
            }
        });
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        view.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View view) {
                String username = LogInFragment.this.usernameText.getText().toString();
                String password = passwordText.getText().toString();
                String deviceID = ((MainActivity) LogInFragment.this.getActivity()).getDeviceID();
                UserInfo userInfo = new UserInfo(username, password);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(userInfo);
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String userInfoEncoded = Base64.getEncoder().encodeToString(baos.toByteArray());
                Log.d("Check serialize", userInfoEncoded);
                LogInFragment.this.cryption.WBCInit(deviceID);
                String encrypted = LogInFragment.this.cryption.EncryptWBC(userInfoEncoded);
                Log.d("Cryption", "encryption = " + encrypted);
                String respone = new RequestAPI().sendLoginData(deviceID, encrypted);
                Log.d("Login", "response: " + respone);
                if (respone.equals("200")) {
                    if (checkBox.isChecked()) {
                        databaseHandler.addUsers(new UserInfo_extend(username, encrypted));
                        UserInfo_extend check = databaseHandler.getUserInfo(username);
                        Log.d("SQLite", "Check get userinfo " + check.getUid());
                    }
                    LogInFragment.this.viewModel.setLoginStatus(true);
                    NavHostFragment.findNavController(LogInFragment.this).navigate((int) R.id.action_logInFragment_to_dashBoardFragment);
                    LogInFragment.this.viewModel.setUserName(username);
                }
            }
        });
        view.findViewById(R.id.registerFromLogin_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NavHostFragment.findNavController(LogInFragment.this).navigate((int) R.id.action_logInFragment_to_registerFragment);
            }
        });
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}