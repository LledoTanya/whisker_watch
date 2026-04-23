package com.example.whisker_watch;

public class HelpCenter {
    private String name;
    private String location;
    private String contact;
    private boolean isHeader;

    public HelpCenter(String name, String location, String contact) {
        this.name = name;
        this.location = location;
        this.contact = contact;
        this.isHeader = false;
    }

    public HelpCenter(String name) {
        this.name = name;
        this.isHeader = true;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getContact() { return contact; }
    public boolean isHeader() { return isHeader; }
}