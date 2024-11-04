package com.mtechsoft.bookapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.models.Chapter;
import com.mtechsoft.bookapp.models.History;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<History> histories;
    private Callback callback;

    public HistoryAdapter(Context context, ArrayList<History> histories, Callback callback) {
        this.context = context;
        this.callback = callback;
        this.histories = histories;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_history, parent, false);
        return new BookViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BookViewHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        TextView tvChapterResult;
        LinearLayout llITem;

        private BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChapterResult = itemView.findViewById(R.id.tvChapresult);
            llITem = itemView.findViewById(R.id.llItem);

        }

        private void bind(int pos) {
            History history = histories.get(pos);
            tvChapterResult.setText(history.getChapterName());
            initClickListener();
        }

        private void initClickListener() {

            llITem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface Callback {
        void onItemClick(int pos);
    }
}
