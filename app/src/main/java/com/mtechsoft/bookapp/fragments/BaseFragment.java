package com.mtechsoft.bookapp.fragments;

import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mtechsoft.bookapp.R;

public class BaseFragment extends Fragment {

    protected void replaceFragment(Fragment fragment, boolean addToBackstack, boolean animatepop) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = fragmentManager.beginTransaction();
        if (addToBackstack) trans.addToBackStack(null);
        if (animatepop) {
            trans.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_right);
        } else {
            trans.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        }
        trans.replace(R.id.fragment_container, fragment).commit();

    }

    protected void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
