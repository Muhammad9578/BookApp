package com.mtechsoft.bookapp.fragments.login;


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

import com.mtechsoft.bookapp.Interfaces.NetworkListener;
import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.activities.LandingActivity;
import com.mtechsoft.bookapp.fragments.BaseFragment;
import com.mtechsoft.bookapp.models.Parameter;
import com.mtechsoft.bookapp.utils.NetworkTask;
import com.mtechsoft.bookapp.utils.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends BaseFragment implements View.OnClickListener, NetworkListener {
    private Button bSignup;
    private EditText etFirstName, etLastName, etEmail, etPass, etConfirmPass;
    private ProgressBar loadingProgressBar;

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        loadingProgressBar = view.findViewById(R.id.loading);
        bSignup = view.findViewById(R.id.bSignup);
        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_last_name);
        etEmail = view.findViewById(R.id.et_email);
        etPass = view.findViewById(R.id.et_pass);
        etConfirmPass = view.findViewById(R.id.et_confirm_pass);
        bSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onPressSignup();

    }


    private void onPressSignup() {
        String username = etEmail.getText().toString().trim();
        String password = etPass.getText().toString().trim();
        String confirmPassword = etConfirmPass.getText().toString().trim();
        String name = etFirstName.getText().toString().trim() + " " + etLastName.getText().toString().trim();
        if (isValidData(username, password, confirmPassword)) {
            registerWithServer(name, username, password, confirmPassword);
        }
    }

    private boolean isValidData(String username, String password, String confirmPassword) {
        if (!isUserNameValid(username)) {
            etEmail.setError(getString(R.string.invalid_username));
            return false;
        } else if (!isPasswordValid(password)) {
            etPass.setError(getString(R.string.invalid_password));
            return false;
        } else if (!isPasswordMatch(password, confirmPassword)) {
            etPass.setError(getString(R.string.mismatch_password));
            etConfirmPass.setError(getString(R.string.mismatch_password));
            return false;
        }
        return true;
    }

    private boolean isUserNameValid(String username) {
        if (username == null || username.trim().isEmpty())
            return false;
        else
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isPasswordMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }


    private void registerWithServer(String name, String email, String password, String confirmPassword) {

        ArrayList<Parameter> params = new ArrayList<>();
        params.add(new Parameter("name", name));
        params.add(new Parameter("email", email));
        params.add(new Parameter("password", password));
        params.add(new Parameter("password_confirmation", confirmPassword));

        NetworkTask loginTask = new NetworkTask(getContext(), "POST", WebServices.REGISTER, params);
        loginTask.setListener(this);
        loginTask.setMessage("Please Wait ....");
        loginTask.execute();
    }

    @Override
    public void onSuccess(String result) {
        try {
            JSONObject jobj = new JSONObject(result);
            String msg = jobj.getString("message");
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            LoginFragment loginFragment = new LoginFragment();
            replaceFragment(loginFragment, false, false);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {


    }
}
