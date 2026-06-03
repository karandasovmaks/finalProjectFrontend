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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalaapplication.R;
import com.example.finalaapplication.api.ApiClient;
import com.example.finalaapplication.api.ApiService;
import com.example.finalaapplication.api.model.ClassDto;
import com.example.finalaapplication.api.model.ClassMemberDto;
import com.example.finalaapplication.ui.Adapters.ClassAdapter;
import com.example.finalaapplication.ui.view_part_example.ClassItem;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragmentperson extends Fragment {

    private String currentUserId, currentRole;
    private ApiService api;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragmentperson, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        currentUserId = prefs.getString("user_id", "");
        currentRole = prefs.getString("role", "STUDENT");
        api = ApiClient.getService();

        MaterialAutoCompleteTextView inputField = view.findViewById(R.id.class_input);
        AppCompatButton actionButton = view.findViewById(R.id.class_action_main_btn);
        recyclerView = view.findViewById(R.id.class_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if ("TEACHER".equals(currentRole)) {
            inputField.setHint("Название класса");
            actionButton.setText("Создать класс");
        } else {
            inputField.setHint("Код класса");
            actionButton.setText("Присоединиться");
        }

        loadClassList();

        actionButton.setOnClickListener(v -> {
            String text = inputField.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(getContext(), "Заполните поле", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("TEACHER".equals(currentRole)) {
                Map<String, Object> body = new HashMap<>();
                body.put("name", text);
                body.put("teacher_id", Long.parseLong(currentUserId));

                api.createClass(body).enqueue(new Callback<ClassDto>() {
                    @Override
                    public void onResponse(Call<ClassDto> call, Response<ClassDto> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(getContext(), "Класс создан! Код: " + response.body().getCode(), Toast.LENGTH_LONG).show();
                            inputField.setText("");
                            loadClassList();
                        } else {
                            Toast.makeText(getContext(), "Ошибка создания класса", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ClassDto> call, Throwable t) {
                        Toast.makeText(getContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Map<String, Object> body = new HashMap<>();
                body.put("code", text);
                body.put("student_id", Long.parseLong(currentUserId));

                api.joinClass(body).enqueue(new Callback<ClassMemberDto>() {
                    @Override
                    public void onResponse(Call<ClassMemberDto> call, Response<ClassMemberDto> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Вы присоединились к классу!", Toast.LENGTH_SHORT).show();
                            inputField.setText("");
                            loadClassList();
                        } else {
                            Toast.makeText(getContext(), "Неверный код класса", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ClassMemberDto> call, Throwable t) {
                        Toast.makeText(getContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

    private void loadClassList() {
        if ("TEACHER".equals(currentRole)) {
            api.getTeacherClasses(Long.parseLong(currentUserId)).enqueue(new Callback<List<ClassDto>>() {
                @Override
                public void onResponse(Call<List<ClassDto>> call, Response<List<ClassDto>> response) {
                    if (!response.isSuccessful() || response.body() == null) return;
                    List<ClassItem> list = new ArrayList<>();
                    for (ClassDto dto : response.body()) {
                        list.add(new ClassItem(String.valueOf(dto.getId()), dto.getName(), dto.getCode(), "TEACHER"));
                    }
                    setAdapter(list);
                }

                @Override
                public void onFailure(Call<List<ClassDto>> call, Throwable t) {
                    Toast.makeText(getContext(), "Ошибка загрузки: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            api.getStudentClasses(Long.parseLong(currentUserId)).enqueue(new Callback<List<ClassDto>>() {
                @Override
                public void onResponse(Call<List<ClassDto>> call, Response<List<ClassDto>> response) {
                    if (!response.isSuccessful() || response.body() == null) return;
                    List<ClassItem> list = new ArrayList<>();
                    for (ClassDto dto : response.body()) {
                        list.add(new ClassItem(String.valueOf(dto.getId()), dto.getName(), dto.getCode(), "STUDENT"));
                    }
                    setAdapter(list);
                }

                @Override
                public void onFailure(Call<List<ClassDto>> call, Throwable t) {
                    Toast.makeText(getContext(), "Ошибка загрузки: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setAdapter(List<ClassItem> list) {
        ClassAdapter adapter = new ClassAdapter(getContext(), list, currentUserId, new ClassAdapter.OnClassActionListener() {
            @Override
            public void onDeleteClass(String classId) {
                api.deleteClass(Long.parseLong(classId)).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadClassList();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onLeaveClass(String classId) {
                long classIdLong = Long.parseLong(classId);
                long studentIdLong = Long.parseLong(currentUserId);
                api.leaveClass(classIdLong, studentIdLong).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadClassList();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
