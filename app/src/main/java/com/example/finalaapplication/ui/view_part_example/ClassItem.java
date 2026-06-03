package com.example.finalaapplication.ui.view_part_example;

import java.io.Serializable;

public class ClassItem implements Serializable {
    private String classId;
    private String className;
    private String classCode;
    private String role;

    public ClassItem(String classId, String className, String classCode, String role) {
        this.classId = classId;
        this.className = className;
        this.classCode = classCode;
        this.role = role;
    }

    public String getClassId() { return classId; }
    public String getClassName() { return className; }
    public String getClassCode() { return classCode; }
    public String getRole() { return role; }
}
