package com.example.finalaapplication.ui.view_part_example;

import java.io.Serializable;

public class AssignmentItem implements Serializable {
    private String assignId;
    private String testId;
    private String testName;
    private String testDesc;
    private String className;
    private String teacherName;
    private String teacherId;
    private int submittedCount;
    private int totalCount;
    private String role;

    public AssignmentItem(String assignId, String testId, String testName, String testDesc, String className,
                          String teacherName, String teacherId, int submittedCount, int totalCount, String role) {
        this.assignId = assignId;
        this.testId = testId;
        this.testName = testName;
        this.testDesc = testDesc;
        this.className = className;
        this.teacherName = teacherName;
        this.teacherId = teacherId;
        this.submittedCount = submittedCount;
        this.totalCount = totalCount;
        this.role = role;
    }

    public String getAssignId() { return assignId; }
    public String getTestId() { return testId; }
    public String getTestName() { return testName; }
    public String getTestDesc() { return testDesc; }
    public String getClassName() { return className; }
    public String getTeacherName() { return teacherName; }
    public String getTeacherId() { return teacherId; }
    public int getSubmittedCount() { return submittedCount; }
    public int getTotalCount() { return totalCount; }
    public String getRole() { return role; }
}
