package com.mtechsoft.bookapp.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.mtechsoft.bookapp.Interfaces.NetworkListener;
import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.models.Attempt;
import com.mtechsoft.bookapp.models.Parameter;
import com.mtechsoft.bookapp.utils.NetworkTask;
import com.mtechsoft.bookapp.utils.SharedPref;
import com.mtechsoft.bookapp.utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailSheetFragment extends BaseFragment implements NetworkListener {
    private TextView tvAttemptOne, tvAttemptTwo, tvAttemptThree;
    private Button bAnsSheet;
    private ArrayList<Attempt> resultSheets;
    public static String chapterId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_sheet, container, false);
        init(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showResultWithServer();
        // initAdapter();
    }

    private void init(View v) {
        resultSheets = new ArrayList<>();
        bAnsSheet = v.findViewById(R.id.bAnsSheet);
        tvAttemptOne = v.findViewById(R.id.tvAttemp1);
        tvAttemptTwo = v.findViewById(R.id.tvAttemp2);
        tvAttemptThree = v.findViewById(R.id.tvAttemp3);
        bAnsSheet.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_detailSheetFragment_to_answerSheetFragment));
    }

    private void showResultWithServer() {

        ArrayList<Parameter> params = new ArrayList<>();
        params.add(new Parameter("chapter_id", chapterId));
        params.add(new Parameter("user_id", SharedPref.getUser(getContext()).getId()));

        NetworkTask loginTask = new NetworkTask(getContext(), "POST", WebServices.GET_ATTEMPTS_RESULTS, params);
        loginTask.setListener(this);
        loginTask.setMessage("Please Wait ....");
        loginTask.execute();
    }

    private void parseJsonResponse(JSONArray jsonArray) {
        boolean isAllAttempted = false;
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                Attempt attempt = new Attempt();
                JSONObject obj = jsonArray.getJSONObject(i);
                attempt.setId(obj.getString("id"));
                attempt.setChapterId(obj.getString("chapter_id"));
                attempt.setTotalQuestions(obj.getString("total_questions"));
                attempt.setAttempted(obj.getString("attempted"));
                attempt.setUnAttempted(obj.getString("unattempted"));
                attempt.setCorrect(obj.getString("true"));
                attempt.setIncorrect(obj.getString("false"));
                attempt.setPercentage(obj.getString("percentage"));
                if (i == 0) tvAttemptOne.setText(attempt.getTitle());
                else if (i == 1) tvAttemptTwo.setText(attempt.getTitle());
                else if (i == 2) tvAttemptThree.setText(attempt.getTitle());
                if (attempt.getUnAttempted().equals("0"))
                    isAllAttempted = true;

                resultSheets.add(attempt);
            }
            if (isAllAttempted)
                bAnsSheet.setVisibility(View.VISIBLE);
            else
                bAnsSheet.setVisibility(View.INVISIBLE);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String result) {
        try {
            JSONObject jobj = new JSONObject(result);
            parseJsonResponse(jobj.getJSONArray("data"));
            String msg = jobj.getString("message");
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
