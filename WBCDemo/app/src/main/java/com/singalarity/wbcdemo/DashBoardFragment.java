package com.singalarity.wbcdemo;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
                NavHostFragment.findNavController(DashBoardFragment.this).navigate((int) R.id.action_dashBoardFragment_to_autoLoginFragment);
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
}