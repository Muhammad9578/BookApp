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
import com.mtechsoft.bookapp.adapters.PoemAdapter;
import com.mtechsoft.bookapp.models.Parameter;
import com.mtechsoft.bookapp.models.Poem;
import com.mtechsoft.bookapp.utils.NetworkTask;
import com.mtechsoft.bookapp.utils.SharedPref;
import com.mtechsoft.bookapp.utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PoemNameFragment extends BaseFragment implements PoemAdapter.Callback, NetworkListener {
    private RecyclerView recyclerView;
    private PoemAdapter mAdapter;
    private ArrayList<Poem> poems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_poem_name, container, false);

        init(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        getPoemsWithServer();
    }

    private void init(View v) {
        recyclerView = v.findViewById(R.id.rv_poemName);

    }

    private void getPoemsWithServer() {

        ArrayList<Parameter> params = new ArrayList<>();
        params.add(new Parameter("user_id", SharedPref.getUser(getContext()).getId()));
        NetworkTask loginTask = new NetworkTask(getContext(), "GET", WebServices.GET_POEMS, params);
        loginTask.setListener(this);
        loginTask.setMessage("Fetching Poems ....");
        loginTask.execute();
    }


    private void initAdapter() {
        poems = new ArrayList<>();
        mAdapter = new PoemAdapter(getActivity(), poems, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private List<Poem> parseJsonResponse(JSONArray jsonArray) {
        try {
            List<Poem> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Poem poem = new Poem();
                poem.setId(obj.getInt("id"));
                poem.setPoemName(obj.getString("poem_name"));
                poem.setStatus(obj.getString("status"));
                list.add(poem);
            }
            return list;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void onItemClick(int pos) {
        if (poems.get(pos).getStatus().equals("locked")) {
            Toast.makeText(getContext(), "Poem is locked", Toast.LENGTH_SHORT).show();
            return;
        }
        QuizFragment.chapterId = String.valueOf(poems.get(pos).getId());
        ((MainActivity) getActivity()).navController.navigate(R.id.action_poemNameFragment_to_chapterDetailFragment);

    }

    @Override
    public void onSuccess(String result) {
        try {
            JSONObject jObj = new JSONObject(result);
            poems.clear();
//            if (parseJsonResponse(jObj.getJSONArray("data")) != null)
            poems.addAll(parseJsonResponse(jObj.getJSONArray("data")));
            mAdapter.notifyDataSetChanged();


        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

    }
}
