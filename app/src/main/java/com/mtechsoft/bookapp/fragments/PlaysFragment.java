package com.mtechsoft.bookapp.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mtechsoft.bookapp.Interfaces.NetworkListener;
import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.activities.MainActivity;
import com.mtechsoft.bookapp.adapters.ChaptersAdapter;
import com.mtechsoft.bookapp.adapters.PlaysAdapter;
import com.mtechsoft.bookapp.models.Chapter;
import com.mtechsoft.bookapp.models.Parameter;
import com.mtechsoft.bookapp.models.Plays;
import com.mtechsoft.bookapp.utils.NetworkTask;
import com.mtechsoft.bookapp.utils.SharedPref;
import com.mtechsoft.bookapp.utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaysFragment extends BaseFragment implements PlaysAdapter.Callback, NetworkListener {
    private RecyclerView recyclerView;
    private PlaysAdapter mAdapter;
    private ArrayList<Plays> plays;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_plays, container, false);

        init(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        getPlaysWithServer();
    }

    private void init(View v) {
        recyclerView = v.findViewById(R.id.rvPlays);

    }

    private void getPlaysWithServer() {

        ArrayList<Parameter> params = new ArrayList<>();
        params.add(new Parameter("user_id", SharedPref.getUser(getContext()).getId()));
        params.add(new Parameter("con_type", "play"));
        NetworkTask loginTask = new NetworkTask(getContext(), "GET", WebServices.GET_CHAPTERS, params);
        loginTask.setListener(this);
        loginTask.setMessage("Fetching Plays ....");
        loginTask.execute();
    }


    private void initAdapter() {
        plays = new ArrayList<>();
        mAdapter = new PlaysAdapter(getActivity(), plays, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private List<Plays> parseJsonResponse(JSONArray jsonArray) {
        try {
            List<Plays> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Plays plays = new Plays();
                plays.setId(obj.getInt("id"));
                plays.setPlayName(obj.getString("play_name"));
                plays.setStatus(obj.getString("status"));
                list.add(plays);
            }
            return list;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void onItemClick(int pos) {
        if (plays.get(pos).getStatus().equals("locked")) {
            Toast.makeText(getContext(), "Chapter is locked", Toast.LENGTH_SHORT).show();
            return;
        }
        QuizFragment.chapterId = String.valueOf(plays.get(pos).getId());
        ((MainActivity) getActivity()).navController.navigate(R.id.action_playsFragment_to_chapterDetailFragment);

    }

    @Override
    public void onSuccess(String result) {
        try {
            JSONObject jObj = new JSONObject(result);
            plays.clear();
//            if (parseJsonResponse(jObj.getJSONArray("data")) != null)
            plays.addAll(parseJsonResponse(jObj.getJSONArray("data")));
            mAdapter.notifyDataSetChanged();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

    }
}
