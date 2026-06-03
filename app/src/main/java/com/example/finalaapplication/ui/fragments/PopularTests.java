package com.example.finalaapplication.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalaapplication.R;
import com.example.finalaapplication.api.ApiClient;
import com.example.finalaapplication.api.ApiService;
import com.example.finalaapplication.api.model.ClassDto;
import com.example.finalaapplication.api.model.TaskDto;
import com.example.finalaapplication.api.model.TestDto;
import com.example.finalaapplication.ui.Adapters.Popular_tests_Adapter;
import com.example.finalaapplication.ui.view_part_example.Task;
import com.example.finalaapplication.ui.view_part_example.Test_example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularTests extends Fragment implements Popular_tests_Adapter.OnAssignTestListener {
    private RecyclerView recyclerView;
    private String currentUserId, currentRole;
    private ApiService api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_tests, container, false);
        recyclerView = view.findViewById(R.id.recyclerView1);

        SharedPreferences prefs = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        currentUserId = prefs.getString("user_id", "");
        currentRole = prefs.getString("role", "STUDENT");
        api = ApiClient.getService();

        androidx.appcompat.widget.AppCompatButton addTestBtn = view.findViewById(R.id.button_add_test);
        if ("TEACHER".equals(currentRole)) {
            addTestBtn.setVisibility(View.VISIBLE);
            addTestBtn.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.action_popularTests2_to_testCreate)
            );
        } else {
            addTestBtn.setVisibility(View.GONE);
        }

        loadTests();

        return view;
    }

    private void loadTests() {
        api.getAllTests().enqueue(new Callback<List<TestDto>>() {
            @Override
            public void onResponse(Call<List<TestDto>> call, Response<List<TestDto>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(getContext(), "Ошибка загрузки тестов", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Test_example> list = new ArrayList<>();
                for (TestDto dto : response.body()) {
                    list.add(new Test_example(
                            String.valueOf(dto.getId()),
                            dto.getName(),
                            dto.getDescription(),
                            dto.getImageLink(),
                            new ArrayList<Task>()
                    ));
                }

                Popular_tests_Adapter adapter = new Popular_tests_Adapter(getContext(), list, currentRole, currentUserId, PopularTests.this::onTestClicked, PopularTests.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<TestDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onTestClicked(Test_example test) {
        api.getTasksByTestId(Long.parseLong(test.getTest_id())).enqueue(new Callback<List<TaskDto>>() {
            @Override
            public void onResponse(Call<List<TaskDto>> call, Response<List<TaskDto>> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                List<Task> tasks = new ArrayList<>();
                for (TaskDto dto : response.body()) {
                    tasks.add(new Task(dto.getTaskText(), dto.getAnswer()));
                }
                test.setTasks(tasks);

                Bundle bundle = new Bundle();
                bundle.putString("test_name", test.getTest_name());
                bundle.putString("test_desc", test.getTest_description());
                bundle.putString("test_id", test.getTest_id());
                bundle.putSerializable("test_task_list", (ArrayList<Task>) tasks);

                Navigation.findNavController(getView()).navigate(R.id.action_popularTests2_to_tests_ui, bundle);
            }

            @Override
            public void onFailure(Call<List<TaskDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка загрузки заданий: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAssignTest(String testId) {
        api.getTeacherClasses(Long.parseLong(currentUserId)).enqueue(new Callback<List<ClassDto>>() {
            @Override
            public void onResponse(Call<List<ClassDto>> call, Response<List<ClassDto>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().isEmpty()) {
                    Toast.makeText(getContext(), "Сначала создайте класс на странице профиля", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<ClassDto> classes = response.body();
                String[] classNames = new String[classes.size()];
                Long[] classIds = new Long[classes.size()];
                for (int i = 0; i < classes.size(); i++) {
                    classNames[i] = classes.get(i).getName();
                    classIds[i] = classes.get(i).getId();
                }

                new AlertDialog.Builder(getContext())
                        .setTitle("Выберите класс")
                        .setItems(classNames, (dialog, which) -> {
                            Map<String, Object> body = new HashMap<>();
                            body.put("test_id", Long.parseLong(testId));
                            body.put("class_id", classIds[which]);
                            body.put("teacher_id", Long.parseLong(currentUserId));

                            api.createAssignment(body).enqueue(new Callback<com.example.finalaapplication.api.model.AssignmentDto>() {
                                @Override
                                public void onResponse(Call<com.example.finalaapplication.api.model.AssignmentDto> call, Response<com.example.finalaapplication.api.model.AssignmentDto> resp) {
                                    if (resp.isSuccessful()) {
                                        Toast.makeText(getContext(), "Задание создано!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<com.example.finalaapplication.api.model.AssignmentDto> call, Throwable t) {
                                    Toast.makeText(getContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .show();
            }

            @Override
            public void onFailure(Call<List<ClassDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка загрузки классов: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
