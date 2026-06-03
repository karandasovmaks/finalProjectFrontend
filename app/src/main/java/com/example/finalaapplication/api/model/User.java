package com.example.finalaapplication.api.model;

import com.google.gson.annotations.SerializedName;

public class User {
    private long id;
    private String username;
    @SerializedName("password_hash")
    private String passwordHash;
    private String role;

    public User(String username, String passwordHash, String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public long getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
}
