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
import com.mtechsoft.bookapp.models.Chapter;
import com.mtechsoft.bookapp.models.Parameter;
import com.mtechsoft.bookapp.utils.NetworkTask;
import com.mtechsoft.bookapp.utils.SharedPref;
import com.mtechsoft.bookapp.utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements ChaptersAdapter.Callback, NetworkListener {
    private RecyclerView recyclerView;
    private ChaptersAdapter mAdapter;
    private ArrayList<Chapter> chapters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        init(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        getChaptersWithServer();
    }

    private void init(View v) {
        recyclerView = v.findViewById(R.id.rvChapters);

    }

    private void getChaptersWithServer() {

        ArrayList<Parameter> params = new ArrayList<>();
        params.add(new Parameter("user_id", SharedPref.getUser(getContext()).getId()));
        params.add(new Parameter("con_type", MainFragment.butn));
        NetworkTask loginTask = new NetworkTask(getContext(), "GET", WebServices.GET_CHAPTERS, params);
        loginTask.setListener(this);
        loginTask.setMessage("Fetching Data ....");
        loginTask.execute();
    }


    private void initAdapter() {
        chapters = new ArrayList<>();
        mAdapter = new ChaptersAdapter(getActivity(), chapters, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private List<Chapter> parseJsonResponse(JSONArray jsonArray) {
        try {
            List<Chapter> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Chapter chapter = new Chapter();
                chapter.setId(obj.getInt("id"));
                chapter.setChapterName(obj.getString("chapter_name"));
                chapter.setStatus(obj.getString("status"));
                chapter.setType(obj.getString("type"));
                list.add(chapter);
            }
            return list;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void onItemClick(int pos) {
        if (chapters.get(pos).getStatus().equals("locked")) {
            Toast.makeText(getContext(), "Chapter is locked", Toast.LENGTH_SHORT).show();
            return;
        }if (chapters.get(pos).getStatus().equals("locked")) {
            Toast.makeText(getContext(), "Chapter is locked", Toast.LENGTH_SHORT).show();
            return;
        }
        QuizFragment.chapterId = String.valueOf(chapters.get(pos).getId());
        ChapterDetailFragment.chapter_name = String.valueOf(chapters.get(pos).getChapterName());
        ((MainActivity) getActivity()).navController.navigate(R.id.action_homeFragment_to_chapterDetailFragment);

    }

    @Override
    public void onSuccess(String result) {
        try {
            JSONObject jObj = new JSONObject(result);
            chapters.clear();
//            if (parseJsonResponse(jObj.getJSONArray("data")) != null)
                chapters.addAll(parseJsonResponse(jObj.getJSONArray("data")));
            mAdapter.notifyDataSetChanged();


        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

    }
}
