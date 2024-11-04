package com.mtechsoft.bookapp.activities;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mtechsoft.bookapp.R;

public class LandingActivity extends BaseActivity {
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        initNavigation();
    }

    private void initNavigation() {
        navController = Navigation.findNavController(this,R.id.nav_host_fragment);
    }

}