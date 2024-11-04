package com.mtechsoft.bookapp.fragments.login;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.mtechsoft.bookapp.Interfaces.NetworkListener;
import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.activities.MainActivity;
import com.mtechsoft.bookapp.fragments.BaseFragment;
import com.mtechsoft.bookapp.models.Parameter;
import com.mtechsoft.bookapp.models.User;
import com.mtechsoft.bookapp.utils.NetworkTask;
import com.mtechsoft.bookapp.utils.SharedPref;
import com.mtechsoft.bookapp.utils.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener, NetworkListener {
    private Button blogin, bCreateAccount, bForgot;
    private EditText etEmail, etPassword;
    private ProgressBar loadingProgressBar;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.login_fragment, container, false);
        initViews(view);


        return view;
    }

    private void initViews(View view) {
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        blogin = view.findViewById(R.id.bLogin);
        bCreateAccount = view.findViewById(R.id.bCreateAccount);
        bForgot = view.findViewById(R.id.bForgot);
        loadingProgressBar = view.findViewById(R.id.loading);
        blogin.setOnClickListener(this);
        bCreateAccount.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_signupFragment));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLogin:
                onPressLogin();
                break;
            case R.id.bForgot:
                break;
        }

    }

    private void onPressLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (isDataValid(email, password)) {
            loginWithServer(email, password);
        }
    }

    private void loginWithServer(String email, String password) {

        ArrayList<Parameter> params = new ArrayList<>();
        params.add(new Parameter("email", email));
        params.add(new Parameter("password", password));

        NetworkTask loginTask = new NetworkTask(getContext(), "POST", WebServices.LOGIN, params);
        loginTask.setListener(this);
        loginTask.setMessage("Please Wait ....");
        loginTask.execute();
    }


    private void moveToHome() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }


    private boolean isDataValid(String username, String password) {
        if (!isUserNameValid(username)) {
            etEmail.setError(getString(R.string.invalid_username));
            return false;
        } else if (!isPasswordValid(password)) {
            etPassword.setError(getString(R.string.invalid_password));
            return false;
        }
        return true;
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null || username.trim().isEmpty())
            return false;
        else
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    @Override
    public void onSuccess(String result) {
        try {
            JSONObject jobj = new JSONObject(result);
            JSONObject objUser = jobj.getJSONObject("data").getJSONObject("user");
            String msg = jobj.getString("message");
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            User user = new User();
            user.setId(objUser.getString("id"));
            user.setName(objUser.getString("name"));
            SharedPref.saveUser(user, getActivity());
            moveToHome();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onError(String error) {
        try {
            JSONObject jobj = new JSONObject(error);
            String msg = jobj.getString("message");
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

}
