package com.example.finalaapplication.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalaapplication.R;
import com.example.finalaapplication.api.ApiClient;
import com.example.finalaapplication.api.model.SubmissionsDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Test_Result extends Fragment {

    private AppCompatButton appCompatButton;
    private TextView tv;
    private String percentage;
    private String testId;
    private String assignmentId;
    private int score;
    private int total;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            percentage = getArguments().getString("score");
            testId = getArguments().getString("test_id");
            assignmentId = getArguments().getString("assignment_id");
            score = getArguments().getInt("score_raw", 0);
            total = getArguments().getInt("total_raw", 0);
            percentage += "%";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test__result, container, false);
        tv = view.findViewById(R.id.textView6);
        appCompatButton = view.findViewById(R.id.exit_testbutton);
        tv.setText(percentage);

        saveSubmission();

        appCompatButton.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_test_Result_to_popularTests2));
        return view;
    }

    private void saveSubmission() {
        SharedPreferences prefs = getContext().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);

        if (userId == null || testId == null) return;
        if (assignmentId == null || assignmentId.isEmpty()) return;

        SubmissionsDto sub = new SubmissionsDto(Long.parseLong(assignmentId), Long.parseLong(userId));
        ApiClient.getService().createSubmission(sub).enqueue(new Callback<SubmissionsDto>() {
            @Override
            public void onResponse(Call<SubmissionsDto> call, Response<SubmissionsDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Решение отправлено!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubmissionsDto> call, Throwable t) {
                // silent fail — результат уже показан пользователю
            }
        });
    }
}
