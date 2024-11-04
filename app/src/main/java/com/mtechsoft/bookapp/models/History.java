package com.mtechsoft.bookapp.models;

public class History {
    int pages;
    int id;
    String chapterName;
    String chapterResult;
    String chapterDetail;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getChapterResult() {
        return chapterResult;
    }

    public void setChapterResult(String chapterResult) {
        this.chapterResult = chapterResult;
    }

    public String getChapterDetail() {
        return chapterDetail;
    }

    public void setChapterDetail(String chapterDetail) {
        this.chapterDetail = chapterDetail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
