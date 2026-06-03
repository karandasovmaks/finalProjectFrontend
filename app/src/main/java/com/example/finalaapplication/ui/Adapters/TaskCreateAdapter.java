package com.example.finalaapplication.ui.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalaapplication.R;

import java.util.List;

public class TaskCreateAdapter extends RecyclerView.Adapter<TaskCreateAdapter.TaskCreateHolder> {

    private List<TaskCreateItem> items;

    public TaskCreateAdapter(List<TaskCreateItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public TaskCreateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_create_item_layout, parent, false);
        return new TaskCreateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskCreateHolder holder, int position) {
        TaskCreateItem item = items.get(position);
        holder.etQuestion.setText(item.getQuestion());
        holder.etAnswer.setText(item.getAnswer());

        holder.etQuestion.setHint("Вопрос " + (position + 1));
        holder.etAnswer.setHint("Ответ " + (position + 1));

        holder.etQuestion.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) item.setQuestion(holder.etQuestion.getText().toString().trim());
        });
        holder.etAnswer.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) item.setAnswer(holder.etAnswer.getText().toString().trim());
        });

        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                items.remove(pos);
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class TaskCreateHolder extends RecyclerView.ViewHolder {
        private EditText etQuestion, etAnswer;
        private AppCompatButton btnDelete;

        public TaskCreateHolder(@NonNull View itemView) {
            super(itemView);
            etQuestion = itemView.findViewById(R.id.task_create_question);
            etAnswer = itemView.findViewById(R.id.task_create_answer);
            btnDelete = itemView.findViewById(R.id.task_create_delete);
        }
    }

    public static class TaskCreateItem {
        private String question;
        private String answer;

        public TaskCreateItem() {
            this.question = "";
            this.answer = "";
        }

        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }
        public String getAnswer() { return answer; }
        public void setAnswer(String answer) { this.answer = answer; }
    }
}
