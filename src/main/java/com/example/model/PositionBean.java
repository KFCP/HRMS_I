package com.example.model;

public class PositionBean {
    private int id;
    private String positionName;
    private int level;

    // Default constructor
    public PositionBean() {
    }

    // Constructor with all fields
    public PositionBean(int id, String positionName, int level) {
        this.id = id;
        this.positionName = positionName;
        this.level = level;
    }

    // Getter and setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
