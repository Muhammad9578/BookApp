package com.mtechsoft.bookapp.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mtechsoft.bookapp.Interfaces.NetworkListener;
import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.activities.MainActivity;
import com.mtechsoft.bookapp.adapters.HistoryAdapter;
import com.mtechsoft.bookapp.models.Chapter;
import com.mtechsoft.bookapp.models.History;
import com.mtechsoft.bookapp.models.Parameter;
import com.mtechsoft.bookapp.utils.NetworkTask;
import com.mtechsoft.bookapp.utils.SharedPref;
import com.mtechsoft.bookapp.utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends BaseFragment implements HistoryAdapter.Callback, NetworkListener {
    private RecyclerView recyclerView;
    private HistoryAdapter mAdapter;
    private ArrayList<History> histories;
    public static String quizType;
    public static String chapterId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        init(v);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        getHistoryWithServer();
    }

    private void init(View v) {
        recyclerView = v.findViewById(R.id.rvHistory);

    }
    private void getHistoryWithServer(){

        ArrayList<Parameter> params = new ArrayList<>();
        params.add(new Parameter("user_id",  SharedPref.getUser(getActivity()).getId()));
        NetworkTask loginTask = new NetworkTask(getContext(), "GET", WebServices.ATTEMPTED_CHAPTERS, params);
        loginTask.setListener(this);
        loginTask.setMessage("Fetching History ....");
        loginTask.execute();
    }


    private void initAdapter() {

        histories = new ArrayList<>();
        mAdapter = new HistoryAdapter(getActivity(), histories, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private List<History> parseJsonResponse(JSONArray jsonArray) {
        try {
            List<History> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {

                String quizTypea = jsonArray.getJSONObject(i).getString("con_type");
                String id= jsonArray.getJSONObject(i).getString("id");
                String ch_name= jsonArray.getJSONObject(i).getString("chapter_name");
//                JSONObject obj = jsonArray.getJSONObject(i).getJSONObject("data");
                History history = new History();
                history.setId(Integer.parseInt(id));
                history.setType(quizTypea);
                history.setChapterName(ch_name);
                list.add(history);
            }
            return list;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    @Override
    public void onItemClick(int pos) {
        ((MainActivity) getActivity()).navController.navigate(R.id.action_historyFragment_to_detailSheetFragment);
        DetailSheetFragment.chapterId = String.valueOf(histories.get(pos).getId());
        quizType = String.valueOf(histories.get(pos).getType());
    }

    @Override
    public void onSuccess(String result) {
        try {

            JSONObject jObj = new JSONObject(result);
            histories.clear();
            histories.addAll(parseJsonResponse(jObj.getJSONArray("data")));
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

}

