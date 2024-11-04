package com.mtechsoft.bookapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment {
    public MainFragment() {
        // Required empty public constructor
    }

    public static String butn = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mian, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        TextView tvLessons = view.findViewById(R.id.tvbooks);
        TextView tvPlays = view.findViewById(R.id.tvPlays);
        TextView tvPoems = view.findViewById(R.id.tvpoems);


        tvLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                butn = "lesson";
                ((MainActivity) getActivity()).navController.navigate(R.id.action_nav_dashboard_to_homeFragment);
            }
        });
        tvPlays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                butn = "play";
                ((MainActivity) getActivity()).navController.navigate(R.id.action_nav_dashboard_to_homeFragment);
            }
        });
        tvPoems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                butn = "poem";
                ((MainActivity) getActivity()).navController.navigate(R.id.action_nav_dashboard_to_homeFragment);
            }
        });

//        Navigation.createNavigateOnClickListener(R.id.action_nav_dashboard_to_homeFragment));
//        tvPlays.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_dashboard_to_playsFragment));
//        tvPoems.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_dashboard_to_poemNameFragment));
    }
}
