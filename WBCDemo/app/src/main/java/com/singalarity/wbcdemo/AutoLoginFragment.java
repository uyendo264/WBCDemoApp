package com.singalarity.wbcdemo;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import java.util.List;

public class AutoLoginFragment extends Fragment {
    private SharedViewModel viewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto_login, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseHandler db = new DatabaseHandler(getActivity());
        int numberUsers = db.getNumberUsers();
        final List<UserInfo_extend> list = db.getAllUserInfo();
        Log.d("List", "list size = " + list.size());
        int count = list.size();
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.autoLoginframeLayout);
        Button[] buttons = new Button[count];
        for (int i = 0; i < count; i++) {
            final int j = i;
            String username = list.get(j).getUid();
            buttons[i] = new Button(getActivity());
            buttons[i].setId(i);
            buttons[i].setText(username);
            layout.addView(buttons[i]);
            Log.d("Welcome Frament", "addview buttons");
            buttons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    RequestAPI requestAPI = new RequestAPI();
                    Log.d("Welcome Frament", "init request API");
                    String encrypted = ((UserInfo_extend) list.get(j)).getHashPassword();
                    Log.d("Welcome Frament", "encrypted = " + encrypted);
                    String deviceID = ((MainActivity) AutoLoginFragment.this.getActivity()).getDeviceID();
                    Log.d("Welcome Frament", "deviceID = " + deviceID);
                    String respone = requestAPI.sendLoginData(deviceID, encrypted);
                    Log.d("Login", "response: " + respone);
                    if (respone.equals("200")) {
                        NavHostFragment.findNavController(AutoLoginFragment.this).navigate((int) R.id.action_autoLoginFragment_to_dashBoardFragment);
                    }
                }
            });
        }
        Button button = new Button(getActivity());
        button.setText("Log in with another account");
        layout.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(AutoLoginFragment.this).navigate((int) R.id.action_autoLoginFragment_to_logInFragment);
            }
        });
    }
}