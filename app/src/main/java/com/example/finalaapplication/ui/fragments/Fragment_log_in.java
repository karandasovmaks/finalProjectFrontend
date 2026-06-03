package com.example.finalaapplication.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_log_in extends Fragment {

    private MaterialAutoCompleteTextView loginInput, passwordInput;
    private AppCompatButton loginButton;
    private TextView noAccountText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        loginInput = view.findViewById(R.id.loginInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton = view.findViewById(R.id.loginButton);
        noAccountText = view.findViewById(R.id.noAccountText);

        loginButton.setOnClickListener(v -> {
            String username = loginInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            loginButton.setEnabled(false);

            ApiClient.getService().checkUser(username).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && Boolean.FALSE.equals(response.body())) {
                        loginButton.setEnabled(true);
                        Toast.makeText(getContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ApiClient.getService().getUserByUsername(username).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> res) {
                            loginButton.setEnabled(true);
                            if (res.isSuccessful() && res.body() != null) {
                                User user = res.body();
                                if (user.getPasswordHash().equals(password)) {
                                    saveCurrentUser(user.getId(), user.getUsername(), user.getRole());
                                    Toast.makeText(getContext(), "Вход выполнен!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), Activity_second.class));
                                } else {
                                    Toast.makeText(getContext(), "Неверный пароль", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Ошибка входа", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            loginButton.setEnabled(true);
                            Toast.makeText(getContext(), "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    loginButton.setEnabled(true);
                    Toast.makeText(getContext(), "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        noAccountText.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_fragment_log_in_to_enterfragment)
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
