package com.example.videodemo;

public class VideoMotionData {

    private int imageOfVideoFrame,timeFrame;
    private String title , timeStamp;

    public VideoMotionData(int imageOfVideoFrame, String title, int timeFrame, String timeStamp) {
        this.imageOfVideoFrame = imageOfVideoFrame;
        this.title = title;
        this.timeFrame = timeFrame;
        this.timeStamp = timeStamp;
    }

    public int getImageOfVideoFrame() {
        return imageOfVideoFrame;
    }

    public void setImageOfVideoFrame(int imageOfVideoFrame) {
        this.imageOfVideoFrame = imageOfVideoFrame;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(int timeFrame) {
        this.timeFrame = timeFrame;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
