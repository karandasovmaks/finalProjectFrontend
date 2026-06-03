package com.example.finalaapplication.ui.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalaapplication.R;
import com.example.finalaapplication.ui.view_part_example.Test_example;

import java.util.List;

public class Popular_tests_Adapter extends RecyclerView.Adapter<Popular_tests_Adapter.TestsHolder> {

    public interface OnTestClickListener {
        void onTestClick(Test_example test);
    }

    public interface OnAssignTestListener {
        void onAssignTest(String testId);
    }

    private Context context;
    List<Test_example> listOfTests;
    private String role;
    private String userId;
    private OnTestClickListener listener;
    private OnAssignTestListener assignListener;

    public Popular_tests_Adapter(Context context, List<Test_example> listOfTests, String role, String userId, OnTestClickListener listener, OnAssignTestListener assignListener) {
        this.context = context;
        this.listOfTests = listOfTests;
        this.role = role;
        this.userId = userId;
        this.listener = listener;
        this.assignListener = assignListener;
    }

    @NonNull
    @Override
    public TestsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_layout, parent, false);
        return new TestsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestsHolder holder, int position) {
        Test_example test = listOfTests.get(position);
        holder.tv_name.setText(test.getTest_name());
        holder.tv_desc.setText(test.getTest_description());
        holder.im.setImageResource(test.getImage_link());

        holder.buttonTry.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTestClick(test);
            }
        });

        if ("TEACHER".equals(role)) {
            holder.buttonAssign.setVisibility(View.VISIBLE);
            holder.buttonAssign.setOnClickListener(v -> {
                if (assignListener != null) {
                    assignListener.onAssignTest(test.getTest_id());
                }
            });
        } else {
            holder.buttonAssign.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listOfTests.size();
    }

    public static class TestsHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        private TextView tv_desc;
        private ImageView im;
        private androidx.appcompat.widget.AppCompatButton buttonTry;
        private androidx.appcompat.widget.AppCompatButton buttonAssign;
        public TestsHolder(@NonNull View itemView){
            super(itemView);
            tv_name = itemView.findViewById(R.id.test_name);
            tv_desc = itemView.findViewById(R.id.description);
            im = itemView.findViewById(R.id.imageView);
            buttonTry = itemView.findViewById(R.id.button_try);
            buttonAssign = itemView.findViewById(R.id.button_assign);
        }
    }
}
