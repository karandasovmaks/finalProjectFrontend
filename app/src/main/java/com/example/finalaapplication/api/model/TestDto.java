package com.example.finalaapplication.api.model;

import com.google.gson.annotations.SerializedName;

public class TestDto {
    private long id;
    private String name;
    private String description;
    @SerializedName("image_link")
    private Integer imageLink;
    @SerializedName("created_by")
    private long createdBy;

    public TestDto() {}

    public TestDto(String name, String description, Integer imageLink, long createdBy) {
        this.name = name;
        this.description = description;
        this.imageLink = imageLink;
        this.createdBy = createdBy;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Integer getImageLink() { return imageLink; }
    public long getCreatedBy() { return createdBy; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setImageLink(Integer imageLink) { this.imageLink = imageLink; }
    public void setCreatedBy(long createdBy) { this.createdBy = createdBy; }
}
