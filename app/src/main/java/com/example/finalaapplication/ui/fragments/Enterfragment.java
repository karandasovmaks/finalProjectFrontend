package com.example.finalaapplication.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalaapplication.R;
import com.example.finalaapplication.api.ApiClient;
import com.example.finalaapplication.api.model.User;
import com.example.finalaapplication.ui.screens.Activity_second;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Enterfragment extends Fragment {

    private MaterialAutoCompleteTextView loginInput, passwordInput;
    private AppCompatButton registrationButton;
    private TextView haveAccountText;
    private MaterialButtonToggleGroup roleToggleGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enterfragment, container, false);

        loginInput = view.findViewById(R.id.loginInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        registrationButton = view.findViewById(R.id.registrationButton);
        haveAccountText = view.findViewById(R.id.haveAcc);
        roleToggleGroup = view.findViewById(R.id.roleToggleGroup);

        registrationButton.setOnClickListener(v -> {
            String username = loginInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedId = roleToggleGroup.getCheckedButtonId();
            String role = (selectedId == R.id.btnStudent) ? "STUDENT" : "TEACHER";

            registrationButton.setEnabled(false);
            User user = new User(username, password, role);

            ApiClient.getService().checkUser(username).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                        registrationButton.setEnabled(true);
                        Toast.makeText(getContext(), "такой пользователь уже существует", Toast.LENGTH_SHORT).show();
                    } else {
                        ApiClient.getService().createUser(user).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> res) {
                                registrationButton.setEnabled(true);
                                if (res.isSuccessful() && res.body() != null) {
                                    User saved = res.body();
                                    saveCurrentUser(saved.getId(), saved.getUsername(), saved.getRole());
                                    Toast.makeText(getContext(),"Регистрация успешна!",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(),Activity_second.class));
                                } else {
                                    Toast.makeText(getContext(), "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                registrationButton.setEnabled(true);
                                Toast.makeText(getContext(), "Ошибка: " + t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    registrationButton.setEnabled(true);
                    Toast.makeText(getContext(), "Ошибка сети: " + t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        });

        haveAccountText.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_enterfragment_to_fragment_log_in)
        );

        return view;
    }

    private void saveCurrentUser(long userId, String username, String role) {
        SharedPreferences prefs = getContext().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        prefs.edit()
                .putString("user_id", String.valueOf(userId))
                .putString("username", username)
                .putString("role", role)
                .apply();
    }
}
