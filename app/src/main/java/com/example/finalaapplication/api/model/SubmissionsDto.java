package com.example.finalaapplication.api.model;

import com.google.gson.annotations.SerializedName;

public class SubmissionsDto {
    @SerializedName("assignment_id")
    private long assignmentId;
    @SerializedName("student_id")
    private long studentId;

    public SubmissionsDto() {}

    public SubmissionsDto(long assignmentId, long studentId) {
        this.assignmentId = assignmentId;
        this.studentId = studentId;
    }

    public long getAssignmentId() { return assignmentId; }
    public long getStudentId() { return studentId; }
}
