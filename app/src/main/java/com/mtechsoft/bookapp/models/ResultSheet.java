package com.mtechsoft.bookapp.models;

public class ResultSheet {
    int pages;
    int id;
    String questionNo;
    String chapterDetail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public String getChapterDetail() {
        return chapterDetail;
    }

    public void setChapterDetail(String chapterDetail) {
        this.chapterDetail = chapterDetail;
    }
}
