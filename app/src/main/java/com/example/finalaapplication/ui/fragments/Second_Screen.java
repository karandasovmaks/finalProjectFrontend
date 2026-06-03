package com.example.finalaapplication.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalaapplication.R;
import com.example.finalaapplication.api.ApiService;
import com.example.finalaapplication.api.ApiClient;
import com.example.finalaapplication.api.model.AssignmentDto;
import com.example.finalaapplication.api.model.TaskDto;
import com.example.finalaapplication.ui.Adapters.AssignmentsAdapter;
import com.example.finalaapplication.ui.view_part_example.AssignmentItem;
import com.example.finalaapplication.ui.view_part_example.Task;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Second_Screen extends Fragment implements AssignmentsAdapter.OnAssignmentActionListener {

    private RecyclerView recyclerView;
    private String currentUserId, currentRole;
    private ApiService api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second__screen, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences prefs = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        currentUserId = prefs.getString("user_id", "");
        currentRole = prefs.getString("role", "STUDENT");
        api = ApiClient.getService();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAssignments();
    }

    private void loadAssignments() {
        long userId = Long.parseLong(currentUserId);

        Call<List<AssignmentDto>> call = "TEACHER".equals(currentRole)
                ? api.getAssignmentsForTeacher(userId)
                : api.getAssignmentsForStudent(userId);

        call.enqueue(new Callback<List<AssignmentDto>>() {
            @Override
            public void onResponse(Call<List<AssignmentDto>> call, Response<List<AssignmentDto>> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                List<AssignmentItem> list = new ArrayList<>();
                for (AssignmentDto dto : response.body()) {
                    list.add(new AssignmentItem(
                            String.valueOf(dto.getAssignId()),
                            String.valueOf(dto.getTestId()),
                            dto.getTestName(),
                            dto.getTestDesc(),
                            dto.getClassName(),
                            "STUDENT".equals(currentRole) ? dto.getTeacherName() : null,
                            "STUDENT".equals(currentRole) ? String.valueOf(dto.getTeacherId()) : null,
                            dto.getSubmittedCount(),
                            dto.getTotalCount(),
                            currentRole
                    ));
                }
                recyclerView.setAdapter(new AssignmentsAdapter(
                        getContext(), Second_Screen.this, list, currentRole, currentUserId));
            }

            @Override
            public void onFailure(Call<List<AssignmentDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTakeTest(AssignmentItem item) {
        api.getTasksByTestId(Long.parseLong(item.getTestId())).enqueue(new Callback<List<TaskDto>>() {
            @Override
            public void onResponse(Call<List<TaskDto>> call, Response<List<TaskDto>> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                List<Task> tasks = new ArrayList<>();
                for (TaskDto dto : response.body()) {
                    tasks.add(new Task(dto.getTaskText(), dto.getAnswer()));
                }

                Bundle bundle = new Bundle();
                bundle.putString("test_name", item.getTestName());
                bundle.putString("test_desc", item.getTestDesc());
                bundle.putString("test_id", item.getTestId());
                bundle.putString("assignment_id", item.getAssignId());
                bundle.putSerializable("test_task_list", (ArrayList<Task>) tasks);

                Navigation.findNavController(getView()).navigate(R.id.action_second_Screen_to_tests_ui, bundle);
            }

            @Override
            public void onFailure(Call<List<TaskDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка загрузки заданий: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
