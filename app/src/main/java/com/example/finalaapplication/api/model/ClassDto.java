package com.example.finalaapplication.api.model;

import com.google.gson.annotations.SerializedName;

public class ClassDto {
    private long id;
    private String name;
    private String code;
    @SerializedName("teacher_id")
    private long teacherId;

    public long getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public long getTeacherId() { return teacherId; }
}
