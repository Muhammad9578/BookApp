package com.mtechsoft.bookapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.models.Attempt;

import java.util.ArrayList;

public class DetailResultSheetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Attempt> attempts;
    private Callback callback;

    public DetailResultSheetAdapter(Context context, ArrayList<Attempt> attempts, Callback callback) {
        this.context = context;
        this.callback = callback;
        this.attempts = attempts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_detail_result_sheet, parent, false);
        return new BookViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        BookViewHolder holder1 = (BookViewHolder) holder;
        holder1.bind(position);
    }

    @Override
    public int getItemCount() {
        return attempts.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestionNo;
        // LinearLayout llITem;

        private BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNo = itemView.findViewById(R.id.tvQuest);
            //  llITem = itemView.findViewById(R.id.llItem);

        }

        private void bind(int pos) {
            Attempt attempt = attempts.get(pos);
//            tvQuestionNo.setText(resultSheet.getQuestionNo());
            //    initClickListener();
        }

//        private void initClickListener() {
//
//            llITem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    callback.onItemClick(getAdapterPosition());
//                }
//            });
//        }
    }

    public interface Callback {
        void onItemClick(int pos);
    }
}
