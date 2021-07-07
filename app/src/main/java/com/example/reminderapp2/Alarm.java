package com.example.reminderapp2;

public class Alarm {

    int id;
    private String title;
    private String obat;
    private String time;
    private String date;

    Alarm(int id, String title, String obat, String time, String date) {
        this.id = id;
        this.title = title;
        this.obat = obat;
        this.time = time;
        this.date = date;
    }

    int getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    String getObat() {
        return obat;
    }

    String getTime() {
        return time;
    }

    String getDate() { return date; }
}
