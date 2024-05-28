package com.example.smartcam;

public class NotificationData {
    private String title;
    private String date;
    private String time;

    public NotificationData() {
        // Default constructor required for calls to DataSnapshot.getValue(NotificationData.class)
    }

    public NotificationData(String title, String date, String time) {
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
