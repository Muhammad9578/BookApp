package com.mtechsoft.bookapp.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mtechsoft.bookapp.R;
import com.mtechsoft.bookapp.models.MCQ;

import java.util.ArrayList;

public class QuizAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<MCQ> mcqsList;
    Callback callback;

    public QuizAdapter(Context context, Callback callback, ArrayList<MCQ> mcqsList) {
        this.context = context;
        this.callback = callback;
        this.mcqsList = mcqsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_quiz, parent, false);
        return new BookViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        BookViewHolder holder1 = (BookViewHolder) holder;
        holder1.bind(position);
       // holder1.radioGroup.check(position);
    }

    @Override
    public int getItemCount() {
        return mcqsList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion;
        RadioButton op1;
        RadioButton op2;
        RadioButton op3;
        RadioButton op4;
        RadioGroup radioGroup;

        private BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQues);
            op1 = itemView.findViewById(R.id.option1);
            op2 = itemView.findViewById(R.id.option2);
            op3 = itemView.findViewById(R.id.option3);
            op4 = itemView.findViewById(R.id.option4);
            radioGroup = itemView.findViewById(R.id.rgOptions);

        }

        private void bind(int pos) {

            tvQuestion.setText(Html.fromHtml(mcqsList.get(pos).getQues()));
            op1.setText(mcqsList.get(pos).getOp1());
            op2.setText(mcqsList.get(pos).getOp2());
            op3.setText(mcqsList.get(pos).getOp3());
            op4.setText(mcqsList.get(pos).getOp4());
            initClickListener();
        }

        private void initClickListener() {
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    callback.onItemClick(getAdapterPosition(), radioGroup, checkedId);
                    Log.d("ppp",""+getAdapterPosition());
                }
            });
        }
    }

    public interface Callback {
        void onItemClick(int pos, RadioGroup radioGroup, int checkedId);
    }

    public int getItemViewType(int position) {
        return position;
    }
}
