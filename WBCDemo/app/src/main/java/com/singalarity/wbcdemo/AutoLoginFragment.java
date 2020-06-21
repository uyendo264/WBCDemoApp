package com.singalarity.wbcdemo;


import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import java.util.List;

public class AutoLoginFragment extends Fragment {
    private SharedViewModel viewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto_login, container, false);
    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        viewModel.getLoginStatus().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            public void onChanged(Boolean s) {
                if (s == false) {
                    // Chua login => show list user
                    Log.d("AutoLogin", "Login Status:" + s);

                    final DatabaseHandler db = new DatabaseHandler(getActivity());
                    int numberUsers = db.getNumberUsers();
                    final List<UserInfo_extend> list = db.getAllUserInfo();
                    Log.d("List", "list size = " + list.size());
                    int count = list.size();

                    ScrollView scrollView = view.findViewById(R.id.scrollView);


                    final LinearLayout layout = (LinearLayout) view.findViewById(R.id.linearLayout);

                    final Button[] buttons = new Button[count];


                    for (int i = 0; i < count; i++) {
                        final int j = i;
                        final String username = list.get(j).getUid();
                        buttons[i] = new Button(getActivity());
                        buttons[i].setId(i);
                        buttons[i].setText(username);
                        layout.addView(buttons[i]);

                        Log.d("Welcome Frament", "addview buttons");
                        //registerForContextMenu(buttons[i]);
                        buttons[i].setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                RequestAPI requestAPI = new RequestAPI();
                                Log.d("Welcome Frament", "init request API");
                                String encrypted = ((UserInfo_extend) list.get(j)).getHashPassword();
                                String username = ((UserInfo_extend) list.get(j)).getUid();
                                Log.d("Welcome Frament", "encrypted = " + encrypted);
                                String deviceID = ((MainActivity) AutoLoginFragment.this.getActivity()).getDeviceID();
                                Log.d("Welcome Frament", "deviceID = " + deviceID);
                                String respone = requestAPI.sendLoginData(deviceID, encrypted);
                                Log.d("Login", "response: " + respone);
                                if (respone.equals("200")) {
                                    viewModel.setLoginStatus(true);
                                    viewModel.setUserName(username);
                                    NavHostFragment.findNavController(AutoLoginFragment.this).navigate((int) R.id.dashBoardFragment);

                                }
                            }
                        });
                        buttons[i].setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                            @Override
                            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                                AutoLoginFragment.super.onCreateContextMenu(menu, v, menuInfo);
                                MenuInflater inflater = getActivity().getMenuInflater();
                                inflater.inflate(R.menu.option_menu, menu);
                                MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.delete:
                                                Toast.makeText(getContext(), "Bookmark", Toast.LENGTH_SHORT).show();
                                                db.deleteUsers(username);
                                                layout.removeView(buttons[j]);
                                                //reload
                                                Log.d("AutoLogin", "delete user");
                                                return true;
                                            default:
                                                return AutoLoginFragment.super.onContextItemSelected(item);
                                        }
                                    }
                                };
                                for (int i = 0, n = menu.size(); i < n; i++) {
                                    menu.getItem(i).setOnMenuItemClickListener(listener);
                                }
                            }

                        });


                        view.findViewById(R.id.loginAnotherAcctount_textView).setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            public void onClick(View view) {
                                NavHostFragment.findNavController(AutoLoginFragment.this).navigate((int) R.id.action_autoLoginFragment_to_logInFragment);
                            }
                        });
                    }

                } else {// da login => chuyen den dashboard frament
                    Log.d("AutoLogin", "Login Status:" + s);
                    NavHostFragment.findNavController(AutoLoginFragment.this).navigate((int) R.id.action_autoLoginFragment_to_dashBoardFragment);
                }
            }
        });


    }


    private void deleteUsername(String uid) {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        db.deleteUsers(uid);
    }
}