package com.example.finalaapplication.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalaapplication.R;
import com.example.finalaapplication.api.ApiClient;
import com.example.finalaapplication.api.ApiService;
import com.example.finalaapplication.api.model.TaskDto;
import com.example.finalaapplication.api.model.TestDto;
import com.example.finalaapplication.ui.Adapters.TaskCreateAdapter;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestCreate extends Fragment {

    private MaterialAutoCompleteTextView etTestName, etTestDesc;
    private RecyclerView recyclerView;
    private TaskCreateAdapter adapter;
    private ArrayList<TaskCreateAdapter.TaskCreateItem> taskItems;
    private long currentUserId;
    private ApiService api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_create, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        currentUserId = Long.parseLong(prefs.getString("user_id", "0"));
        api = ApiClient.getService();

        etTestName = view.findViewById(R.id.test_create_name);
        etTestDesc = view.findViewById(R.id.test_create_desc);
        recyclerView = view.findViewById(R.id.test_create_recycler);
        AppCompatButton btnAddTask = view.findViewById(R.id.test_create_add_task);
        AppCompatButton btnSave = view.findViewById(R.id.test_create_save);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskItems = new ArrayList<>();
        adapter = new TaskCreateAdapter(taskItems);
        recyclerView.setAdapter(adapter);

        btnAddTask.setOnClickListener(v -> {
            taskItems.add(new TaskCreateAdapter.TaskCreateItem());
            adapter.notifyItemInserted(taskItems.size() - 1);
        });

        btnSave.setOnClickListener(v -> {
            String name = etTestName.getText().toString().trim();
            String desc = etTestDesc.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(getContext(), "Введите название теста", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < taskItems.size(); i++) {
                TaskCreateAdapter.TaskCreateItem item = taskItems.get(i);
                if (item.getQuestion().isEmpty() || item.getAnswer().isEmpty()) {
                    Toast.makeText(getContext(), "Заполните вопрос и ответ в задании " + (i + 1), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (taskItems.isEmpty()) {
                Toast.makeText(getContext(), "Добавьте хотя бы один вопрос", Toast.LENGTH_SHORT).show();
                return;
            }

            TestDto test = new TestDto(name, desc, 0, currentUserId);
            api.createTest(test).enqueue(new Callback<TestDto>() {
                @Override
                public void onResponse(Call<TestDto> call, Response<TestDto> response) {
                    if (!response.isSuccessful() || response.body() == null) {
                        Toast.makeText(getContext(), "Ошибка создания теста", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    long testId = response.body().getId();
                    saveTasksSequentially(testId, 0, v);
                }

                @Override
                public void onFailure(Call<TestDto> call, Throwable t) {
                    Toast.makeText(getContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    private void saveTasksSequentially(long testId, int index, View view) {
        if (index >= taskItems.size()) {
            Toast.makeText(getContext(), "Тест создан!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigate(R.id.action_testCreate_to_popularTests2);
            return;
        }

        TaskCreateAdapter.TaskCreateItem item = taskItems.get(index);
        TaskDto task = new TaskDto(testId, item.getQuestion(), item.getAnswer(), index + 1);

        api.createTask(task).enqueue(new Callback<TaskDto>() {
            @Override
            public void onResponse(Call<TaskDto> call, Response<TaskDto> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Ошибка сохранения задания", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveTasksSequentially(testId, index + 1, view);
            }

            @Override
            public void onFailure(Call<TaskDto> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
