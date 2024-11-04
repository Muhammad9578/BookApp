package com.mtechsoft.bookapp.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.Interfaces.NetworkListener;
import com.mtechsoft.bookapp.activities.MainActivity;
import com.mtechsoft.bookapp.adapters.QuizAdapter;
import com.mtechsoft.bookapp.models.MCQ;
import com.mtechsoft.bookapp.models.Parameter;
import com.mtechsoft.bookapp.models.Result;
import com.mtechsoft.bookapp.utils.NetworkTask;
import com.mtechsoft.bookapp.utils.SharedPref;
import com.mtechsoft.bookapp.utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends BaseFragment implements View.OnClickListener, QuizAdapter.Callback, NetworkListener {
    private Button bSubmit, bAnsSheet;
    private QuizAdapter mAdapter;
    ImageView ivBack;
    private ArrayList<MCQ> mcqsList = new ArrayList<>();
    private ArrayList<Result> results;
    public static String chapterId;
    public static String quizType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);
        Toolbar toolbar = v.findViewById(R.id.quiz_toolbar);
        TextView mTitle = toolbar.findViewById(R.id.tvTitle);
        bSubmit = toolbar.findViewById(R.id.submit);
        ivBack = toolbar.findViewById(R.id.ivBack1);
        bAnsSheet = v.findViewById(R.id.bAnsSheet);
        mTitle.setText("Start Quiz");

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View v) {
        initAdapter(v);
        ivBack.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_quizFragment_to_quizCategoryFragment));
        getQuizFromServer();
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitPress();
            }
        });
    }

    private void initAdapter(View v) {
        mAdapter = new QuizAdapter(getActivity(), this, mcqsList);
        RecyclerView recyclerView = v.findViewById(R.id.rc_quiz);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void getQuizFromServer() {

        ArrayList<Parameter> params = new ArrayList<>();
        params.add(new Parameter("chapter_id", chapterId));
        params.add(new Parameter("type", quizType));
        params.add(new Parameter("con_type", MainFragment.butn));
        NetworkTask loginTask = new NetworkTask(getContext(), "POST", WebServices.GET_QUIZ, params);
        loginTask.setListener(this);
        loginTask.setMessage("Fetching Quiz ....");
        loginTask.execute();
    }

    private void initResult() {
        results = new ArrayList<>();
        for (MCQ mcq : mcqsList) {
            Result result = new Result();
            result.setQuestId(mcq.getId());
            result.setCheckedOption("empty");
            results.add(result);
        }
    }

    private void onSubmitPress() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        Gson gson = gsonBuilder.create();

        String JSONObject = gson.toJson(results);
        Log.d("Converted", JSONObject);

        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = prettyGson.toJson(results);
        Log.d("ConvertedPRe", prettyJson);

        JSONArray jsArray = new JSONArray(mcqsList);

        Log.i("rrr",""+mcqsList);

        ArrayList<Parameter> params = new ArrayList<>();
        params.add(new Parameter("user_id", SharedPref.getUser(getContext()).getId()));
        Log.d("uu", SharedPref.getUser(getContext()).getId());

        params.add(new Parameter("chapter_id", chapterId));
        Log.d("cc", chapterId);

        params.add(new Parameter("type", quizType));
        Log.d("tt", quizType);

        params.add(new Parameter("con_type", MainFragment.butn));
        Log.d("ccn", MainFragment.butn);

        params.add(new Parameter("mcqs", JSONObject));
        Log.d("jjo",""+JSONObject);

        Log.d("QUIZ_AB", jsArray.toString());
        NetworkTask submitQuizTask = new NetworkTask(getContext(), "POST", WebServices.SUBMIT_QUIZ, params);
        submitQuizTask.setMessage("Submitting Result ....");
        submitQuizTask.setListener(new NetworkListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    Log.i("aaa",""+result);
                    showToast(jObj.getString("message"));
                    ((MainActivity) getActivity()).navController.navigate(R.id.action_quizFragment_to_quizCategoryFragment);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });
        submitQuizTask.execute();
    }

    private List<MCQ> parseJsonResponse(JSONArray jsonArray) {
        try {
            List<MCQ> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                MCQ mcq = new MCQ();
                mcq.setId(obj.getInt("id"));
                mcq.setChapterId(obj.getInt("chapter_id"));
                mcq.setQues(obj.getString("question"));
                mcq.setOp1(obj.getString("option1"));
                mcq.setOp2(obj.getString("option2"));
                mcq.setOp3(obj.getString("option3"));
                mcq.setOp4(obj.getString("option4"));

                list.add(mcq);
            }
            return list;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @Override
    public void onItemClick(int pos, RadioGroup radioGroup, int checkedId) {
        pos = 1;
        Log.d("ooo",""+pos);
        switch (checkedId) {
            case R.id.option1:
                results.get(pos).setCheckedOption(mcqsList.get(pos).getOp1());
                break;
            case R.id.option2:
                results.get(pos).setCheckedOption(mcqsList.get(pos).getOp2());
                break;
            case R.id.option3:
                results.get(pos).setCheckedOption(mcqsList.get(pos).getOp3());
                break;
            case R.id.option4:
                results.get(pos).setCheckedOption(mcqsList.get(pos).getOp4());
                break;
            default:

        }
    }

    @Override
    public void onSuccess(String result) {
        try {
            JSONObject jObj = new JSONObject(result);
            mcqsList.clear();
            mcqsList.addAll(parseJsonResponse(jObj.getJSONArray("data")));

            Log.d("zzz",""+parseJsonResponse(jObj.getJSONArray("data")));
            initResult();
            mAdapter.notifyDataSetChanged();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack1:
                getActivity().onBackPressed();
        }

    }
}

