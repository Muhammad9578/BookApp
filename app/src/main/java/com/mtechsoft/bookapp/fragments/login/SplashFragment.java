package com.mtechsoft.bookapp.fragments.login;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.navigation.Navigation;

import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.fragments.BaseFragment;

public class SplashFragment extends BaseFragment {
    private RelativeLayout rl;
    public SplashFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  setFullScreen();
        View view = inflater.inflate(R.layout.activity_splash, container, false);
        initViews(view);
        updateUi();
        return view;
    }

    private void initViews(View view) {
        Button bLogin = view.findViewById(R.id.btn_signin);
        Button bSignup = view.findViewById(R.id.btn_signup);
        rl = view.findViewById(R.id.vBtns);
        bLogin.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_splashFragment_to_loginFragment));
        bSignup.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_splashFragment_to_signupFragment));
    }

    private void updateUi() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showButtons();
            }
        }, 2000);
    }

    private void setFullScreen() {

        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    private void showButtons() {
        rl.setVisibility(View.VISIBLE);
        Animation slideUpAnimation = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_up_animation);
        rl.startAnimation(slideUpAnimation);
    }

}

