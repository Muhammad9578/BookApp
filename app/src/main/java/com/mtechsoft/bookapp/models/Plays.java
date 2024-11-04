package com.mtechsoft.bookapp.models;

public class Plays {
    int id;
    String playName;
    String status = "";

    public String getPlayName() {
        return playName;
    }

    public void setPlayName(String chapterName) {
        this.playName = chapterName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String type) {
        this.status = type;
    }
}
