package com.example.whisker_watch;

import java.io.Serializable;

public class Report implements Serializable {
    private String description;
    private String location;
    private String imagePath; // Store the URI as a string

    public Report(String description, String location, String imagePath) {
        this.description = description;
        this.location = location;
        this.imagePath = imagePath;
    }

    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getImagePath() { return imagePath; }
}