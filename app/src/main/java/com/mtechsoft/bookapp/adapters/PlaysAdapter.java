package com.mtechsoft.bookapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.models.Chapter;
import com.mtechsoft.bookapp.models.Plays;

import java.util.ArrayList;

public class PlaysAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Plays> plays;
    private Callback callback;

    public PlaysAdapter(Context context, ArrayList<Plays> plays, Callback callback) {
        this.context = context;
        this.callback = callback;
        this.plays = plays;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_plays, parent, false);
        return new BookViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BookViewHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return plays.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        TextView tvPlaysName;
        ImageView ivLock;
        LinearLayout llITem;

        private BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlaysName = itemView.findViewById(R.id.tvPlaysName);
            ivLock = itemView.findViewById(R.id.ivLock);
            llITem = itemView.findViewById(R.id.llItem);

        }

        private void bind(int pos) {
            Plays play = plays.get(pos);
            tvPlaysName.setText(play.getPlayName());
            updateBackground(play.getStatus(), llITem, ivLock);
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

    private void updateBackground(String status, LinearLayout llITem, ImageView ivLock) {
//        if (status.equals("locked")) {
//            llITem.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
//            ivLock.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_lock_black_24dp));
//        } else if (status.equals("unlocked")) {
//            llITem.setBackgroundColor(context.getResources().getColor(R.color.colorYellow));
//            ivLock.setImageDrawable(context.getResources().getDrawable(R.drawable.unlock));
//        }else if (status.equals("unlocked")) {
//            llITem.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
//            ivLock.setImageDrawable(context.getResources().getDrawable(R.drawable.unlock));
//        }
    }

    public interface Callback {
        void onItemClick(int pos);
    }
}
