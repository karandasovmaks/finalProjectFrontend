package com.example.finalaapplication.ui.view_part_example;

import java.io.Serializable;
import java.util.List;

public class Test_example implements Serializable {
    private String test_id;
    private String test_name;
    private String test_description;
    private int image_link;
    private List<Task> listoftasks;

    public Test_example(String test_id, String test_name, String test_description, int image_link, List<Task> listoftasks) {
        this.test_id = test_id;
        this.test_name = test_name;
        this.test_description = test_description;
        this.image_link = image_link;
        this.listoftasks = listoftasks;
    }

    public String getTest_id() {
        return test_id;
    }

    public String getTest_name() {
        return test_name;
    }

    public String getTest_description() {
        return test_description;
    }

    public int getImage_link() {
        return  image_link;
    }

    public List<Task> getListoftasks() {
        return listoftasks;
    }

    public void setTasks(List<Task> listoftasks) {
        this.listoftasks = listoftasks;
    }

}
