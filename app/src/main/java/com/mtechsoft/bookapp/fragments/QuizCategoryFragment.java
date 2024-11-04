package com.mtechsoft.bookapp.fragments;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.mtechsoft.bookapp.activities.MainActivity;
import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.models.MCQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizCategoryFragment extends BaseFragment implements View.OnClickListener {
    TextView tvEasyQuiz, tvAverageQuiz, tvDifficultQuiz, tvRandomQuiz;
    String chapName;
    private ArrayList<MCQ> mcqsList = new ArrayList<>();

    public QuizCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz_category, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            chapName = args.getString("chapName");
        }
    }

    private void initViews(View view) {
        tvEasyQuiz = view.findViewById(R.id.tvEasyQuiz);
        tvAverageQuiz = view.findViewById(R.id.tvAverageQuiz);
        tvDifficultQuiz = view.findViewById(R.id.tvDifficultQuiz);
        tvRandomQuiz = view.findViewById(R.id.tvRandomQuiz);

        tvEasyQuiz.setOnClickListener(this);
        tvAverageQuiz.setOnClickListener(this);
        tvDifficultQuiz.setOnClickListener(this);
        tvRandomQuiz.setOnClickListener(this);
        showAnimation();
    }

    private void showAnimation() {
        Animation slideLeftToRightAnimation = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_left_to_right_animation);
        tvEasyQuiz.setVisibility(View.VISIBLE);
        tvAverageQuiz.setVisibility(View.VISIBLE);
        tvDifficultQuiz.setVisibility(View.VISIBLE);
        tvRandomQuiz.setVisibility(View.VISIBLE);

        tvEasyQuiz.startAnimation(slideLeftToRightAnimation);
        tvAverageQuiz.startAnimation(slideLeftToRightAnimation);
        tvDifficultQuiz.startAnimation(slideLeftToRightAnimation);
        tvRandomQuiz.startAnimation(slideLeftToRightAnimation);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void moveToQuizFragment() {
        ((MainActivity) Objects.requireNonNull(getActivity())).navController.navigate(R.id.action_quizCategoryFragment_to_quizFragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvEasyQuiz:
                QuizFragment.quizType = "easy";
                break;
            case R.id.tvAverageQuiz:
                QuizFragment.quizType = "average";
                break;
            case R.id.tvDifficultQuiz:
                QuizFragment.quizType = "difficult";
                break;
            case R.id.tvRandomQuiz:
                QuizFragment.quizType = "random";
                break;
        }
        moveToQuizFragment();
    }
}
