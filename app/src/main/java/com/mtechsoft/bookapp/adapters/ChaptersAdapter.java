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

import java.util.ArrayList;

public class ChaptersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Chapter> chapters;
    private Callback callback;

    public ChaptersAdapter(Context context, ArrayList<Chapter> chapters, Callback callback) {
        this.context = context;
        this.callback = callback;
        this.chapters = chapters;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chapters_numbers, parent, false);
        return new BookViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BookViewHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        TextView tvChapterName;
        ImageView ivLock;
        LinearLayout llITem;

        private BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChapterName = itemView.findViewById(R.id.tvChapName);
            ivLock = itemView.findViewById(R.id.ivLock);
            llITem = itemView.findViewById(R.id.llItem);

        }

        private void bind(int pos) {
            Chapter chapter = chapters.get(pos);
            tvChapterName.setText(chapter.getChapterName());
            llITem.setBackgroundColor(chapter.getColor());
            updateBackground(chapter.getType(), llITem, ivLock);
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

    private void updateBackground(String type ,LinearLayout llITem, ImageView ivLock) {
        if (type.equals("next")) {
            llITem.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
            ivLock.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_lock_black_24dp));
        } else if (type.equals("current")) {
            llITem.setBackgroundColor(context.getResources().getColor(R.color.colorYellow));
            ivLock.setImageDrawable(context.getResources().getDrawable(R.drawable.unlock));
        }else if (type.equals("completed")) {
            llITem.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
            ivLock.setImageDrawable(context.getResources().getDrawable(R.drawable.unlock));
        }
    }

    public interface Callback {
        void onItemClick(int pos);
    }
}
