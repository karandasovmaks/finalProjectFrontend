package com.example.finalaapplication.api.model;

import com.google.gson.annotations.SerializedName;

public class AssignmentDto {
    @SerializedName("assign_id")
    private long assignId;
    @SerializedName("test_id")
    private long testId;
    @SerializedName("test_name")
    private String testName;
    @SerializedName("test_desc")
    private String testDesc;
    @SerializedName("class_name")
    private String className;
    @SerializedName("class_id")
    private long classId;
    @SerializedName("submitted_count")
    private int submittedCount;
    @SerializedName("total_count")
    private int totalCount;
    @SerializedName("teacher_name")
    private String teacherName;
    @SerializedName("teacher_id")
    private long teacherId;
    private boolean submitted;

    public long getAssignId() { return assignId; }
    public long getTestId() { return testId; }
    public String getTestName() { return testName; }
    public String getTestDesc() { return testDesc; }
    public String getClassName() { return className; }
    public long getClassId() { return classId; }
    public int getSubmittedCount() { return submittedCount; }
    public int getTotalCount() { return totalCount; }
    public String getTeacherName() { return teacherName; }
    public long getTeacherId() { return teacherId; }
    public boolean isSubmitted() { return submitted; }
}
