package com.example.finalaapplication.ui.view_part_example;

import java.io.Serializable;

public class Task  implements Serializable {
    private String taskText;

    private String userInput;

    private String anser;

    private boolean anered = false;

    public Task(String taskText, String anser) {
        this.taskText = taskText;
        this.userInput = null;
        this.anser = anser;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public String getTaskText() {
        return taskText;
    }

    public String getUserInput() {
        return userInput;
    }

    public String getAnser() {
        return anser;
    }

    public boolean isAnered() {
        return anered;
    }

    public void setAnered(boolean anered) {
        this.anered = anered;
    }

    public  boolean chekAnser(){
        if(anser.equals(userInput)){
            return true;
        }
        return  false;
    }
}
