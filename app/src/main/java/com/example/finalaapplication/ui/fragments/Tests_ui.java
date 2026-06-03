package com.example.finalaapplication.ui.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalaapplication.R;
import com.example.finalaapplication.ui.Adapters.TestAdapter;
import com.example.finalaapplication.ui.view_part_example.Task;

import java.util.List;

public class Tests_ui extends Fragment {

    private String testName;
    private String testDesc;
    private String testId;
    private String assignmentId;
    private List<Task> listOfTasks;
    private TextView testNametv;
    private TextView testDesctv;
    private RecyclerView recyclerView;
    private AppCompatButton appCompatButton;

    public Tests_ui() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            testName = getArguments().getString("test_name");
            testDesc = getArguments().getString("test_desc");
            testId = getArguments().getString("test_id");
            assignmentId = getArguments().getString("assignment_id");
            listOfTasks = (List<Task>) getArguments().getSerializable("test_task_list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        recyclerView = view.findViewById(R.id.test_Amount);
        testNametv = view.findViewById(R.id.textView3);
        testDesctv = view.findViewById(R.id.testdesc);
        testDesctv.setText(testDesc);
        testNametv.setText(testName);
        appCompatButton = view.findViewById(R.id.appCompatButton);
        TestAdapter adapter = new TestAdapter(getContext(), this, listOfTasks);
        recyclerView.setAdapter(adapter);
        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                int score = adapter.getScore();
                int total = adapter.getItemCount();
                bundle.putString("score", String.valueOf((int)((double) score / (double) total * 100)));
                bundle.putString("test_id", testId);
                bundle.putString("assignment_id", assignmentId);
                bundle.putInt("score_raw", score);
                bundle.putInt("total_raw", total);
                Navigation.findNavController(v).navigate(R.id.action_tests_ui_to_test_Result, bundle);
            }
        });
        return view;
    }
}
