package com.mtechsoft.bookapp.models;

public class Poem {
  int id;
  String poemName;
  String status = "";
  int color;

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
  }

  public String getPoemName() {
    return poemName;
  }

  public void setPoemName(String chapterName) {
    this.poemName = chapterName;
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
