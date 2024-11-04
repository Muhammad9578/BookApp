package com.mtechsoft.bookapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.models.AnsSheet;

import java.util.ArrayList;

public class AnsSheetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<AnsSheet> ansSheets;

    public AnsSheetAdapter(Context context, ArrayList<AnsSheet> ansSheets) {
        this.context = context;
        this.ansSheets = ansSheets;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.row_answer_sheet, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BookViewHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        Log.d("ADAPTER", "size is " + ansSheets.size());
        return ansSheets.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuest, tvCorrectAns;
        LinearLayout llITem;

        private BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuest = itemView.findViewById(R.id.tvQuestiom);
            tvCorrectAns = itemView.findViewById(R.id.tvCorrectAns);
            llITem = itemView.findViewById(R.id.llItem);
        }

        private void bind(int pos) {
            AnsSheet ansSheet = ansSheets.get(pos);
            tvQuest.setText(ansSheet.getQuestion());
            tvCorrectAns.setText(ansSheet.getCorrectOption());
        }
    }

}
