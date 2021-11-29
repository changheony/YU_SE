package com.example.nevi_menu.ui.home;

public class BodyRecord {
    private String date;
    private String currentWeight;
    private String targetWeight;
    private String height;

    public BodyRecord() {}

    public BodyRecord(String date, String currentWeight, String targetWeight, String height)
    {
        this.date = date;
        this.currentWeight = currentWeight;
        this.targetWeight = targetWeight;
        this.height = height;
    }

    public String getCurrentWeight() {
        return currentWeight;
    }

    public String getTargetWeight() {
        return targetWeight;
    }

    public String getHeight() {
        return height;
    }

    public String getDate() {return date;}

    public void setDate(String date) {
        this.date = date;
    }
}
