package com.singalarity.wbcdemo;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

public class DashBoardFragment extends Fragment {
    TextView responseText;
    private SharedViewModel viewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.responseText = (TextView) view.findViewById(R.id.dashboard_textView);
        new DatabaseHandler(getContext());
        view.findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                viewModel.getLoginStatus().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        Log.d("Dashboard", "Login Status:"+aBoolean);
                    }
                });

                NavHostFragment.findNavController(DashBoardFragment.this).navigate((int) R.id.action_dashBoardFragment_to_autoLoginFragment);
                viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
                viewModel.setLoginStatus(false);
                viewModel.getLoginStatus().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        Log.d("Dashboard", "Login Status - Logout:"+aBoolean);
                    }
                });
            }
        });
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedViewModel sharedViewModel = (SharedViewModel) ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        this.viewModel = sharedViewModel;
        sharedViewModel.getUserName().observe(getViewLifecycleOwner(), new Observer<String>() {
            public void onChanged(String s) {
                DashBoardFragment.this.responseText.setText(s);
            }
        });
    }
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    getActivity().finish();
                    return;
                }
                doubleBackToExitPressedOnce = true;
                Toast.makeText(getContext(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

}