package com.example.finalaapplication.ui.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalaapplication.R;
import com.example.finalaapplication.ui.view_part_example.Task;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TaskHolder> {

    private Context context;
    private Fragment fragment;

    List<Task> listOfTasks;

    private int score = 0;


    public TestAdapter(Context context, Fragment fragment, List<Task> listOfTasks) {
        this.context = context;
        this.fragment = fragment;
        this.listOfTasks = listOfTasks;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_background, parent, false);
        return new TestAdapter.TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        holder.tv.setText(listOfTasks.get(position).getTaskText());
        holder.appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(holder.materialAutoCompleteTextView.getText()).equals(listOfTasks.get(position).getAnser()) && !listOfTasks.get(position).isAnered()){
                    score += 1;
                    listOfTasks.get(position).setAnered(true);
                }
                holder.appCompatButton.setBackgroundResource(R.drawable.anserddrowable);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfTasks.size();
    }

    public int getScore() {
        return score;
    }

    public static class TaskHolder extends RecyclerView.ViewHolder{
        private TextView tv;
        
        private MaterialAutoCompleteTextView materialAutoCompleteTextView;

        private AppCompatButton appCompatButton;
        
        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            
            tv = itemView.findViewById(R.id.textView4);

            materialAutoCompleteTextView = itemView.findViewById(R.id.materialAutoCompleteTextView);

            appCompatButton = itemView.findViewById(R.id.buttonAnser);

        }
    }
}
