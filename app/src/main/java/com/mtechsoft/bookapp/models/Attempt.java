package com.mtechsoft.bookapp.models;

public class Attempt {
    private String id, chapterId, totalQuestions, attempted, unAttempted, percentage, correct, incorrect;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(String totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getAttempted() {
        return attempted;
    }

    public void setAttempted(String attempted) {
        this.attempted = attempted;
    }

    public String getUnAttempted() {
        return unAttempted;
    }

    public void setUnAttempted(String unAttempted) {
        this.unAttempted = unAttempted;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(String incorrect) {
        this.incorrect = incorrect;
    }

    public String getTitle() {
        return "- TOTAL Mcqs : "+getTotalQuestions()+"\n- ATTEMPTED: "+getAttempted()+"\n - UN-ATTEMPTED : "+getUnAttempted()+"\n" +
                "-TRUE: "+getCorrect()+"\n-FALSE: "+getIncorrect()+"\n-PERCENTAGE: "+getPercentage()+" %";
    }
}
