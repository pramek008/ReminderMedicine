package com.example.reminderapp2;

//merupakan class untuk model data dari data yang akan digunakan
//class ini tidak memiliki ketergantungan dengan klas lain
public class Alarm {
    //inisiasi variabel yang akan digunakan
    int id;
    private String title;
    private String obat;
    private String time;
    private String date;

    //membuat construktor
    Alarm(int id, String title, String obat, String time, String date) {
        this.id = id;
        this.title = title;
        this.obat = obat;
        this.time = time;
        this.date = date;
    }

    //Set di gunakan untuk mengisikan nilai ke dalam atribut
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
