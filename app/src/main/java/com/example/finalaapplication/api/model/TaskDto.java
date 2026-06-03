package com.example.finalaapplication.api.model;

import com.google.gson.annotations.SerializedName;

public class TaskDto {
    private long id;
    @SerializedName("test_id")
    private long testId;
    @SerializedName("task_text")
    private String taskText;
    private String answer;
    @SerializedName("order_num")
    private long orderNum;

    public TaskDto() {}

    public TaskDto(long testId, String taskText, String answer, long orderNum) {
        this.testId = testId;
        this.taskText = taskText;
        this.answer = answer;
        this.orderNum = orderNum;
    }

    public long getId() { return id; }
    public long getTestId() { return testId; }
    public String getTaskText() { return taskText; }
    public String getAnswer() { return answer; }
    public long getOrderNum() { return orderNum; }

    public void setTestId(long testId) { this.testId = testId; }
    public void setTaskText(String taskText) { this.taskText = taskText; }
    public void setAnswer(String answer) { this.answer = answer; }
    public void setOrderNum(long orderNum) { this.orderNum = orderNum; }
}
