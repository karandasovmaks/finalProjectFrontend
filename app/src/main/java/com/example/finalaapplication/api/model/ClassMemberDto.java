package com.example.finalaapplication.api.model;

import com.google.gson.annotations.SerializedName;

public class ClassMemberDto {
    private long id;
    @SerializedName("class_id")
    private long classId;
    @SerializedName("student_id")
    private long studentId;

    public long getId() { return id; }
    public long getClassId() { return classId; }
    public long getStudentId() { return studentId; }
}
