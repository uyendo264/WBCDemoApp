package com.singalarity.wbcdemo;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import com.singalarity.serverLib.UserInfo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class RegisterFragment extends Fragment {
    EditText usernameText;
    /* access modifiers changed from: private */
    public SharedViewModel viewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.usernameText = (EditText) view.findViewById(R.id.registerUsername_editText);
        final EditText passwordText = (EditText) view.findViewById(R.id.registerPassword_editText);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        view.findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View view) {
                Log.d("Register", "1111111111");
                String username = RegisterFragment.this.usernameText.getText().toString();
                String password = passwordText.getText().toString();
                String deviceID = ((MainActivity) RegisterFragment.this.getActivity()).getDeviceID();
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
                Cryption cryption = new Cryption(RegisterFragment.this.getContext());
                cryption.WBCInit(deviceID);
                String encrypted = cryption.EncryptWBC(userInfoEncoded);
                Log.d("Cryption", "encryption = " + encrypted);
                String respone = new RequestAPI().sendRegisterData(deviceID, encrypted);
                Log.d("Login", "response: " + respone);
                if (respone.equals("200")) {
                    NavHostFragment.findNavController(RegisterFragment.this).navigate((int) R.id.action_registerFragment_to_logInFragment);
                    viewModel.setUserName(username);
                }
            }
        });
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.getUserName().observe(getViewLifecycleOwner(), new Observer<String>() {
            public void onChanged(String s) {
                RegisterFragment.this.usernameText.setText(s);
            }
        });
    }
}