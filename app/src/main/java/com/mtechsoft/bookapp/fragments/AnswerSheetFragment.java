package com.mtechsoft.bookapp.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mtechsoft.bookapp.Interfaces.NetworkListener;
import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.adapters.AnsSheetAdapter;
import com.mtechsoft.bookapp.models.AnsSheet;
import com.mtechsoft.bookapp.models.Parameter;
import com.mtechsoft.bookapp.utils.NetworkTask;
import com.mtechsoft.bookapp.utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnswerSheetFragment extends BaseFragment implements NetworkListener {
    private RecyclerView recyclerView;
    private AnsSheetAdapter asAdapter;
    private ArrayList<AnsSheet> answers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_answer_sheet, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rvAnsSheet);
        initAdapter();
        getAnswersFromServer();
    }
    private void getAnswersFromServer() {
        ArrayList<Parameter> params = new ArrayList<>();
        params.add(new Parameter("chapter_id", DetailSheetFragment.chapterId));
        params.add(new Parameter("con_type", HistoryFragment.quizType));

        NetworkTask loginTask = new NetworkTask(getContext(), "GET", WebServices.ANSWER_SHEET, params);
        loginTask.setListener(this);
        loginTask.setMessage("Please Wait ....");
        loginTask.execute();
    }

    private void parseJsonResponse(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                AnsSheet ansSheet = new AnsSheet();
                JSONObject obj = jsonArray.getJSONObject(i);
                ansSheet.setQuestion(obj.getString("question"));
                ansSheet.setCorrectOption(obj.getString("correct"));
                answers.add(ansSheet);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void initAdapter() {
        answers = new ArrayList<>();
        asAdapter = new AnsSheetAdapter(getActivity(), answers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(asAdapter);
    }
    @Override
    public void onSuccess(String result) {
        try {
            JSONObject jobj = new JSONObject(result);
            answers.clear();
            parseJsonResponse(jobj.getJSONArray("data"));
            Log.d("FRAGMENT","size "+answers.size());
            asAdapter.notifyDataSetChanged();
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
