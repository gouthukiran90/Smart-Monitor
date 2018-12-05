package com.example.videodemo;

import java.util.ArrayList;

public class Schedule {


    String name, cameraOnTime, cameraOffTime;
    ArrayList<String> arrayListForSelectedDays;

    public Schedule(String name, String cameraOnTime, String cameraOffTime,ArrayList<String> arrayListForSelectedDays) {
        this.name = name;
        this.cameraOnTime = cameraOnTime;
        this.cameraOffTime = cameraOffTime;
        this.arrayListForSelectedDays=arrayListForSelectedDays;
    }

    public ArrayList<String> getArrayListForSelectedDays() {
        return arrayListForSelectedDays;
    }

    public void setArrayListForSelectedDays(ArrayList<String> arrayListForSelectedDays) {
        this.arrayListForSelectedDays = arrayListForSelectedDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCameraOnTime() {
        return cameraOnTime;
    }

    public void setCameraOnTime(String cameraOnTime) {
        this.cameraOnTime = cameraOnTime;
    }

    public String getCameraOffTime() {
        return cameraOffTime;
    }

    public void setCameraOffTime(String cameraOffTime) {
        this.cameraOffTime = cameraOffTime;
    }
}
