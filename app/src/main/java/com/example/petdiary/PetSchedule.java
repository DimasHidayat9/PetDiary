package com.example.petdiary;

public class PetSchedule {
    private String id, name, morningTime, eveningTime;
    private boolean isNotifyActive;

    public PetSchedule(String id, String name, String morningTime, String eveningTime, boolean isNotifyActive) {
        this.id = id; this.name = name; this.morningTime = morningTime; this.eveningTime = eveningTime; this.isNotifyActive = isNotifyActive;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getMorningTime() { return morningTime; }
    public String getEveningTime() { return eveningTime; }
    public boolean isNotifyActive() { return isNotifyActive; }
    public void setNotifyActive(boolean active) { isNotifyActive = active; }
}